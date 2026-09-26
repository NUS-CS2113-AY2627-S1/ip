"""Run the documented console cases with Java 25 in an isolated data directory."""

import argparse
import datetime
import hashlib
import pathlib
import queue
import re
import shutil
import subprocess
import tempfile
import threading

ROOT = pathlib.Path(__file__).resolve().parents[1]
arguments = argparse.ArgumentParser(description=__doc__)
arguments.add_argument('session_name', nargs='?', default='ui-tests')
arguments.add_argument('--jar', type=pathlib.Path, help='Test an existing JAR without rebuilding source.')
options = arguments.parse_args()
JAR = options.jar.resolve() if options.jar else None
if JAR is not None and not JAR.is_file():
    arguments.error(f'JAR file does not exist: {JAR}')
LABEL = options.session_name
if not re.fullmatch(r'[a-zA-Z0-9_-]+', LABEL):
    raise SystemExit('Use a session name containing letters, numbers, underscores, or hyphens.')
OUT = ROOT / '_temp' / LABEL
OUT.mkdir(parents=True, exist_ok=True)
CLASSES = OUT / 'classes'
CLASSES.mkdir(exist_ok=True)
version = subprocess.run(['java', '-version'], capture_output=True, text=True, check=True)
if not re.search(r'version "25[.\"]', version.stderr + version.stdout):
    raise SystemExit('The UI suite requires Java 25 on PATH.')
if JAR is None:
    subprocess.run(['javac', '--release', '25', '-encoding', 'UTF-8', '-d', str(CLASSES),
                    *map(str, (ROOT / 'src/main/java').rglob('*.java'))], check=True)
plan = (ROOT / 'test/ui-test-plan.md').read_text(encoding='utf-8')
cases = re.split(r'^## Test Case ', plan, flags=re.M)[1:]
separator = '_' * 60 + '\n'
greeting = re.search(r'Expected greeting:\s*```text\n(.*?)```', plan, re.S).group(1) + separator
transcript = []
process = None
lines = None


def compare(actual, expected, context):
    """Report the precise mismatch and stop before sending another command."""
    if actual != expected:
        transcript.append(f'FAILED {context}\nACTUAL\n{actual}\nEXPECTED\n{expected}')
        raise AssertionError(transcript[-1])


def read_response():
    result = ''
    while True:
        try:
            line = lines.get(timeout=10)
        except queue.Empty:
            raise AssertionError('Timed out waiting for console output. Received:\n' + result)
        if line is None:
            raise AssertionError('Application exited before completing its response:\n' + result)
        result += line
        if line == separator:
            return result


def start(work, case):
    global process, lines
    target = ['-jar', 'ip.jar'] if JAR else ['-cp', str(CLASSES), 'ui.SamSquare']
    process = subprocess.Popen(['java', '-Dstdout.encoding=UTF-8', '-Dstderr.encoding=UTF-8',
                                '-Duser.language=fr', '-Duser.country=FR',
                                *target], cwd=work,
                               stdin=subprocess.PIPE, stdout=subprocess.PIPE,
                               stderr=subprocess.STDOUT, text=True, encoding='utf-8', bufsize=1)
    lines = queue.Queue()

    def collect(output, output_lines):
        for line in output:
            output_lines.put(line)
        output_lines.put(None)

    threading.Thread(target=collect, args=(process.stdout, lines), daemon=True).start()
    actual = read_response()
    transcript.append('STARTUP\n' + actual)
    compare(actual, greeting, 'startup greeting')
    notice = re.search(r'Startup notice.*?:\s*```text\n(.*?)```', case, re.S)
    if notice:
        actual = ''.join(lines.get(timeout=10) for _ in notice.group(1).splitlines())
        transcript.append('STARTUP NOTICE\n' + actual)
        compare(actual, notice.group(1), 'startup notice')


with tempfile.TemporaryDirectory(prefix='samsquare-ui-') as work:
    if JAR:
        # Run a copy from an empty folder, preserving the user's artifact and data.
        copied_jar = pathlib.Path(work) / 'ip.jar'
        shutil.copy2(JAR, copied_jar)
        digest = hashlib.sha256(copied_jar.read_bytes()).hexdigest()
        transcript.append(f'JAR {JAR}\nSHA-256 {digest}\n')
    task_file = pathlib.Path(work) / 'data' / 'samsquare.txt'
    count = 0
    try:
        for index, case in enumerate(cases):
            title = case.splitlines()[0]
            fixture = re.search(r'Saved file before startup:\s*```text\n(.*?)```', case, re.S)
            if fixture:
                assert process is None or process.poll() == 0
                task_file.parent.mkdir(exist_ok=True)
                task_file.write_bytes(fixture.group(1).encode('utf-8'))
            saved_before = task_file.read_bytes() if task_file.exists() else None
            if index == 0 or 'Starts a new process' in case:
                assert process is None or process.poll() == 0, 'Prior session did not exit successfully'
                start(work, case)

            inputs, expectations = re.split(r'Expected responses?:', case, maxsplit=1)
            inline = re.search(r'Input: `([^`]+)`', inputs)
            commands = ([inline.group(1)] if inline else
                        re.search(r'Input:\s*```text\n(.*?)```', inputs, re.S).group(1).splitlines())
            expected = re.findall(r'```text\n(.*?)```', expectations, re.S)
            assert len(commands) == len(expected), title
            for command, wanted in zip(commands, expected):
                command = command.replace('<one trailing space>', ' ').replace('<three spaces>', '   ')
                process.stdin.write(command + '\n')
                process.stdin.flush()
                actual = read_response()
                transcript.append('CASE ' + title + '\nINPUT ' + repr(command) + '\n' + actual)
                compare(actual, wanted + separator, 'Case ' + title + ', input ' + repr(command))
                count += 1
                if command.strip() == 'bye':
                    process.stdin.close()
                    assert process.wait(timeout=10) == 0
                    assert lines.get(timeout=10) is None, 'Unexpected output after bye'
                    if task_file.exists():
                        for row in task_file.read_text(encoding='utf-8').splitlines():
                            if row.startswith('D | '):
                                value = row.split(' | ')[3]
                                assert datetime.date.fromisoformat(value).isoformat() == value
            if 'Verify legacy backup after session:' in case:
                backups = list(task_file.parent.glob('samsquare-legacy-dates-*.txt'))
                assert len(backups) == 1, 'Expected exactly one legacy date backup'
                assert backups[0].read_bytes() == fixture.group(1).encode('utf-8'), 'Backup differs from original'
                transcript.append('PASS: Original legacy task file backed up byte for byte.\n')
            if 'Verify task file unchanged after session:' in case:
                saved_after = task_file.read_bytes() if task_file.exists() else None
                assert saved_after == saved_before, 'Read-only commands changed the saved task file'
                transcript.append('PASS: Read-only commands left the saved task file unchanged.\n')
            print('PASS ' + title)
        assert process.poll() == 0
        transcript.append(f'PASS: {len(cases)} cases, {count} commands.\n')
        print(transcript[-1])
    finally:
        if process is not None and process.poll() is None:
            process.kill()
            process.wait()
        (OUT / 'console-record.txt').write_text('\n'.join(transcript), encoding='utf-8')
