# Changelog

All notable changes to the ClickNCheck Mobile Automation Framework will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added
- Comprehensive JavaDoc documentation for core framework classes (TestConfig, TestLogger, AppiumManager, UiElement)
- CHANGELOG.md for tracking version changes
- CONTRIBUTING.md with development guidelines

## [1.1.0] - 2026-01-20

### Added
- **TestConfig class** for centralized configuration management
  - Configurable wait timeouts via system properties (`-Ddefault.wait`, `-Dlong.wait`, `-Dshort.wait`)
  - Configurable screenshot directory (`-Dscreenshot.dir`)
  - Configurable screenshot retention (`-Dscreenshot.retention.days`)
- **SLF4J + Logback logging framework**
  - Console and file appenders
  - Rolling log files with 7-day retention
  - Configurable log levels per class
  - Logs saved to `target/logs/`
- **Helper methods in UiObject** for better error handling
  - `findByLocator()` - Reflection wrapper with detailed error messages
  - `findAppiumByLocator()` - AppiumBy reflection wrapper
  - `validatePlatformAndDriver()` - Platform and driver validation
- **Named constants in AppiumManager**
  - `EMULATOR_BOOT_TIMEOUT_SECONDS` = 300 seconds
  - `EMULATOR_BOOT_CHECK_INTERVAL_MS` = 5000 ms

### Changed
- **TestLogger complete rewrite**
  - Now thread-safe using ThreadLocal
  - Includes timestamps (Instant precision) on every log entry
  - Sequential step numbering per thread
  - Proper JSON generation using Gson
  - Added `cleanup()` method to prevent memory leaks
  - Added `clearLogs()` for test isolation
- **AndroidSettings.initialize()** signature changed
  - Added parameters: `appPath`, `appPackage`, `appActivity`
  - Android app capabilities now properly configured (no longer commented out)
- **AppiumManager improvements**
  - Replaced all `System.out.println` with SLF4J logger
  - Added configurable Appium path via `APPIUM_PATH` env var or `-Dappium.path` system property
  - Emulator boot wait now has timeout (5 minutes)
  - Fixed resource leaks in `waitForEmulatorToBoot()` (BufferedReader, Process properly closed)
  - Better error handling with proper exception propagation
- **UiObject error handling improvements**
  - Updated `findOneElement()`, `clickWithWait()`, `sendTextWithWait()` with specific exception handling
  - All exceptions now include detailed context (element name, selector, locator)
  - Specific handling for NoSuchElementException, TimeoutException, StaleElementReferenceException
- **Screenshot management improvements**
  - Auto-creates directories (`target/screenshots/{android,ios,fails}`)
  - Proper timestamp format (`yyyyMMdd_HHmmss`)
  - Uses configurable `TestConfig.SCREENSHOT_DIR`
  - Better error handling and logging
- **Wait timeouts**
  - All 56 hardcoded `Duration.ofSeconds()` calls replaced with `TestConfig` constants
  - Now configurable via system properties

### Fixed
- **Critical Issues**
  - Wrong package import in IosSettings.java (`agency.sevenofnine.testing` → `com.exit3.testing`)
  - Hardcoded Appium path (now configurable)
- **High Priority Issues**
  - Incomplete error handling (now comprehensive with specific exceptions)
  - Hardcoded wait timeouts (now configurable)
  - Android app capabilities (now properly set)
  - No null safety checks (added throughout)
  - Reflection errors without context (now detailed)
  - Screenshot file management (auto-creates dirs, proper timestamps)
- **Medium Priority Issues**
  - No logging configuration (added SLF4J + Logback)
  - Magic numbers (replaced with named constants)
  - TestLogger inefficiency (complete rewrite, thread-safe)
  - Emulator management issues (added timeout, fixed resource leaks)

### Security
- Enhanced input validation in shell command execution
- Better resource cleanup prevents potential resource exhaustion

## [1.0.0] - 2026-01-19

### Added
- Initial framework release
- Cross-platform support for Android and iOS
- UiElement builder pattern for defining UI elements
- UiObject for element interactions
- Appium server management
- Android emulator management
- ThreadLocal-based platform context
- Screenshot capture capabilities
- Fluent interface for method chaining
- Support for nested element interactions
- Gesture support (swipe, scroll)
- Performance optimizations (disabled animations)

### Dependencies
- Appium Java Client 9.3.0
- Selenium 4.25.0
- TestNG 7.9.0
- JUnit 4.13.2
- SLF4J 2.0.9
- Logback 1.4.14
- Apache Commons IO 2.15.1
- Gson 2.10.1

---

## Version History

### [Unreleased]
- In development

### [1.1.0] - 2026-01-20
- Major improvements to error handling, logging, and configurability
- Thread-safe TestLogger rewrite
- Comprehensive fixes for CRITICAL, HIGH, and MEDIUM priority issues

### [1.0.0] - 2026-01-19
- Initial release with core mobile automation features

---

## Migration Guide

### Migrating from 1.0.0 to 1.1.0

#### AndroidSettings.initialize() Signature Change
**Before:**
```java
AndroidSettings.initialize(phoneName, phoneVersion, ipAddress, port);
```

**After:**
```java
AndroidSettings.initialize(phoneName, phoneVersion, appPath, appPackage, appActivity, ipAddress, port);
```

#### TestLogger Usage
**Before:**
```java
TestLogger.logMessages.clear(); // Direct field access
```

**After:**
```java
TestLogger.clearLogs(); // Use method
TestLogger.cleanup(); // Add cleanup in @AfterMethod
```

#### Configure Wait Timeouts
**New capability:**
```bash
mvn test -Ddefault.wait=40 -Dlong.wait=90 -Dshort.wait=15
```

#### Configure Appium Path
**New capability:**
```bash
export APPIUM_PATH=/custom/path/to/appium
# or
mvn test -Dappium.path=/custom/path/to/appium
```

---

## Acknowledgments

- Claude (Anthropic) - Code analysis and improvements
- ClickNCheck Development Team
