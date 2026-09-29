# User guide review record

Date: 2026-09-29. Status: approved by the owner for commit and publication.
The owner also requested the lightweight tag `A-UserGuide`.

## What changed

- Replaced the starter content in [the user guide](../docs/README.md) with Java 25 setup,
  all nine commands, examples and expected responses, storage guidance, and troubleshooting.
- Added a guide link and a product introduction to the root README.
- Checked behavior against the current source, including Level 9 search, Level 7 persistence,
  Level 6 deletion, JAR packaging, and the A-MoreOOP and A-JavaDoc increments.
  The latest commit observed during verification was `76258a1`.
- Used [GitHub's Markdown guidance](https://docs.github.com/en/get-started/writing-on-github/getting-started-with-writing-and-formatting-on-github/basic-writing-and-formatting-syntax)
  for formatting and the [AB3 Features section](https://se-education.org/addressbook-level3/UserGuide.html#features)
  as a structural benchmark. All documented command behavior comes from Chillguy.

## Verification

- **PASS: all 41 recorded regression cases**, using Java and javac 25.0.4 and the existing
  `test/run-ui-tests.py` runner. See the [full regression transcript](user-guide-regression-transcript.md).
  It includes inputs, actual output, exit codes, stderr, persistence checks, and failed-save recovery.
- **PASS: all nine guide feature examples**, compared with the guide's expected response excerpts.
  A freshly built JAR was copied into an empty disposable folder and launched with
  `java -jar chillguy-all.jar`; saved contents and a second launch were also checked.
  See the [example transcript](user-guide-example-transcript.md).
- **PASS: local HTML preview**, inspected in the browser. Headings, code blocks, lists, and tables
  render correctly. All internal section links resolve to existing headings.
- No application behavior or recorded test expectations changed. The existing UI test plan remains applicable.
  Test data was isolated from personal saved tasks.

The first build attempts stopped before any UI cases because the default Gradle cache was unwritable.
The successful run used `GRADLE_USER_HOME=C:\Users\zepht\.gradle` with access to that existing cache.
The runner's transcript destination was set to `test/user-guide-regression-transcript.md`
to preserve the previous regression record. Its cases and assertions were unchanged.

## Publication checklist recorded before deployment

1. Owner review: completed; the guide was approved without further wording changes.
2. Commit and push authorization: received, including the `A-UserGuide` tag.
3. Commit and push the approved documentation and its lightweight tag.
4. In the repository's **Settings > Pages**, verify that publishing uses **Deploy from a branch**,
   the **master** branch, and the **/docs** folder; save if a change is needed.
5. Once deployment completes, check [the public guide](https://zephtzk.github.io/ip/) for the updated
   Chillguy title, all command sections, working contents links, readable examples, and tables.

At the time of this pre-deployment record, the public Pages URL had not been verified.
The final publication result will be reported after checking the deployed page.
The local preview does not replace that final check.
