"""Exercise damaged files and failed writes in isolated directories."""
from pathlib import Path
import subprocess
import tempfile

root = Path(__file__).resolve().parents[1]
classes = root / 'build/ui-test'
record = ['# Storage error sessions\n']
load_error = ('     OOPS!!! Could not load data/mybff.txt. Your saved file has not been changed. '
              'Check the file and restart.\n')
save_error = ('     OOPS!!! Could not save data/mybff.txt. No changes were made. '
              'Check the file and try again.')


def run(folder, commands):
    result = subprocess.run(['java', '-cp', str(classes), 'mybff.MyBff'], cwd=folder,
                            input=commands, capture_output=True, text=True, timeout=10)
    record.append(f'\nInput:\n```text\n{commands}```\nOutput:\n```text\n{result.stdout}{result.stderr}```\n')
    assert result.returncode == 0 and not result.stderr, result
    return result.stdout


try:
    for payload in [b'T | 2 | bad', b'Z | 0 | bad', b'T | 0', b'T | 0 | ',
                    b'D | 0 | bad', b'E | 0 | bad | noon', b'T | 0 | extra | field',
                    b'T | 0 | valid\nmalformed', b'\xff', b'MyBff storage v2\nT | 0 | !!!']:
        folder = Path(tempfile.mkdtemp(dir=classes))
        (folder / 'data').mkdir()
        target = folder / 'data/mybff.txt'
        target.write_bytes(payload)
        output = run(folder, 'todo replacement\nbye\n')
        assert output.endswith(load_error), output
        assert target.read_bytes() == payload
    folder = Path(tempfile.mkdtemp(dir=classes))
    (folder / 'data').mkdir()
    (folder / 'data/mybff.txt').mkdir()
    assert run(folder, 'bye\n').endswith(load_error)
    folder = Path(tempfile.mkdtemp(dir=classes))
    (folder / 'data').write_text('obstruction')
    output = run(folder, 'todo rejected\nlist\nbye\n')
    # Some file systems reject the load itself when a parent is not a directory.
    assert output.endswith(load_error) or (save_error in output and '1.[T]' not in output)
    assert (folder / 'data').read_text() == 'obstruction'
    folder = Path(tempfile.mkdtemp(dir=classes))
    (folder / 'data').mkdir()
    (folder / 'data/mybff.txt').write_text('\ufeff\nT | 1 | original\n\n', encoding='utf-8')
    output = run(folder, 'list\ntodo pipes | and \\ paths\nbye\n')
    assert '1.[T][X] original' in output
    output = run(folder, 'list\nbye\n')
    assert '2.[T][ ] pipes | and \\ paths' in output
    folder = Path(tempfile.mkdtemp(dir=classes))
    (folder / 'data').mkdir()
    target = folder / 'data/mybff.txt'
    target.write_text(''.join(f'T | 0 | task {i}\n' for i in range(1, 102)))
    output = run(folder, 'list\ntodo next\nbye\n')
    assert '     101.[T][ ] task 101\n' in output
    assert '     Now you have 102 tasks in the list.\n' in output
    assert target.read_text().splitlines() == (
        ['MyBff storage v3'] + [f'T | 0 | task {i}' for i in range(1, 102)] + ['T | 0 | next'])
    output = run(folder, 'list\nbye\n')
    expected_list = ''.join(f'     {i}.[T][ ] task {i}\n' for i in range(1, 102))
    assert expected_list + '     102.[T][ ] next\n' in output
    # Block replacement after startup to exercise rollback of in-memory changes.
    folder = Path(tempfile.mkdtemp(dir=classes))
    (folder / 'data').mkdir()
    target = folder / 'data/mybff.txt'
    target.write_text('T | 0 | first\nT | 1 | second\n')
    process = subprocess.Popen(['java', '-cp', str(classes), 'mybff.MyBff'], cwd=folder,
                               stdin=subprocess.PIPE, stdout=subprocess.PIPE, stderr=subprocess.STDOUT,
                               text=True)
    try:
        while 'What can I do for you?' not in process.stdout.readline():
            assert process.poll() is None
        # Synchronize with a list response, which occurs after loading finishes.
        process.stdin.write('list\n')
        process.stdin.flush()
        while '2.[T][X] second' not in process.stdout.readline():
            assert process.poll() is None
        target.rename(folder / 'data/original.txt')
        target.mkdir()
        (target / 'obstruction').write_text('keep')
        commands = 'mark 1\nunmark 2\ntodo rejected\nlist\nbye\n'
        output, _ = process.communicate(commands, timeout=10)
        record.append(f'\nBlocked replacement input:\n```text\n{commands}```\nOutput:\n```text\n{output}```\n')
        assert process.returncode == 0 and output.count(save_error) == 3, output
        assert '1.[T][ ] first' in output and '2.[T][X] second' in output
        assert "I've added" not in output and "I've marked" not in output
        assert not list((folder / 'data').glob('mybff-*.tmp'))
        assert (folder / 'data/original.txt').read_text() == 'T | 0 | first\nT | 1 | second\n'
    finally:
        if process.poll() is None:
            process.kill()
            process.wait()
    print('PASS: Malformed files, invalid encoding, large lists, blocked paths, BOM, blank lines and special text')
finally:
    (root / 'test/storage-error-sessions.md').write_text(''.join(record), encoding='utf-8')
