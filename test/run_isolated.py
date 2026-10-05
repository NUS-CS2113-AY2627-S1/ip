"""Runs one Goat Bot UI test in a temporary working directory."""

import subprocess
import sys
import tempfile


def main():
    """Runs the supplied command with isolated persistent storage."""
    with tempfile.TemporaryDirectory() as working_directory:
        result = subprocess.run(sys.argv[1:], cwd=working_directory, check=False)
    return result.returncode


if __name__ == "__main__":
    raise SystemExit(main())
