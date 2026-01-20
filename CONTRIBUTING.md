# Contributing to ClickNCheck

Thank you for your interest in contributing to the ClickNCheck Mobile Automation Framework! This document provides guidelines and instructions for contributing to the project.

## Table of Contents

1. [Code of Conduct](#code-of-conduct)
2. [Getting Started](#getting-started)
3. [Development Setup](#development-setup)
4. [Code Style Guidelines](#code-style-guidelines)
5. [Testing Guidelines](#testing-guidelines)
6. [Pull Request Process](#pull-request-process)
7. [Commit Message Guidelines](#commit-message-guidelines)
8. [Reporting Bugs](#reporting-bugs)
9. [Suggesting Enhancements](#suggesting-enhancements)

---

## Code of Conduct

This project adheres to a code of professional conduct. By participating, you are expected to:
- Be respectful and constructive in all interactions
- Focus on what is best for the community and the project
- Show empathy towards other community members

---

## Getting Started

### Prerequisites

- **Java 11** or higher
- **Maven 3.6+**
- **Appium Server** (installed globally or in PATH)
- **Android SDK** (for Android testing)
- **Xcode** (for iOS testing on macOS)
- **Git**

### Recommended IDE Setup

- **IntelliJ IDEA** (recommended) or **Eclipse**
- Plugins:
  - Lombok (if used in future)
  - SonarLint (for code quality)
  - CheckStyle

---

## Development Setup

### 1. Fork and Clone

```bash
# Fork the repository on GitHub
git clone https://github.com/YOUR_USERNAME/ClickNCheck.git
cd ClickNCheck
```

### 2. Build the Project

```bash
# Clean and compile
mvn clean compile

# Run tests
mvn test

# Package
mvn package
```

### 3. Configure Environment

```bash
# Set Appium path (if not in PATH)
export APPIUM_PATH=/path/to/appium

# Set Android SDK (if needed)
export ANDROID_HOME=/path/to/android-sdk
```

### 4. Run Example Tests

```bash
# Run with default configuration
mvn test

# Run with custom wait timeouts
mvn test -Ddefault.wait=40 -Dshort.wait=15
```

---

## Code Style Guidelines

### Java Coding Standards

#### General Rules

- **Java Version**: Use Java 11 features
- **Indentation**: 4 spaces (no tabs)
- **Line Length**: Maximum 120 characters
- **File Encoding**: UTF-8

#### Naming Conventions

```java
// Classes: PascalCase
public class UiElement { }

// Methods: camelCase
public void clickWithWait() { }

// Constants: UPPER_SNAKE_CASE
public static final int DEFAULT_WAIT = 30;

// Variables: camelCase
private String elementName;

// Packages: lowercase
package com.exit3.testing;
```

#### Documentation

- **All public classes** must have JavaDoc
- **All public methods** must have JavaDoc
- Include `@param`, `@return`, `@throws` tags
- Provide usage examples for complex APIs

**Example:**
```java
/**
 * Clicks the element after waiting for it to be clickable.
 * <p>
 * This method uses {@link TestConfig#DEFAULT_WAIT} timeout.
 * </p>
 *
 * @return this UiObject for method chaining
 * @throws NoSuchElementException if element not found within timeout
 * @throws IOException if screenshot fails on error
 *
 * @see #tryClickWithWait(Integer)
 */
public UiObject clickWithWait() throws IOException {
    // implementation
}
```

#### Error Handling

- **Catch specific exceptions** (not generic Exception)
- **Include context** in error messages
- **Use SLF4J** for logging, not System.out
- **Validate inputs** early (fail fast)

**Example:**
```java
// Good
try {
    element.click();
} catch (NoSuchElementException e) {
    logger.error("Element not found: {}", elementName, e);
    throw new RuntimeException("Failed to click element: " + elementName, e);
}

// Bad
try {
    element.click();
} catch (Exception e) {
    e.printStackTrace();
}
```

#### Resource Management

- **Always close resources** (use try-with-resources or finally)
- **Clean up ThreadLocal** to prevent memory leaks
- **Stop Appium server** in @AfterClass

**Example:**
```java
// Good
try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
    String line = reader.readLine();
} catch (IOException e) {
    logger.error("Failed to read stream", e);
}

// ThreadLocal cleanup
@AfterMethod
public void cleanup() {
    TestLogger.cleanup();
    TestLogger.LogEntry.cleanupStepCounter();
}
```

---

## Testing Guidelines

### Unit Tests

- Place unit tests in `src/test/java`
- Use **TestNG** for framework tests
- Aim for **80%+ code coverage** for new code

**Example:**
```java
public class TestConfigTest {
    @Test
    public void testDefaultWaitValue() {
        assertTrue(TestConfig.DEFAULT_WAIT > 0);
        assertTrue(TestConfig.DEFAULT_WAIT <= 60);
    }
}
```

### Integration Tests

- Test end-to-end functionality with real drivers
- Use demo apps for consistent testing
- Clean up resources after tests

### Test Naming

```java
@Test
public void clickWithWait_whenElementExists_shouldClickSuccessfully() {
    // Given
    UiObject button = createTestButton();

    // When
    button.clickWithWait();

    // Then
    verify(button was clicked);
}
```

---

## Pull Request Process

### Before Submitting

1. **Update documentation** if you changed public APIs
2. **Add/update tests** for new functionality
3. **Run all tests** locally: `mvn clean test`
4. **Update CHANGELOG.md** with your changes
5. **Ensure code follows style guidelines**

### PR Title Format

```
[Type] Brief description

Examples:
[Feature] Add retry mechanism for flaky tests
[Fix] Correct screenshot timestamp format
[Docs] Update README with new configuration options
[Refactor] Extract platform-specific logic to separate classes
```

### PR Description Template

```markdown
## Description
Brief description of changes

## Type of Change
- [ ] Bug fix (non-breaking change which fixes an issue)
- [ ] New feature (non-breaking change which adds functionality)
- [ ] Breaking change (fix or feature that would cause existing functionality to not work as expected)
- [ ] Documentation update

## How Has This Been Tested?
Describe the tests you ran

## Checklist
- [ ] My code follows the style guidelines of this project
- [ ] I have performed a self-review of my own code
- [ ] I have commented my code, particularly in hard-to-understand areas
- [ ] I have made corresponding changes to the documentation
- [ ] My changes generate no new warnings
- [ ] I have added tests that prove my fix is effective or that my feature works
- [ ] New and existing unit tests pass locally with my changes
- [ ] I have updated CHANGELOG.md

## Related Issues
Closes #123
```

### Review Process

1. At least **one reviewer** must approve
2. All **CI checks must pass**
3. **No merge conflicts**
4. **All review comments addressed**

---

## Commit Message Guidelines

### Format

```
<type>(<scope>): <subject>

<body>

<footer>
```

### Types

- **feat**: New feature
- **fix**: Bug fix
- **docs**: Documentation changes
- **style**: Code style changes (formatting, no logic change)
- **refactor**: Code refactoring (no feature change)
- **test**: Adding or updating tests
- **chore**: Maintenance tasks

### Examples

```
feat(ui-object): add retry mechanism for flaky operations

Implement withRetry() helper method that wraps operations with
configurable retry logic. Includes exponential backoff.

Closes #45

---

fix(appium-manager): prevent resource leak in emulator boot wait

BufferedReader and Process now properly closed in finally block.
Added timeout to prevent infinite loops.

Fixes #67

---

docs(readme): update configuration examples

Add examples for new TestConfig system properties and
screenshot directory configuration.
```

---

## Reporting Bugs

### Before Reporting

1. **Check existing issues** - someone may have already reported it
2. **Try latest version** - bug may already be fixed
3. **Gather information** - logs, screenshots, environment details

### Bug Report Template

```markdown
## Bug Description
Clear and concise description of what the bug is.

## Steps to Reproduce
1. Go to '...'
2. Click on '....'
3. Scroll down to '....'
4. See error

## Expected Behavior
What you expected to happen.

## Actual Behavior
What actually happened.

## Environment
- OS: [e.g., macOS 13, Windows 11, Ubuntu 22.04]
- Java Version: [e.g., 11.0.18]
- Maven Version: [e.g., 3.8.6]
- Appium Version: [e.g., 2.0.0]
- Framework Version: [e.g., 1.1.0]

## Logs/Screenshots
Attach relevant logs or screenshots.

## Additional Context
Any other context about the problem.
```

---

## Suggesting Enhancements

### Enhancement Request Template

```markdown
## Feature Description
Clear and concise description of the feature.

## Use Case
Describe the problem this feature would solve.

## Proposed Solution
How you think this should be implemented.

## Alternatives Considered
Other approaches you've thought about.

## Additional Context
Any other relevant information.
```

---

## Questions?

If you have questions about contributing:

1. Check existing **documentation** and **issues**
2. Ask in **discussions** (if available)
3. Open an **issue** with the "question" label

---

## License

By contributing to ClickNCheck, you agree that your contributions will be licensed under the same license as the project.

---

**Thank you for contributing to ClickNCheck!** 🎉
