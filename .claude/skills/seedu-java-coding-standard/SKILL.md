---
name: seedu-java-coding-standard
description: The se-education Java coding standard that all Java code in this project must follow, marking which rules CS2113 actually mandates and which this project has adopted voluntarily. Use when writing, reviewing, refactoring, or reformatting any Java code, when checking a file for style compliance, or when asked about naming, layout, braces, imports, or Javadoc conventions in this project.
---

# seedu Java Coding Standard

## What the course actually requires

The authority is the module's own page, not the standard itself:
https://nus-cs2113-ay2627-s1.github.io/website/admin/standardsAndConventions.html

It mandates exactly two things:

1. **The _basic_ rules** of the SE-EDU Java coding standard —
   https://se-education.org/guides/conventions/java/basic.html
2. **The Git commit message _subject_ conventions** — see the `seedu-git-standard` skill.

Everything else is optional: the intermediate and advanced Java rules, the Markdown style guide, and
the Google developer documentation style guide.

**This project has nevertheless adopted the intermediate rules.** They are marked *(intermediate)*
below. Treat them as binding here — the code already follows them and consistency is worth more than
the licence to stop — but know that they are this repository's choice, not a course requirement, if
a deadline ever forces a judgement about what must be fixed and what can wait.

Note that the intermediate page is cumulative: it presents "basic + intermediate" together, so a rule
appearing there is not evidence that it is intermediate. The split below comes from reading the basic
page on its own. Where it was unclear, the rule is marked intermediate, which is the cautious way
round: it stays binding in this project either way.

For anything neither page covers, fall back to the Google Java Style Guide.

## Naming

| Element | Rule | Good | Bad |
|---|---|---|---|
| Package | all lower case | `todobuddy.ui` | `todoBuddy.UI` |
| Class / Enum | noun, PascalCase | `Line`, `AudioSystem` | `line`, `audio_system` |
| Variable | camelCase | `line`, `audioSystem` | `Line`, `audio_system` |
| Constant | ALL_CAPS with underscores | `MAX_ITERATIONS` | `maxIterations` |
| Method | verb, camelCase | `getName()`, `computeTotalWidth()` | `name()`, `GetName()` |

- **All names in English.**
- **Scope governs length**: large scope gets a long name; small scope may be short. Scratch variables
  (`i`, `j`, `k`, `m`, `n`, `c`, `d`) are acceptable for temporary use only.
- **Booleans** read as predicates — prefix with `is`, `has`, `was`: `isSet`, `isVisible`, `hasData`.
- **Collections take the plural**: `Collection<Point> points`.
- **Test methods** use three parts: `featureUnderTest_testScenario_expectedBehavior()`.
- *(intermediate)* **Abbreviations are not uppercased** inside names: `exportHtmlSource()`, not
  `exportHTMLSource()`.
- *(intermediate)* **Setter parameter form** for booleans: `void setFound(boolean isFound)`.
- *(intermediate)* **Related constants share a prefix**: `COLOR_RED`, `COLOR_GREEN`, `COLOR_BLUE`.

## Layout

- **Indentation is 4 spaces, never tabs.**
- **Line length**: soft limit 110 characters, hard limit 120.
- **Wrapped lines indent 8 spaces** (double the normal indent).
- **K&R ("Egyptian") braces** — the opening brace stays on the line that opens the block:

  ```java
  public void someMethod() throws SomeException {
      ...
  }

  if (condition) {
      statements;
  } else {
      statements;
  }

  try {
      statements;
  } catch (Exception exception) {
      statements;
  } finally {
      statements;
  }
  ```

- *(intermediate)* **Break lines** after commas, and *before* operators and operator-like symbols
  (`.`, `&`, `|`). Keep a method or constructor name attached to its opening parenthesis. Prefer
  breaking at a higher syntactic level over a lower one.
