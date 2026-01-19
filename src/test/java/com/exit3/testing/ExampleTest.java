package com.exit3.testing;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.Point;
import org.testng.annotations.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;

import static org.testng.Assert.*;

/**
 * Example TestNG test class demonstrating ClickNCheck framework usage.
 *
 * This test class shows how to:
 * - Configure Appium drivers for Android and iOS
 * - Create UI elements using UiElement builder
 * - Perform various interactions (click, text input, scroll, swipe)
 * - Verify element states
 * - Use method chaining for cleaner test code
 *
 * Note: This is a template/example. Replace with actual app details:
 * - Update app package, activity, bundle ID
 * - Update element locators to match your app
 * - Configure device details in TestNG XML
 */
public class ExampleTest {

    // Driver instance (thread-safe)
    private static ThreadLocal<Object> driver = new ThreadLocal<>();

    // Test parameters
    private String platform;
    private String deviceName;
    private String platformVersion;
    private String appPackage;
    private String appActivity;
    private String bundleId;
    private String udid;
    private String xcodeOrgId;
    private String xcodeSigningId;

    /**
     * Setup method - runs once before all tests in this class
     * Initializes the Appium driver based on platform
     *
     * @param platform "android" or "ios"
     * @param deviceName Device name (e.g., "Pixel 5", "iPhone 14")
     * @param platformVersion OS version (e.g., "13.0", "16.0")
     * @param appPackage Android app package (e.g., "com.example.app")
     * @param appActivity Android main activity (e.g., ".MainActivity")
     * @param bundleId iOS bundle identifier (e.g., "com.example.app")
     * @param udid iOS device UDID
     * @param xcodeOrgId Xcode organization ID for iOS
     * @param xcodeSigningId Xcode signing identity for iOS
     */
    @BeforeClass
    @Parameters({"platform", "deviceName", "platformVersion", "appPackage",
                 "appActivity", "bundleId", "udid", "xcodeOrgId", "xcodeSigningId"})
    public void setupDriver(
            @Optional("android") String platform,
            @Optional("Pixel_5_API_33") String deviceName,
            @Optional("13.0") String platformVersion,
            @Optional("com.android.settings") String appPackage,
            @Optional(".Settings") String appActivity,
            @Optional("com.apple.Preferences") String bundleId,
            @Optional("") String udid,
            @Optional("") String xcodeOrgId,
            @Optional("") String xcodeSigningId
    ) throws IOException, InterruptedException {

        this.platform = platform.toLowerCase();
        this.deviceName = deviceName;
        this.platformVersion = platformVersion;
        this.appPackage = appPackage;
        this.appActivity = appActivity;
        this.bundleId = bundleId;
        this.udid = udid;
        this.xcodeOrgId = xcodeOrgId;
        this.xcodeSigningId = xcodeSigningId;

        // Set platform context for UiObject
        UiObject.setPlatform(this.platform);

        // Initialize driver based on platform
        if ("android".equals(this.platform)) {
            initializeAndroidDriver();
        } else if ("ios".equals(this.platform)) {
            initializeIosDriver();
        } else {
            throw new IllegalArgumentException("Invalid platform: " + platform + ". Use 'android' or 'ios'");
        }

        System.out.println("=".repeat(60));
        System.out.println("Test Setup Complete");
        System.out.println("Platform: " + this.platform);
        System.out.println("Device: " + this.deviceName);
        System.out.println("OS Version: " + this.platformVersion);
        System.out.println("=".repeat(60));
    }

    /**
     * Initialize Android driver with configuration
     */
    private void initializeAndroidDriver() throws IOException, InterruptedException {
        String ipAddress = System.getProperty("appium.ip", "127.0.0.1");
        String port = System.getProperty("appium.port", "4723");

        AndroidDriver androidDriver = AndroidSettings.initialize(
                deviceName,
                platformVersion,
                ipAddress,
                port
        );

        driver.set(androidDriver);

        // Note: In real tests, you should set these capabilities in AndroidSettings:
        // capabilities.setCapability("appium:appPackage", appPackage);
        // capabilities.setCapability("appium:appActivity", appActivity);
    }

