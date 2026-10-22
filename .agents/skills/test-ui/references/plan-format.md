# UI test plan format

Store the complete test plan in `test/ui-test-plan.md`. The runner expects:

```markdown
## Test setup

### Build command
```text
<command that builds the program>
```

### Program command
```text
<command that launches the program>
```

## Test case: Descriptive name

### Aim

State what behavior this test checks.

### Inputs
```text
<complete standard-input sequence>
```

### Expected output
```text
<complete expected console output>
```

## Latest test session
```

Use one `## Test case:` section per case. The runner executes the build command once and the program command once for each case. It replaces everything below `## Latest test session` with the latest transcript.

Expected output is compared exactly after only line-ending normalization. Include all spaces, blank lines, prompts, and final newlines that the program prints.
