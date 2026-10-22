from pathlib import Path
import subprocess
import tempfile

root = Path(__file__).resolve().parents[1]
jar = root / 'build/libs/mybff.jar'
folder = Path(tempfile.mkdtemp(prefix='delete-', dir=root / 'build'))
record = []

def run(commands):
    result = subprocess.run(['java', '-jar', str(jar)], cwd=folder, input=commands,
                            capture_output=True, text=True, timeout=60)
    record.append(f'Input:\n{commands}\nOutput:\n{result.stdout}{result.stderr}')
    assert result.returncode == 0, result.stderr
    return result.stdout

try:
    run('todo first\ntodo second\ntodo third\ndelete 2\nbye\n')
    saved = folder / 'data/mybff.txt'
    assert saved.read_text().splitlines() == ['MyBff storage v3', 'T | 0 | first', 'T | 0 | third']
    output = run('list\ndelete 2\ndelete 1\nbye\n')
    assert '1.[T][ ] first' in output and '2.[T][ ] third' in output
    output = run('list\ndelete 1\nbye\n')
    assert 'Invalid task number.' in output and '1.[T]' not in output
    assert saved.read_text().splitlines() == ['MyBff storage v3']
    saved.write_text('T | 0 | first\nT | 0 | second\nT | 0 | third\n')
    process = subprocess.Popen(['java', '-jar', str(jar)], cwd=folder, stdin=subprocess.PIPE,
                               stdout=subprocess.PIPE, stderr=subprocess.STDOUT, text=True)
    try:
        while 'What can I do for you?' not in process.stdout.readline():
            assert process.poll() is None
        saved.rename(folder / 'data/original.txt')
        saved.mkdir()
        (saved / 'obstruction').write_text('keep')
        commands = 'delete 2\nlist\nbye\n'
        output, _ = process.communicate(commands, timeout=60)
        record.append(f'Blocked save input:\n{commands}\nOutput:\n{output}')
        assert 'Could not save data/mybff.txt.' in output
        assert "I've removed" not in output
        assert all(f'{i}.[T][ ] {name}' in output for i, name in enumerate(['first', 'second', 'third'], 1))
        assert not list((folder / 'data').glob('mybff-*.tmp'))
    finally:
        if process.poll() is None:
            process.kill()
            process.wait()
    print('PASS: JAR deletion persists, empty list persists, invalid index rejected, failed save restores order')
finally:
    (root / 'build/delete-jar-sessions.txt').write_text('\n'.join(record))