    /**
     * Initialize iOS driver with configuration
     */
    private void initializeIosDriver() throws MalformedURLException, InterruptedException {
        String ipAddress = System.getProperty("appium.ip", "127.0.0.1");
        String port = System.getProperty("appium.port", "4723");

        IOSDriver iosDriver = IosSettings.initialize(
                deviceName,          // appName
                bundleId,            // appPackage
                deviceName,          // phoneName
                udid,                // phoneId
                platformVersion,     // phoneVersion
                xcodeOrgId,          // xcodeOrgId
                xcodeSigningId,      // xcodeSigningId
                ipAddress,
                port
        );

        driver.set(iosDriver);
    }

    /**
     * Setup method - runs before each test method
     */
    @BeforeMethod
    public void beforeTest() {
        System.out.println("\n--- Starting Test ---");
    }

    /**
     * Teardown method - runs after each test method
     */
    @AfterMethod
    public void afterTest() {
        System.out.println("--- Test Complete ---\n");
    }

    /**
     * Example Test 1: Basic Element Interaction
     *
     * Demonstrates:
     * - Creating UI elements with platform-specific locators
     * - Clicking elements
     * - Text input
     * - Method chaining
     * - Element verification
     */
    @Test(priority = 1, description = "Test basic element interactions")
    public void testBasicInteraction() throws FileNotFoundException, NoSuchMethodException,
            InvocationTargetException, IllegalAccessException, IOException {

        // Create UI elements with Android and iOS locators
        UiObject searchField = new UiElement()
                .name(
                        "Search Field",
                        "id", "android:id/search_src_text",     // Android locator
                        "accessibility_id", "SearchField"        // iOS locator
                )
                .makeUiObject();

        UiObject searchButton = new UiElement()
                .name(
                        "Search Button",
                        "id", "android:id/search_button",
                        "accessibility_id", "SearchButton"
                )
                .makeUiObject();

        // Perform actions using method chaining
        searchField
                .waitUntil()                              // Wait for element to appear
                .clearTextWithWait()                      // Clear any existing text
                .sendTextWithWait("Appium Test")          // Type text
                .sendEnterWithWait();                     // Press Enter

        // Verify element is displayed
        assertTrue(searchField.isDisplayed(10),
                "Search field should be displayed");
    }

    /**
     * Example Test 2: Working with Lists
     *
     * Demonstrates:
     * - Finding multiple elements
     * - Interacting with indexed elements
     * - Getting text from elements
     */
    @Test(priority = 2, description = "Test list element interactions")
    public void testListInteraction() throws FileNotFoundException, NoSuchMethodException,
            InvocationTargetException, IllegalAccessException, IOException {

        // Create a list item element
        UiObject listItem = new UiElement()
                .name(
                        "List Item",
                        "class_name", "android.widget.TextView",
                        "class_name", "XCUIElementTypeCell"
                )
                .makeUiObject();

        // Find all matching elements
        listItem.findAllElements();

        // Click the 3rd item (0-indexed)
        listItem.clickElementWithIndexAndWait(2);

        // Get text from the 1st item
        String firstItemText = listItem.getTextFromElementWithIndexAndWait(0);
        System.out.println("First item text: " + firstItemText);

        assertNotNull(firstItemText, "First item should have text");
    }

    /**
     * Example Test 3: Scrolling Operations
     *
     * Demonstrates:
     * - Scrolling to elements
     * - Scrolling to elements with text
     * - Scrolling up
     */
    @Test(priority = 3, description = "Test scrolling operations")
    public void testScrolling() throws FileNotFoundException, NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {

        UiObject scrollableElement = new UiElement()
                .name(
                        "Scrollable List",
                        "id", "android:id/list",
                        "accessibility_id", "MainList"
                )
                .makeUiObject();

        // Scroll to element
        scrollableElement.scrollToElement();

        // Scroll to element with specific text
        scrollableElement.scrollToElementWithText("Settings");

        // Scroll up to element
        scrollableElement.scrollUpToElement("Top Item");
    }

