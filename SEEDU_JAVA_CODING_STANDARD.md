# SE-Edu Java Coding Standard (Intermediate)

Project-specific skill for mandating Java coding conventions based on https://se-education.org/guides/conventions/java/intermediate.html.

## Quick Summary

### Naming Conventions

**Packages**: All lowercase
```java
com.company.application.ui
```

**Classes/Enums**: PascalCase nouns
```java
Line, AudioSystem, Task
```

**Variables**: camelCase
```java
line, audioSystem, taskName
```

**Constants**: SCREAMING_SNAKE_CASE
```java
MAX_ITERATIONS, COLOR_RED, DEFAULT_TIMEOUT
```

**Methods**: camelCase verbs
```java
getName(), computeTotalWidth(), findTask()
```

**Boolean variables/methods**: Use meaningful prefixes
```java
isSet, isVisible, isFinished, isFound, isOpen, hasData, wasOpen
boolean hasLicense();
boolean canEvaluate();
void setFound(boolean isFound);
```

**Test methods**: Three-part format
```java
featureUnderTest_testScenario_expectedBehavior()
// Examples:
sortList_emptyList_exceptionThrown()
getMember_memberNotFound_nullReturned()
```

**Abbreviations/Acronyms**: Use camelCase, not ALL_CAPS
```java
✓ Good:  exportHtmlSource(), openDvdPlayer()
✗ Bad:   exportHTMLSource(), openDVDPlayer()
```

### Key Rules

- All names must be in English
- Variables with large scope should have long names; small scope can be short (e.g., loop counters i, j, k)
- Provide clear, descriptive variable and method names
- Use meaningful comments for non-obvious code

### Layout and Formatting

- 4-space indentation (standard Java)
- Maximum line length: 120 characters
- Opening braces on same line (Java style)
- One statement per line

### Comments

- Use Javadoc for all public classes and methods
- Provide block comments for complex logic
- Inline comments should explain WHY, not WHAT (code should be self-explanatory)

### Imports

- Import specific classes, not wildcard imports (except for static imports in limited cases)
- Organize imports in logical order
- Remove unused imports

## Application to This Project

All Java code in this project must adhere to these conventions. When contributing code, ensure:

1. Naming follows the conventions above
2. Code is properly formatted and indented
3. Public APIs have Javadoc comments
4. No code smells or obvious violations

Refer to the complete guide: https://se-education.org/guides/conventions/java/intermediate.html
