# UI Test Plan Format

Use one level-two heading per test case. Each test case contains an aim, an
input block, and an expected-output block. Input is sent to the program's
standard input exactly as written; expected output is compared with standard
output exactly, apart from platform line-ending differences.

```markdown
## Test 1: Short descriptive name

Aim: Explain the behavior this case verifies.

### Input

```text
first command
second command
```

### Expected output

```text
Output printed by the program.
```
```

The test runner emits a transcript with separate input and output sections for
each completed case. It stops immediately after the first failing case and
prints that case's expected and actual output.
