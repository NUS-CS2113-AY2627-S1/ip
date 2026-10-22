"""Check saved snapshots before the running chatbot receives its next command."""
from pathlib import Path
import base64
import subprocess
import tempfile

root = Path(__file__).resolve().parents[1]
classes = root / 'build/ui-test'
session = Path(tempfile.mkdtemp(prefix='storage-', dir=classes))
saved = session / 'data/mybff.txt'
def read_saved():
    lines = saved.read_text(encoding='utf-8').splitlines()
    if lines and lines[0] == 'MyBff storage v2':
        return [' | '.join(parts[:2] + [base64.b64decode(f).decode('utf-8') for f in parts[2:]])
                for parts in (line.split(' | ') for line in lines[1:])]
    if lines and lines[0] == 'MyBff storage v3':
        return lines[1:]
    return lines


process = subprocess.Popen(
    ['java', '-cp', str(classes), 'mybff.MyBff'], cwd=session,
    stdin=subprocess.PIPE, stdout=subprocess.PIPE, stderr=subprocess.STDOUT,
    text=True, bufsize=1,
)
record = ['# Storage test session\n\n```text\n']


def read_response():
    while True:
        line = process.stdout.readline()
        if not line:
            raise AssertionError('Chatbot exited before completing its response')
        record.append(line)
        if line == '\n' and any('What can I do' in item for item in record):
            return


try:
    read_response()
    assert not saved.exists(), 'Startup should not write a file'
    snapshots = [
        ('todo read book', ['T | 0 | read book']),
        ('deadline homework /by Friday', ['T | 0 | read book', 'D | 0 | homework | Friday']),
        ('event lunch /from noon /to evening',
         ['T | 0 | read book', 'D | 0 | homework | Friday', 'E | 0 | lunch | noon | evening']),
    ]
    final = snapshots[-1][1]
    snapshots += [('mark 1', ['T | 1 | read book'] + final[1:]),
                  ('unmark 1', final), ('list', final), ('blah', final)]
    for command, expected in snapshots:
        record.append('> ' + command + '\n')
        process.stdin.write(command + '\n')
        process.stdin.flush()
        read_response()
        actual = read_saved()
        assert actual == expected, f'{command}: expected {expected!r}, got {actual!r}'
    record.append('> bye\n')
    output, _ = process.communicate('bye\n', timeout=10)
    record.append(output)
    assert process.returncode == 0
    assert read_saved() == final
    separator = '    ' + '_' * 60 + '\n'
    farewell = separator + ' Bye. Hope to see you again soon!\n   ' + '_' * 60 + '\n'

    def response(*lines):
        return separator + '\n'.join(lines) + '\n' + separator + '\n'

    def restart(commands, expected):
        record.append('\nRestart input:\n' + commands + 'Actual output:\n')
        result = subprocess.run(
            ['java', '-cp', str(classes), 'mybff.MyBff'], cwd=session,
            input=commands, capture_output=True, text=True, timeout=10,
        )
        record.append(result.stdout + result.stderr)
        marker = '     What can I do for you?\n' + separator + '\n'
        actual = result.stdout.split(marker, 1)[-1]
        assert result.returncode == 0 and actual == expected, (
            f'Expected:\n{expected}\nActual:\n{actual}\n{result.stderr}'
        )

    restored = response('     Here are the tasks in your list:',
                        '     1.[T][ ] read book', '     2.[D][ ] homework (by: Friday)',
                        '     3.[E][ ] lunch (from: noon to: evening)')
    restart('list\nmark 2\ntodo next\nbye\n', restored
            + response("     Nice! I've marked this task as done:", '       [X] homework')
            + response("     Got it. I've added this task:", '       [T][ ] next',
                       '     Now you have 4 tasks in the list.') + farewell)
    assert read_saved() == [
        final[0], 'D | 1 | homework | Friday', final[2], 'T | 0 | next',
    ]
    restart('list\nbye\n', response('     Here are the tasks in your list:',
            '     1.[T][ ] read book', '     2.[D][X] homework (by: Friday)',
            '     3.[E][ ] lunch (from: noon to: evening)', '     4.[T][ ] next') + farewell)
    saved.write_text('T | 1 | done\nD | 0 | homework | \nE | 1 | lunch |  | \n', encoding='utf-8')
    restart('list\nbye\n', response('     Here are the tasks in your list:',
            '     1.[T][X] done', '     2.[D][ ] homework (by: )',
            '     3.[E][X] lunch (from:  to: )') + farewell)
    saved.write_text('MyBff storage v2\nT | 1 | cmVhZCBib29r\n', encoding='utf-8')
    restart('list\ntodo next\nbye\n', response('     Here are the tasks in your list:',
            '     1.[T][X] read book') + response("     Got it. I've added this task:",
            '       [T][ ] next', '     Now you have 2 tasks in the list.') + farewell)
    assert saved.read_text(encoding='utf-8').splitlines() == [
        'MyBff storage v3', 'T | 1 | read book', 'T | 0 | next']
    saved.write_text('', encoding='utf-8')
    restart('list\nbye\n', response('     Here are the tasks in your list:') + farewell)
    assert saved.read_text(encoding='utf-8') == ''
    record.append('```\n\nPASS: All saved snapshots and restart responses match.\n')
    print('PASS: Automatic file saving after each change')
    print('PASS: Loading, editing and saving across restarts; completion flags; empty dates and file')
finally:
    if process.poll() is None:
        process.kill()
        process.wait()
    (root / 'test/storage-test-sessions.md').write_text(''.join(record), encoding='utf-8')