- *(intermediate)* **Ternary operators** go either entirely on one line or across three lines.
- *(intermediate)* **Whitespace**: surround operators with spaces; follow reserved words with a
  space; follow commas with a space; surround `:` with spaces when binary or ternary; follow the `;`
  in a `for` header with a space.
- *(intermediate)* **Blank lines** separate logical units within a block — one blank line, not
  several.
- *(intermediate)* **Intentional switch fallthrough** must carry a `// Fallthrough` comment.

## Statements — all intermediate

Every rule in this section is intermediate, and so optional for the course. This project follows all
of them.

- **Put every class in a package.** (This repository is still in the default package; that is a
  deliberate, temporary state until the packaging increment. Do not introduce packages without
  asking.)
- **Import order must be consistent**: static imports, then `java.*`, `javax.*`, `org.*`, `com.*`,
  `javafx.*`.
- **List imports explicitly** — never use wildcard imports.
- **Array brackets attach to the type**: `int[] a`, not `int a[]`.
- **Initialize variables where they are declared**, in the smallest possible scope.
- **Class variables are never `public`**, unless the class is a pure data class with no behaviour.
- **Always brace loop and conditional bodies**, even a single statement on one line.
- **Put the conditional on its own line.**

## Comments and Javadoc

These are basic rules, so they are mandatory. They are also the ones this project has most often got
wrong, because they are easy to write past.

- **All comments in English, using American spelling.** `unrecognized`, not `unrecognised`;
  `capitalization`, not `capitalisation`. This applies to **comments only** — the bot's user-facing
  strings are its own voice and stay as AGENTS.md has them, which includes British spellings such as
  "Tonight has been an honour" and "not a number I recognise". Do not "correct" those.
- **A method's summary is written in the third person**: "Returns the task…", not "Return the
  task…". Likewise `Constructs`, `Adds`, `Shows`, `Prints`.
- **Punctuation behind each parameter description.** Every `@param`, `@return` and `@throws`
  description ends with a full stop. This is a stated basic rule, not a preference.
- **Javadoc format:**

  ```java
  /**
   * First sentence is the short summary.
   *
   * @param name Description, ending with punctuation.
   * @return Description.
   * @throws ExceptionType Description.
   */
  ```

  - `/**` sits on its own line; a space follows each `*`.
  - One empty line between the description and the block tags.
  - No blank line between the Javadoc block and the code it documents.
  - Omit `@return` for `void` methods, and where the returned value is obvious.
  - `@param` is all-or-none: document every parameter or none of them.
  - A single-line Javadoc is acceptable for member variables.
- **Indent comments to match the code** they describe.

### Where Javadoc is required

The basic standard says how to *format* Javadoc but does not say where it must *appear*. Two other
rules do, and both apply here:

- AGENTS.md requires a header comment on every class, and on any method or field whose purpose is
  not obvious.
- The course's `A-JavaDoc` increment asks for header comments on all non-private classes and
  methods, and on non-trivial private methods. This repository already meets that in full.

A header comment may be omitted for a getter or setter, for an overridden method where the parent's
Javadoc applies exactly (keep the `@Override`), and for test classes and methods.

## How to apply this in this project

When writing new Java code, follow the rules above as you write — do not write first and reformat
after. When asked to bring existing code into compliance:

1. Check the whole file against this checklist, in this order: naming → layout → statements →
   comments.
2. Make only style changes. Never mix a behaviour change into a compliance pass; if you spot a bug,
   report it separately rather than fixing it in the same commit.
3. Verify the code still compiles: `javac -d bin src/main/java/*.java`.
4. Run the `test-ui` skill. A pure style pass must leave every test case passing and the test plan
   untouched — that is the evidence nothing but formatting moved.
5. Present the result with the `present-changes-visually` skill so the user can review it.

Remember that this project also mandates a consistent butler voice for all user-facing strings — see
AGENTS.md. A style pass must not flatten that voice into generic wording, and must not Americanize
it.
