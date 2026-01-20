package com.exit3.testing;

import java.io.FileNotFoundException;

/**
 * Builder class for creating cross-platform UI element definitions.
 * <p>
 * UiElement provides a fluent API for defining UI elements that work on both
 * Android and iOS platforms. Each element is defined with platform-specific
 * selectors and locators, then converted into a {@link UiObject} for interaction.
 * </p>
 *
 * <h2>Supported Selector Types:</h2>
 * <ul>
 *   <li><b>xpath</b> - XPath selector (use "xpath" for {@link org.openqa.selenium.By})</li>
 *   <li><b>id</b> - Resource ID (use "id" for By.id)</li>
 *   <li><b>className</b> - Class name (use "className" for By.className)</li>
 *   <li><b>accessibilityId</b> - Accessibility ID (use "accessibilityId" for AppiumBy)</li>
 *   <li><b>androidUIAutomator</b> - Android UiAutomator (use "androidUIAutomator" for AppiumBy)</li>
 *   <li><b>iOSClassChain</b> - iOS Class Chain (use "iOSClassChain" for AppiumBy)</li>
 * </ul>
 *
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * // Define a login button that works on both platforms
 * UiObject loginButton = new UiElement()
 *     .name("Login Button",
 *           "id", "com.app:id/login_btn",              // Android
 *           "accessibilityId", "LoginButton")          // iOS
 *     .makeUiObject();
 *
 * // Interact with the element
 * loginButton.clickWithWait();
 *
 * // Define using XPath
 * UiObject emailField = new UiElement()
 *     .name("Email Field",
 *           "xpath", "//android.widget.EditText[@text='Email']",
 *           "xpath", "//XCUIElementTypeTextField[@name='Email']")
 *     .makeUiObject();
 *
 * emailField.sendTextWithWait("user@example.com");
 * }</pre>
 *
 * <h2>Best Practices:</h2>
 * <ul>
 *   <li>Use meaningful element names for better logging and debugging</li>
 *   <li>Prefer accessibility IDs over XPath when possible (more stable)</li>
 *   <li>Keep Android and iOS locators in sync when possible</li>
 *   <li>Use resource IDs for Android and accessibility IDs for iOS</li>
 * </ul>
 *
 * @author ClickNCheck Framework
 * @version 1.0
 * @since 1.0
 * @see UiObject
 */
public class UiElement {
    private String element;
    private String androidSelector;
    private String androidLocator;
    private String iosSelector;
    private String iosLocator;

    /**
     * Defines a cross-platform UI element with platform-specific selectors.
     * <p>
     * This is the primary builder method for creating UI element definitions.
     * It accepts a human-readable name and separate Android and iOS locator
     * strategies.
     * </p>
     *
     * @param name human-readable element name for logging (e.g., "Login Button")
     * @param aSelector Android selector type (e.g., "id", "xpath", "accessibilityId")
     * @param aLocator Android locator value (e.g., "com.app:id/login", "//android.widget.Button")
     * @param iSelector iOS selector type (e.g., "accessibilityId", "xpath", "iOSClassChain")
     * @param iLocator iOS locator value (e.g., "LoginButton", "//XCUIElementTypeButton")
     * @return this UiElement for method chaining
     *
     * @see #makeUiObject()
     */
    public UiElement name(String name,
                          String aSelector,
                          String aLocator,
                          String iSelector,
                          String iLocator) {
        element = name;
        androidSelector = aSelector;
        androidLocator = aLocator;
        iosSelector = iSelector;
        iosLocator = iLocator;
        return this;
    }

    /**
     * Converts this UiElement definition into a UiObject for interaction.
     * <p>
     * This method finalizes the element definition and creates a UiObject
     * that can be used to interact with the element on the device. The
     * UiObject will automatically use the correct platform-specific locator
     * based on the current platform setting.
     * </p>
     *
     * @return a new UiObject instance ready for interaction
     * @throws FileNotFoundException if there are file-related issues during object creation
     *
     * @see UiObject
     * @see UiObject#setPlatform(String)
     */
    public UiObject makeUiObject() throws FileNotFoundException {
        return new UiObject(element, androidSelector, androidLocator, iosSelector, iosLocator);
    }
}
