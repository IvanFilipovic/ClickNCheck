# ClickNCheck - Mobile Automation Framework

A comprehensive Java-based mobile automation framework using Appium, TestNG, and Selenium WebDriver for cross-platform (Android & iOS) mobile application testing.

## Table of Contents
- [Overview](#overview)
- [Architecture](#architecture)
- [Project Structure](#project-structure)
- [UiObject Methods](#uiobject-methods)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [Usage Examples](#usage-examples)
- [Requirements](#requirements)

## Overview

ClickNCheck is a robust mobile test automation framework designed to simplify cross-platform mobile app testing. It provides a high-level abstraction layer over Appium, allowing testers to write clean, maintainable tests that work seamlessly on both Android and iOS platforms.

### Key Features

- **Cross-Platform Support**: Single API for both Android (Uiautomator2) and iOS (XCUITest) automation
- **Thread-Safe Execution**: Uses ThreadLocal for parallel test execution
- **Rich Element Interaction**: Comprehensive set of methods for element manipulation
- **Smart Waiting**: Built-in intelligent waits and retry mechanisms
- **Automatic Screenshots**: Captures screenshots on test failures for debugging
- **Gesture Support**: Native gesture support (swipe, scroll, tap) for both platforms
- **Fluent Interface**: Method chaining for cleaner test code
- **Detailed Logging**: Built-in test execution logging

## Architecture

The framework follows a layered architecture:

```
┌─────────────────────────────────────┐
│        Test Layer (TestNG)          │
│     (Your Test Classes)             │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│     Abstraction Layer               │
│  UiObject + UiElement (Builder)     │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│    Driver Management Layer          │
│ AndroidSettings + IosSettings       │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│   Infrastructure Layer              │
│      AppiumManager                  │
└─────────────────────────────────────┘
```

## Project Structure

```
ClickNCheck/
├── pom.xml                              # Maven configuration
├── README.md                            # This file
├── testng.xml                           # TestNG test suite configuration
└── src/
    ├── main/java/com/exit3/testing/
    │   ├── UiObject.java               # Core UI automation class
    │   ├── UiElement.java              # UI element builder
    │   ├── AndroidSettings.java        # Android driver configuration
    │   ├── IosSettings.java            # iOS driver configuration
    │   ├── AppiumManager.java          # Appium server management
    │   └── TestLogger.java             # Test logging utility
    └── test/java/com/exit3/testing/
        └── ExampleTest.java            # Example test implementation
```

## UiObject Methods

The `UiObject` class is the heart of the framework, providing 47+ methods for mobile element interaction. Below is a comprehensive list organized by category:

### Element Location & Finding

| Method | Description | Returns |
|--------|-------------|---------|
| `findOneElement()` | Locates a single element using configured selectors | UiObject |
| `findAllElements()` | Locates all matching elements | UiObject |
| `findElementWithText(String text)` | Finds an element containing specific text | UiObject |
| `getLocation()` | Gets element coordinates as Point object | Point |

### Click & Tap Operations

| Method | Description | Returns |
|--------|-------------|---------|
| `clickWithWait()` | Clicks element with explicit wait | UiObject |
| `tapAtCoordinates(int x, int y)` | Taps at specific screen coordinates | UiObject |
| `clickElementWithText(String text)` | Clicks element containing text | UiObject |
| `clickElementWithTextAndIndex(String text, int index)` | Clicks indexed element with text | UiObject |
| `clickElementWithIndexAndWait(Integer index)` | Clicks element at specific index | UiObject |
| `clickElementThatContainsText(String text)` | Clicks element with partial text match | UiObject |
| `clickInsideWithWait(UiObject child)` | Clicks nested child element | UiObject |
| `clickDoubleNestedElement()` | Clicks doubly nested element | UiObject |
| `tryClickWithWait(Integer waitTime)` | Attempts click with custom timeout | UiObject |

### Text Input Operations

| Method | Description | Returns |
|--------|-------------|---------|
| `sendText(String text)` | Types text into element | UiObject |
| `sendTextWithWait(String text)` | Types text with explicit wait | UiObject |
| `sendTextInsideWithWait(UiObject child, String text)` | Types into nested element | UiObject |
| `sendTextToElementWithIndexAndWait(Integer index, String text)` | Types into indexed element | UiObject |
| `sendTextToDoubleNestedElement()` | Types into doubly nested element | UiObject |
| `trySendTextWithWait(String text, Integer waitTime)` | Attempts text input with custom timeout | UiObject |
| `sendEnterWithWait()` | Sends Enter/Return key | UiObject |
| `clearTextWithWait()` | Clears text field | UiObject |
| `clearTextFromElementWithIndexAndWait(Integer index)` | Clears indexed element text | UiObject |

### Text Retrieval Operations

| Method | Description | Returns |
|--------|-------------|---------|
| `getText()` | Gets element text content | String |
| `getTextWithWait()` | Gets text with explicit wait | String |
| `tryGetTextWithWait(Integer waitTime)` | Attempts to get text with custom timeout | String |
| `getTextInsideWithWait(UiObject child)` | Gets text from nested element | String |
| `getTextFromElementWithIndexAndWait(Integer index)` | Gets text from indexed element | String |
| `getTextFromDoubleNestedElement()` | Gets text from doubly nested element | String |

### Element State Verification

| Method | Description | Returns |
|--------|-------------|---------|
| `isDisplayed(Integer waitTime)` | Checks if element is visible | Boolean |
| `ifIsDisplayed(Integer waitTime)` | Conditional check for display (non-throwing) | Boolean |
| `ifIsDisplayedWithText(Integer waitTime, String text)` | Checks if element with text is displayed | Boolean |
| `isElementWithTextDisplayed(Integer waitTime, String text)` | Waits and verifies element text visible | Boolean |
| `elementWithTextIsNotDisplayed(Integer waitTime, String text)` | Verifies element text not displayed | Boolean |
| `isEnabled(Integer waitTime)` | Checks if element is enabled | Boolean |
| `isElementWithIndexEnabled(Integer waitTime, Integer index)` | Checks if indexed element is enabled | Boolean |
| `isSelected(Integer waitTime)` | Checks if element is selected | Boolean |

### Scroll Operations

| Method | Description | Returns |
|--------|-------------|---------|
| `scrollToElement()` | Scrolls to make element visible | UiObject |
| `scrollToElementWithText(String text)` | Scrolls to element containing text | UiObject |
| `scrollUpToElement(String text)` | Scrolls upward to element | UiObject |

### Swipe/Gesture Operations

| Method | Description | Returns |
|--------|-------------|---------|
| `swipeElementLeft()` | Swipes element left | UiObject |
| `swipeElementRight()` | Swipes element right | UiObject |
| `swipeElementUp()` | Swipes element upward | UiObject |

### Wait & Synchronization

| Method | Description | Returns |
|--------|-------------|---------|
| `waitUntil()` | Waits for element presence | UiObject |

### Screenshot & Logging (Static Methods)

| Method | Description | Returns |
|--------|-------------|---------|
| `screenshotAndroid(String name, String locator)` | Captures Android screenshot (Base64) | String |
| `screenshotIos(String name, String locator)` | Captures iOS screenshot | String |
| `screenshotFail(String testName)` | Captures failure screenshot | String |

### Platform Management (Static Methods)

| Method | Description | Returns |
|--------|-------------|---------|
| `getPlatform()` | Gets current platform (android/ios) | String |
| `setPlatform(String platform)` | Sets active platform for test | void |

## Getting Started

### Prerequisites

1. **Java Development Kit (JDK)** 11 or higher
2. **Maven** 3.6 or higher
3. **Node.js** and **npm** (for Appium)
4. **Appium Server** 2.x
5. **Android SDK** (for Android testing)
6. **Xcode** (for iOS testing on macOS)

### Installation

1. Clone the repository:
```bash
git clone <repository-url>
cd ClickNCheck
```

2. Install dependencies:
```bash
mvn clean install
```

3. Install Appium globally:
```bash
npm install -g appium
appium driver install uiautomator2  # For Android
appium driver install xcuitest      # For iOS
```

4. Start Appium server:
```bash
appium --log-level error
```

## Configuration

### Android Configuration

Edit `AndroidSettings.java` to configure your Android device:

```java
AndroidDriver driver = AndroidSettings.initialize(
    "Pixel 5",          // Device name
    "13.0",            // Android version
    "127.0.0.1",       // Appium server IP
    "4723"             // Appium server port
);
```

Uncomment and set these capabilities for your app:
```java
capabilities.setCapability("appium:app", "/path/to/app.apk");
capabilities.setCapability("appium:appPackage", "com.example.app");
capabilities.setCapability("appium:appActivity", ".MainActivity");
```

### iOS Configuration

Edit `IosSettings.java` to configure your iOS device:

```java
IOSDriver driver = IosSettings.initialize(
    "MyApp",                    // App name
    "com.example.myapp",        // Bundle ID
    "iPhone 14",                // Device name
    "00008030-XXXXXXXXXXXX",    // UDID
    "16.0",                     // iOS version
    "TEAM_ID",                  // Xcode org ID
    "iPhone Developer",         // Signing ID
    "127.0.0.1",               // Server IP
    "4723"                     // Server port
);
```

### TestNG Configuration

Configure test execution in `testng.xml`:

```xml
<suite name="Mobile Test Suite">
    <test name="Android Tests">
        <classes>
            <class name="com.exit3.testing.ExampleTest"/>
        </classes>
    </test>
</suite>
```

## Usage Examples

### Creating UI Elements

Use the `UiElement` builder to define platform-specific locators:

```java
// Create a UI element with Android and iOS locators
UiObject loginButton = new UiElement()
    .name(
        "Login Button",                           // Element name
        "id", "com.app:id/login_btn",            // Android: selector type, locator
        "accessibility_id", "LoginButton"         // iOS: selector type, locator
    )
    .makeUiObject();
```

### Basic Test Flow

```java
@Test
public void testLogin() throws Exception {
    // Set platform
    UiObject.setPlatform("android"); // or "ios"

    // Initialize driver
    AndroidDriver driver = AndroidSettings.initialize("Pixel 5", "13.0", "127.0.0.1", "4723");

    // Create UI elements
    UiObject usernameField = new UiElement()
        .name("Username", "id", "username", "accessibility_id", "username_field")
        .makeUiObject();

    UiObject passwordField = new UiElement()
        .name("Password", "id", "password", "accessibility_id", "password_field")
        .makeUiObject();

    UiObject loginButton = new UiElement()
        .name("Login", "id", "login_btn", "accessibility_id", "login_button")
        .makeUiObject();

    // Perform actions
    usernameField
        .waitUntil()
        .sendTextWithWait("testuser@example.com");

    passwordField
        .sendTextWithWait("password123");

    loginButton.clickWithWait();

    // Verify login success
    UiObject welcomeMessage = new UiElement()
        .name("Welcome", "xpath", "//android.widget.TextView[@text='Welcome']",
              "xpath", "//XCUIElementTypeStaticText[@name='Welcome']")
        .makeUiObject();

    assertTrue(welcomeMessage.isDisplayed(10));
}
```

### Method Chaining

The fluent interface allows for clean, readable test code:

```java
searchField
    .waitUntil()
    .clearTextWithWait()
    .sendTextWithWait("test query")
    .sendEnterWithWait();

String result = resultElement
    .waitUntil()
    .getTextWithWait();
```

### Working with Lists

```java
// Click the 3rd item in a list
listItem.clickElementWithIndexAndWait(2); // 0-indexed

// Get text from the 5th item
String text = listItem.getTextFromElementWithIndexAndWait(4);

// Type into the 2nd input field
inputField.sendTextToElementWithIndexAndWait(1, "text");
```

### Nested Elements

```java
// Click a button inside a card
UiObject card = new UiElement()
    .name("Card", "id", "card_container", "accessibility_id", "card")
    .makeUiObject();

UiObject button = new UiElement()
    .name("Button", "id", "action_btn", "accessibility_id", "action")
    .makeUiObject();

card.clickInsideWithWait(button);
```

### Scrolling

```java
// Scroll to element
element.scrollToElement();

// Scroll to element with specific text
element.scrollToElementWithText("Settings");

// Scroll up
element.scrollUpToElement("Top Item");
```

### Gestures

```java
// Swipe left on an element
carouselItem.swipeElementLeft();

// Swipe right
carouselItem.swipeElementRight();

// Swipe up
listItem.swipeElementUp();

// Tap at coordinates
element.tapAtCoordinates(100, 200);
```

### Error Handling with Try Methods

The framework provides "try" variants that don't throw exceptions:

```java
// Try clicking with custom timeout (returns UiObject even if fails)
element.tryClickWithWait(5);

// Try getting text (returns empty string if fails)
String text = element.tryGetTextWithWait(5);

// Try sending text (continues even if fails)
element.trySendTextWithWait("text", 5);
```

## Requirements

### Maven Dependencies

The framework requires the following dependencies (see `pom.xml`):

- **Appium Java Client** 8.x or 9.x
- **Selenium WebDriver** 4.x
- **TestNG** 7.x
- **Apache Commons IO** 2.x
- **SLF4J** (for logging)

### System Requirements

**For Android Testing:**
- Android SDK Tools
- ADB (Android Debug Bridge)
- Android Emulator or physical device
- Uiautomator2 driver

**For iOS Testing (macOS only):**
- Xcode 12+
- Xcode Command Line Tools
- WebDriverAgent
- iOS Simulator or physical device
- XCUITest driver
- Valid Apple Developer account (for real device testing)

## Best Practices

1. **Always set platform**: Call `UiObject.setPlatform("android")` or `UiObject.setPlatform("ios")` before running tests
2. **Use meaningful element names**: Helps with debugging and screenshot identification
3. **Prefer explicit waits**: Use `*WithWait()` methods for better synchronization
4. **Leverage method chaining**: Makes tests more readable
5. **Use try methods for optional actions**: When you don't want test to fail if element not found
6. **Keep locators in separate files**: Consider using Page Object Model for better maintainability
7. **Clean up resources**: Always stop Appium server after test execution

## Troubleshooting

### Common Issues

**Issue**: Element not found
- **Solution**: Increase wait time, verify locator correctness, check if element is in viewport

**Issue**: Appium server connection failed
- **Solution**: Ensure Appium is running on correct port, check firewall settings

**Issue**: iOS tests fail with code signing error
- **Solution**: Configure xcodeOrgId and xcodeSigningId correctly in IosSettings

**Issue**: Android tests fail to start app
- **Solution**: Verify appPackage and appActivity are correctly set

## License

[Add your license information here]

## Contributing

[Add contribution guidelines here]

## Support

[Add support/contact information here]
