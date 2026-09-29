# iP status review — 29 September 2026

Checked the supplied dashboard warnings against local source, commit history,
live GitHub refs, public pull requests and releases, and the published guide URL.
Level 8 is intentionally excluded at the owner's request.

## Corrections published to GitHub

| Dashboard item | Evidence and correction |
| --- | --- |
| Level-0 | Added lightweight tag `Level-0` at `b453279`, which implements the greeting and exit. |
| A-Exceptions | Added lightweight tag `A-Exceptions` at `301cb8c`, the merge containing the custom exception and input validation. |
| A-Collections | Added lightweight tag `A-Collections` at `d9328a9`, the merge containing the growing `ArrayList<Task>` and deletion support. |
| branch-Level-5 | Published branch at `5d44dba`; already merged into master by `301cb8c`. |
| branch-Level-6 | Published branch at `5dc28f7`; already merged into master by `d9328a9`. |
| branch-Level-7 | Published branch at `c2b1735`; already merged into master by `6a55818`. |
| branch-Level-9 | Created the missing local name at the existing Level 9 commit `71e4450`. Its tip is already an ancestor of master. |
| branch-A-JavaDoc | Restored and published the branch name at the existing Javadoc commit `76258a1`. |

The two Week 7 branch names were reconstructed after the work was committed on
master. This restores the refs without rewriting history; it does not create a
historical branch-development or merge event. `git branch --merged master`
lists all five required branches. The course checker may impose additional
workflow requirements; its refresh has not been observed.

No existing tags were moved. The guide commit and `A-UserGuide` tag were
created and published as part of the ongoing documentation work.
The three restored tags and all five required branches were pushed successfully
on 29 September 2026.

## Week 7 checks before PR and release publication

| Item | Result |
| --- | --- |
| A-MoreOOP | Tag `9074dd6` is already on GitHub. `Ui`, `Parser`, `TaskList`, and `Storage` have separate responsibilities. |
| Level-9 | Tag `71e4450` is already on GitHub. Keyword search is implemented and its recorded UI cases pass. |
| A-JavaDoc | Tag `76258a1` is already on GitHub. Java 25 Javadoc generation passes. |
| A-UserGuide | Tag `18040a3` is on GitHub and `master` contains the complete guide. |
| Published UG | `https://zephtzk.github.io/ip/` returns HTTP 200 and contains the `Chillguy User Guide` heading. |
| Merging PRs | The public GitHub API returns no pull requests in `zephtzk/ip`. A real pull request and merge are still needed. Local merge commits do not satisfy this item. |
| JAR release | The only public release is `A-Jar`, published 15 September 2026 at 19:49:43 Singapore time. It satisfies the earlier release window, but not the Week 7 window shown in the screenshot. |

The Week 7 window shown is 19 September 2026 at 16:00:01 through 7 October 2026
at 16:00:00, Singapore time. A newly built `build/libs/chillguy-all.jar` is ready
for the new release. Keep the existing `A-Jar` release intact.

## Validation

- Java and javac: 25.0.4.
- All 41 recorded UI cases pass, including direct JAR launch, errors, more than
  100 tasks, persistence, failed-save rollback, and keyword search.
- The existing runner built the fat JAR and compared exact output, empty stderr,
  exit codes, and recorded storage expectations. Personal task data was isolated.
- Full command/input/output record: [Week 7 UI transcript](week7-ui-test-transcript.md).
- Javadoc passes with `javadoc -quiet -private -Xdoclint:all,-missing`.
  Optional missing Javadoc tags are excluded from that check.
- Documentation whitespace checks pass. The test transcript deliberately retains
  trailing spaces in inputs that test whitespace handling. No Java behavior or
  UI expectations needed changes.
- Existing guide edits and previous verification transcripts were preserved.

## Publication follow-through

GitHub Pages is configured for `master` and `/docs`; its build status is
`built`. No Pages setting change is needed.

This audit and its test transcript were merged through
[PR #1](https://github.com/zephtzk/ip/pull/1) at 14:19:30 Singapore time on
29 September 2026, producing merge commit `77dbba5`.

The [Week 7 release](https://github.com/zephtzk/ip/releases/tag/A-UserGuide)
was published at 14:19:47 Singapore time on 29 September 2026, inside the
required window. It uses the existing `A-UserGuide` tag, whose application
sources match the tested JAR. The earlier `A-Jar` release is retained.

The release artifact is `build/libs/chillguy-all.jar` (20,887 bytes).
Its SHA-256 is
`03531a30771abae09a3dfff3bbd84606646932ddac4da82026527f13a89e9e9f`.
The public download matches that checksum, and the published user guide
returns HTTP 200. GitHub confirms the PR is merged and the release is public.
The dashboard may update later.
