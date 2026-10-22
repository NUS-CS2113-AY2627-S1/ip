"""Check temporary and persistent Windows file locks using the real console app."""
import ctypes
from ctypes import wintypes
import os
from pathlib import Path
import subprocess
import tempfile
import time

root = Path(__file__).resolve().parents[1]
classes = root / 'build/ui-test'
record = ['# File-lock test sessions\n']
separator = '    ' + '_' * 60 + '\n'
farewell = separator + ' Bye. Hope to see you again soon!\n   ' + '_' * 60 + '\n'


def response(*lines):
    return separator + '\n'.join(lines) + '\n' + separator + '\n'


if os.name != 'nt':
    print('SKIP: Windows file-lock checks require Windows')
    raise SystemExit(0)

kernel = ctypes.WinDLL('kernel32', use_last_error=True)
kernel.CreateFileW.argtypes = [wintypes.LPCWSTR, wintypes.DWORD, wintypes.DWORD,
                              ctypes.c_void_p, wintypes.DWORD, wintypes.DWORD, wintypes.HANDLE]
kernel.CreateFileW.restype = wintypes.HANDLE
kernel.CloseHandle.argtypes = [wintypes.HANDLE]
kernel.CloseHandle.restype = wintypes.BOOL

try:
    for temporary_lock in (True, False):
        name = 'Temporary lock' if temporary_lock else 'Persistent lock'
        folder = Path(tempfile.mkdtemp(prefix='lock-', dir=classes))
        target = folder / 'data/mybff.txt'
        target.parent.mkdir()
        original = b'MyBff storage v3\nT | 0 | original\n'
        target.write_bytes(original)
        process = subprocess.Popen(['java', '-cp', str(classes), 'mybff.MyBff'], cwd=folder,
                                   stdin=subprocess.PIPE, stdout=subprocess.PIPE,
                                   stderr=subprocess.PIPE, text=True)
        handle = None
        output = ''
        commands = 'todo next\nlist\nbye\n'
        record.append(f'\n## {name}\n\nInput:\n```text\n{commands}```\n')
        try:
            # Loading is complete before the greeting; retain its full output.
            while 'What can I do for you?' not in output:
                line = process.stdout.readline()
                assert line, 'Application exited before greeting'
                output += line
            output += process.stdout.readline() + process.stdout.readline()
            # Permit reading/writing, but deny deletion/renaming while this handle is open.
            handle = kernel.CreateFileW(str(target), 0x80000000, 3, None, 3, 0, None)
            assert handle != ctypes.c_void_p(-1).value, ctypes.get_last_error()
            process.stdin.write('todo next\n')
            process.stdin.flush()
            if temporary_lock:
                # The opening separator confirms that Java has received the command.
                line = process.stdout.readline()
                output += line
                assert line == separator, repr(line)
                time.sleep(0.15)
                assert target.read_bytes() == original, 'Locked file was replaced'
                assert kernel.CloseHandle(handle)
                handle = None
            rest, errors = process.communicate('list\nbye\n', timeout=10)
            output += rest
            assert process.returncode == 0 and not errors, errors
            marker = '     What can I do for you?\n' + separator + '\n'
            actual = output.split(marker, 1)[1]
            if temporary_lock:
                expected = response("     Got it. I've added this task:", '       [T][ ] next',
                                    '     Now you have 2 tasks in the list.')
                expected += response('     Here are the tasks in your list:',
                                     '     1.[T][ ] original', '     2.[T][ ] next') + farewell
                assert target.read_text().splitlines() == [
                    'MyBff storage v3', 'T | 0 | original', 'T | 0 | next']
            else:
                expected = response('     OOPS!!! Could not save data/mybff.txt. '
                                    'No changes were made. Check the file and try again.')
                expected += response('     Here are the tasks in your list:',
                                     '     1.[T][ ] original') + farewell
                assert target.read_bytes() == original
            assert actual == expected, f'Expected:\n{expected}\nActual:\n{actual}'
            assert not list(target.parent.glob('mybff-*.tmp'))
            record.append('PASS\n')
            print(f'PASS: {name}')
        finally:
            record.append(f'\nActual output:\n```text\n{output}```\n')
            if handle is not None and handle != ctypes.c_void_p(-1).value:
                kernel.CloseHandle(handle)
            if process.poll() is None:
                process.kill()
                process.wait()
finally:
    (root / 'test/storage-retry-sessions.md').write_text(''.join(record), encoding='utf-8')