    /**
     * Example Test 4: Swipe Gestures
     *
     * Demonstrates:
     * - Swiping left, right, up
     * - Tap at coordinates
     */
    @Test(priority = 4, description = "Test swipe gestures")
    public void testSwipeGestures() throws FileNotFoundException, NoSuchMethodException,
            InvocationTargetException, IllegalAccessException, IOException {

        UiObject carouselItem = new UiElement()
                .name(
                        "Carousel Item",
                        "id", "com.example:id/carousel_item",
                        "accessibility_id", "CarouselItem"
                )
                .makeUiObject();

        // Swipe left
        carouselItem.swipeElementLeft();

        // Wait a moment
        try { Thread.sleep(1000); } catch (InterruptedException e) { }

        // Swipe right
        carouselItem.swipeElementRight();

        // Swipe up
        carouselItem.swipeElementUp();

        // Tap at specific coordinates (x=100, y=200)
        carouselItem.tapAtCoordinates(100, 200);
    }

    /**
     * Example Test 5: Element State Verification
     *
     * Demonstrates:
     * - Checking if element is displayed
     * - Checking if element is enabled
     * - Checking if element is selected
     * - Non-throwing conditional checks
     */
    @Test(priority = 5, description = "Test element state verification")
    public void testElementVerification() throws FileNotFoundException, NoSuchMethodException,
            InvocationTargetException, IllegalAccessException, IOException {

        UiObject button = new UiElement()
                .name(
                        "Submit Button",
                        "id", "com.example:id/submit_btn",
                        "accessibility_id", "SubmitButton"
                )
                .makeUiObject();

        // Check if displayed (throws exception if not found)
        boolean isDisplayed = button.isDisplayed(10);
        assertTrue(isDisplayed, "Button should be displayed");

        // Non-throwing check (returns false if not found)
        boolean exists = button.ifIsDisplayed(5);
        System.out.println("Button exists: " + exists);

        // Check if enabled
        boolean isEnabled = button.isEnabled(5);
        System.out.println("Button is enabled: " + isEnabled);

        // Check if element with specific text is displayed
        boolean hasText = button.isElementWithTextDisplayed(5, "Submit");
        System.out.println("Button has 'Submit' text: " + hasText);
    }

    /**
     * Example Test 6: Working with Nested Elements
     *
     * Demonstrates:
     * - Interacting with child elements
     * - Clicking inside parent elements
     * - Getting text from nested elements
     * - Sending text to nested elements
     */
    @Test(priority = 6, description = "Test nested element interactions")
    public void testNestedElements() throws FileNotFoundException, NoSuchMethodException,
            InvocationTargetException, IllegalAccessException, IOException {

        // Parent element
        UiObject cardContainer = new UiElement()
                .name(
                        "Card Container",
                        "id", "com.example:id/card_container",
                        "accessibility_id", "CardContainer"
                )
                .makeUiObject();

        // Child element
        UiObject actionButton = new UiElement()
                .name(
                        "Action Button",
                        "id", "com.example:id/action_btn",
                        "accessibility_id", "ActionButton"
                )
                .makeUiObject();

        // Click child element inside parent
        cardContainer.clickInsideWithWait(actionButton);

        // Get text from child element
        String childText = cardContainer.getTextInsideWithWait(actionButton);
        System.out.println("Child element text: " + childText);

        // Send text to child element
        cardContainer.sendTextInsideWithWait(actionButton, "Test input");
    }

    /**
     * Example Test 7: Try Methods (Non-Throwing)
     *
     * Demonstrates:
     * - Using try methods that don't throw exceptions
     * - Custom timeout values
     * - Graceful failure handling
     */
    @Test(priority = 7, description = "Test try methods with custom timeouts")
    public void testTryMethods() throws FileNotFoundException {

        UiObject optionalElement = new UiElement()
                .name(
                        "Optional Element",
                        "id", "com.example:id/optional",
                        "accessibility_id", "OptionalElement"
                )
                .makeUiObject();

        // Try clicking with custom timeout (doesn't throw exception if fails)
        optionalElement.tryClickWithWait(5);

        // Try getting text (returns empty/null if fails)
        String text = optionalElement.tryGetTextWithWait(5);
        System.out.println("Optional element text: " + (text != null ? text : "Not found"));

        // Try sending text (continues even if fails)
        optionalElement.trySendTextWithWait("Optional input", 3);
    }

