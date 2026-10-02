---
name: seedu-java-coding-standard
description: SE-EDU Java intermediate coding standard for this project
---

# SE-EDU Java Coding Standard (Intermediate)

Follow these rules for all Java code in this project.

## Naming Conventions

- **Packages**: All lowercase (e.g., `com.company.application.ui`)
- **Classes/Enums**: Nouns in PascalCase (e.g., `Task`, `Deadline`, `Event`)
- **Variables**: camelCase (e.g., `taskList`, `isDone`)
- **Constants**: SCREAMING_SNAKE_CASE (e.g., `MAX_TASKS`, `DEFAULT_SEPARATOR`)
- **Methods**: Verbs in camelCase (e.g., `getName()`, `setDone()`, `toString()`)
- **Booleans**: Prefix with `is`, `has`, `was`, `can`, `should` (e.g., `isDone`, `hasData`)
- **Collections**: Plural form (e.g., `tasks`, `events`)
- **Abbreviations**: Use lowercase when part of names (e.g., `exportHtmlFile()` not `exportHTMLFile()`)
- **Language**: English only
- **Scope rule**: Longer names for large scope, short names (i, j, k) acceptable for temporary variables

## Layout Standards

- **Indentation**: 4 spaces (never tabs)
- **Line length**: Soft limit 110 chars, hard limit 120 chars
- **Brackets**: K&R/Egyptian style (opening brace on same line, e.g., `if (...) {`)
- **Whitespace**: Spaces around operators and after commas
- **Blank lines**: Separate logical units within classes

## Coding Standards

- **Imports**: No wildcard imports; order: static, java.*, javax.*, org.*, com.*, project packages
- **Package**: Every class must belong to a package
- **If-else**: Always use braces, even for single statements
- **Loops**: Always wrap body in braces
- **Switch**: Include `// Fallthrough` comment when intentionally omitting break
- **Variables**: Initialize at declaration; declare in smallest scope possible
- **Arrays**: Type on left (e.g., `int[] a` not `int a[]`)
- **Access**: Class variables never public unless data class with no behavior

## Comment Standards

- **Language**: English with American spelling
- **Classes**: Require Javadoc comments (/** ... */)
- **Public methods**: Require Javadoc (except getters/setters, overrides)
- **Javadoc format**: 
  - Opening `/**` on separate line
  - First sentence starts with verb (Returns, Sends, Adds)
  - Empty line before `@param` tags
  - Include `@param` and `@return` for all parameters/returns or none
- **Non-trivial methods**: Add explanatory comments for WHY, not WHAT

## Apply This Standard

When reviewing or modifying Java code:
1. Check naming conventions first (most common issues)
2. Verify Javadoc on classes and public methods
3. Ensure proper indentation and spacing
4. Check that collections are plural-named
5. Verify no wildcard imports