    /**
     * Example Test 8: Finding Elements with Text
     *
     * Demonstrates:
     * - Finding elements by text content
     * - Clicking elements with specific text
     * - Partial text matching
     */
    @Test(priority = 8, description = "Test finding elements by text")
    public void testFindByText() throws FileNotFoundException, NoSuchMethodException,
            InvocationTargetException, IllegalAccessException, IOException {

        UiObject textElement = new UiElement()
                .name(
                        "Text Element",
                        "class_name", "android.widget.TextView",
                        "class_name", "XCUIElementTypeStaticText"
                )
                .makeUiObject();

        // Find element with exact text
        textElement.findElementWithText("Welcome");

        // Click element containing specific text
        textElement.clickElementWithText("Login");

        // Click element that contains partial text
        textElement.clickElementThatContainsText("Sign");

        // Click element with text at specific index
        textElement.clickElementWithTextAndIndex("Item", 2);
    }

    /**
     * Example Test 9: Getting Element Location
     *
     * Demonstrates:
     * - Getting element coordinates
     * - Using location for custom actions
     */
    @Test(priority = 9, description = "Test getting element location")
    public void testElementLocation() throws FileNotFoundException, NoSuchMethodException,
            InvocationTargetException, IllegalAccessException, IOException {

        UiObject element = new UiElement()
                .name(
                        "Located Element",
                        "id", "com.example:id/element",
                        "accessibility_id", "Element"
                )
                .makeUiObject();

        // Get element location
        Point location = element.getLocation();
        System.out.println("Element location - X: " + location.getX() + ", Y: " + location.getY());

        // Can use location for tap at coordinates
        element.tapAtCoordinates(location.getX(), location.getY());
    }

    /**
     * Example Test 10: Complete User Flow
     *
     * Demonstrates:
     * - A realistic end-to-end test scenario
     * - Combining multiple operations
     * - Assertions throughout the flow
     */
    @Test(priority = 10, description = "Test complete user flow")
    public void testCompleteUserFlow() throws FileNotFoundException, NoSuchMethodException,
            InvocationTargetException, IllegalAccessException, IOException {

        System.out.println("=== Starting Complete User Flow Test ===");

        // Step 1: Navigate to login screen
        UiObject loginButton = new UiElement()
                .name("Login Button", "id", "login_btn", "accessibility_id", "LoginButton")
                .makeUiObject();

        loginButton.waitUntil().clickWithWait();
        System.out.println("✓ Navigated to login screen");

        // Step 2: Enter credentials
        UiObject emailField = new UiElement()
                .name("Email Field", "id", "email", "accessibility_id", "EmailField")
                .makeUiObject();

        UiObject passwordField = new UiElement()
                .name("Password Field", "id", "password", "accessibility_id", "PasswordField")
                .makeUiObject();

        emailField
                .waitUntil()
                .clearTextWithWait()
                .sendTextWithWait("test@example.com");
        System.out.println("✓ Entered email");

        passwordField
                .clearTextWithWait()
                .sendTextWithWait("password123");
        System.out.println("✓ Entered password");

        // Step 3: Submit form
        UiObject submitButton = new UiElement()
                .name("Submit Button", "id", "submit", "accessibility_id", "SubmitButton")
                .makeUiObject();

        submitButton.clickWithWait();
        System.out.println("✓ Submitted login form");

        // Step 4: Verify success
        UiObject welcomeMessage = new UiElement()
                .name("Welcome Message", "id", "welcome", "accessibility_id", "WelcomeMessage")
                .makeUiObject();

        boolean isLoggedIn = welcomeMessage.ifIsDisplayed(10);
        assertTrue(isLoggedIn, "User should be logged in");
        System.out.println("✓ Login successful");

        System.out.println("=== Complete User Flow Test Passed ===");
    }

    /**
     * Cleanup method - runs once after all tests in this class
     * Stops the Appium server and closes the driver
     */
    @AfterClass
    public void teardownDriver() {
        try {
            // Close driver
            if (driver.get() != null) {
                if (driver.get() instanceof AndroidDriver) {
                    ((AndroidDriver) driver.get()).quit();
                } else if (driver.get() instanceof IOSDriver) {
                    ((IOSDriver) driver.get()).quit();
                }
                System.out.println("Driver closed successfully");
            }

            // Stop Appium server
            AppiumManager.stopAppiumServer();
            System.out.println("Appium server stopped successfully");

        } catch (Exception e) {
            System.err.println("Error during teardown: " + e.getMessage());
        }
    }
}
