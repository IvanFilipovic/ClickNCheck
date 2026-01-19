package com.exit3.testing;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import junit.framework.Assert;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;


import static java.time.Duration.ofMillis;
import static java.util.Collections.singletonList;

@SuppressWarnings("ALL")
public class UiObject {
    private static IOSDriver driverIos;
    private static AndroidDriver driverAndroid;
    private String element_name;
    private String android_locator;
    private String android_selector;
    private String ios_locator;
    private String ios_selector;
    private String child_android_locator;
    private String child_android_selector;
    private String child_ios_locator;
    private String child_ios_selector;
    private String child_of_child_android_locator;
    private String child_of_child_android_selector;
    private String child_of_child_ios_locator;
    private String child_of_child_ios_selector;
    private static ThreadLocal<String> platform = new ThreadLocal<>();
    UiObject(String element, String androidSelector, String androidLocator, String iosSelector, String iosLocator) throws FileNotFoundException {
        this.element_name = element;
        this.android_selector = androidSelector;
        this.android_locator = androidLocator;
        this.ios_selector = iosSelector;
        this.ios_locator = iosLocator;


    }
    public static String getPlatform() {
        return platform.get();
    }
    public static void setPlatform(String platformValue) {
        platform.set(platformValue);
    }
    public static String screenshotAndroid(String element_name, String element_locator) throws IOException{
        driverAndroid = AndroidSettings.driverAndroid.get();
        File srcFile = driverAndroid.getScreenshotAs(OutputType.FILE);
        String filename = element_name;
        long timestamp = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String readableTime = sdf.format(new Date(timestamp));
        File targetFile = new File(System.getProperty("user.dir") + "/screenshots/android/" + filename + "-" + readableTime + ".jpg");
        try {
            FileUtils.copyFile(srcFile, targetFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] fileContent = Files.readAllBytes(targetFile.toPath());
        return Base64.getEncoder().encodeToString(fileContent);
    }
    public static String screenshotIos(String element_name, String element_locator) throws IOException{
        driverIos = IosSettings.driverIos.get();
        File srcFile = driverIos.getScreenshotAs(OutputType.FILE);
        String filename = element_name;
        long timestamp = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String readableTime = sdf.format(new Date(timestamp));
        File targetFile = new File(System.getProperty("user.dir") + "/screenshots/ios/" + filename + "-" + readableTime + ".jpg");
        try {
            FileUtils.copyFile(srcFile, targetFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "test";
    }
    public static String screenshotFail(String testName) throws IOException{
        File srcFile;
        if("android".equalsIgnoreCase(getPlatform())) {
            driverAndroid = AndroidSettings.driverAndroid.get();
            srcFile = driverAndroid.getScreenshotAs(OutputType.FILE);
        }
        else {
            driverIos = IosSettings.driverIos.get();
            srcFile = driverIos.getScreenshotAs(OutputType.FILE);
        }
        String filename = testName;
        long timestamp = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String readableTime = sdf.format(new Date(timestamp));
        File targetFile = new File(System.getProperty("user.dir") + "/screenshots/fails/" + filename + "-" + readableTime + ".jpg");
        try {
            FileUtils.copyFile(srcFile, targetFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] fileContent = Files.readAllBytes(targetFile.toPath());
        return Base64.getEncoder().encodeToString(fileContent);
    }
    public UiObject findOneElement() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        if("android".equalsIgnoreCase(getPlatform())){
            driverAndroid = AndroidSettings.driverAndroid.get();
            try {
                if (Objects.equals(android_selector, "xpath")) {
                    Method method = By.class.getMethod(android_selector, String.class);
                    By by = (By) method.invoke(null, android_locator);
                    driverAndroid.findElement(by);
                    TestLogger.addLogMessage("Element " + element_name + " is found");
                }else {
                    Method method = AppiumBy.class.getMethod(android_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, android_locator);
                    driverAndroid.findElement(by);
                    TestLogger.addLogMessage("Element " + element_name + " is found");
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                boolean fail = !true;
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                screenshotAndroid(element_name, android_locator);
                assert  fail = false : "Error" + e;
            }
        }
        else if("ios".equalsIgnoreCase(getPlatform())){
            driverIos = IosSettings.driverIos.get();
            try {
                if (Objects.equals(ios_selector, "xpath")) {
                    Method method = By.class.getMethod(ios_selector, String.class);
                    By by = (By) method.invoke(null, ios_locator);
                    driverIos.findElement(by);
                    TestLogger.addLogMessage("Element " + element_name + " je pronađen");
                }else {
                    Method method = AppiumBy.class.getMethod(ios_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, ios_locator);
                    driverIos.findElement(by);
                    TestLogger.addLogMessage("Element " + element_name + " je pronađen");
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                boolean fail = !true;
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                screenshotIos(element_name,ios_locator);
                assert  fail = false : "Error" + e;
            }
        }
        return this;
    }
    public UiObject findAllElements() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        if("android".equalsIgnoreCase(getPlatform())){
            driverAndroid = AndroidSettings.driverAndroid.get();
            try {
                if (Objects.equals(android_selector, "xpath")) {
                    Method method = By.class.getMethod(android_selector, String.class);
                    By by = (By) method.invoke(null, android_locator);
                    driverAndroid.findElements(by);
                    TestLogger.addLogMessage("Elements " + element_name + " are found");
                }else {
                    Method method = AppiumBy.class.getMethod(android_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, android_locator);
                    driverAndroid.findElements(by);
                    TestLogger.addLogMessage("Elements " + element_name + " are found");
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                boolean fail = !true;
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                screenshotAndroid(element_name, android_locator);
                assert  fail = false : "Error" + e;
            }
        }
        else if("ios".equalsIgnoreCase(getPlatform())){
            driverIos = IosSettings.driverIos.get();
            try {
                if (Objects.equals(ios_selector, "xpath")) {
                    Method method = By.class.getMethod(ios_selector, String.class);
                    By by = (By) method.invoke(null, ios_locator);
                    driverIos.findElements(by);
                    TestLogger.addLogMessage("Elements " + element_name + " are found");
                }else {
                    Method method = AppiumBy.class.getMethod(ios_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, ios_locator);
                    driverIos.findElements(by);
                    TestLogger.addLogMessage("Elements " + element_name + " are found");
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                boolean fail = !true;
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                screenshotIos(element_name,ios_locator);
                assert  fail = false : "Error" + e;
            }
        }
        return this;
    }
    public UiObject tapAtCoordinates(int x, int y) throws IOException {
        if ("android".equalsIgnoreCase(getPlatform())) {
            driverAndroid = AndroidSettings.driverAndroid.get();
            try {
                new TouchAction<>(driverAndroid)
                        .tap(PointOption.point(x, y))
                        .waitAction(WaitOptions.waitOptions(Duration.ofMillis(200)))
                        .perform();
                TestLogger.addLogMessage("Tapped at coordinates (" + x + ", " + y + ") on Android");
            } catch (Exception e) {
                TestLogger.addLogMessage("Failed to tap at coordinates (" + x + ", " + y + ") on Android. Error: " + e.getMessage());
                screenshotAndroid(element_name, android_locator);
                Assert.fail("Error: " + e.getMessage());
            }
        } else if ("ios".equalsIgnoreCase(getPlatform())) {
            driverIos = IosSettings.driverIos.get();
            try {
                PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
                Sequence tap = new Sequence(finger, 1)
                        .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y))
                        .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                        .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

                driverIos.perform(Collections.singletonList(tap));
                TestLogger.addLogMessage("Tapped at coordinates (" + x + ", " + y + ") on iOS");
            } catch (Exception e) {
                TestLogger.addLogMessage("Failed to tap at coordinates (" + x + ", " + y + ") on iOS. Error: " + e.getMessage());
                screenshotIos(element_name, ios_locator);
                Assert.fail("Error: " + e.getMessage());;
            }
        }
        return this;
    }
    public UiObject sendText(String text) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        if("android".equalsIgnoreCase(getPlatform())){
            driverAndroid = AndroidSettings.driverAndroid.get();
            WebDriverWait wait = new WebDriverWait(driverAndroid, Duration.ofSeconds(40));
            try {
                if (Objects.equals(android_selector, "xpath")) {
                    Method method = By.class.getMethod(android_selector, String.class);
                    By by = (By) method.invoke(null, android_locator);
                    driverAndroid.findElement(by).sendKeys(text);
                    TestLogger.addLogMessage("The text '" + text + "' has been sent to the " + element_name + " element");
                }else {
                    Method method = AppiumBy.class.getMethod(android_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, android_locator);
                    driverAndroid.findElement(by).sendKeys(text);
                    TestLogger.addLogMessage("The text '" + text + "' has been sent to the " + element_name + " element");
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                screenshotAndroid(element_name, android_locator);
                Assert.fail("Error: " + e.getMessage());
            }
        }
        else if("ios".equalsIgnoreCase(getPlatform())){
            driverIos = IosSettings.driverIos.get();
            WebDriverWait wait = new WebDriverWait(driverIos, Duration.ofSeconds(40));
            try {
                if (Objects.equals(ios_selector, "xpath")) {
                    Method method = By.class.getMethod(ios_selector, String.class);
                    By by = (By) method.invoke(null, ios_locator);
                    driverIos.findElement(by).sendKeys(text);
                    TestLogger.addLogMessage("The text '" + text + "' has been sent to the " + element_name + " element");
                }else {
                    Method method = AppiumBy.class.getMethod(ios_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, ios_locator);
                    driverIos.findElement(by).sendKeys(text);
                    TestLogger.addLogMessage("The text '" + text + "' has been sent to the " + element_name + " element");
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                screenshotIos(element_name, ios_locator);
                Assert.fail("Error: " + e.getMessage());
            }
        }
        return null;
    }
    public UiObject waitUntil() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        if("android".equalsIgnoreCase(getPlatform())){
            driverAndroid = AndroidSettings.driverAndroid.get();
            WebDriverWait wait = new WebDriverWait(driverAndroid, Duration.ofSeconds(50));
            try {
                if (Objects.equals(android_selector, "xpath")) {
                    Method method = By.class.getMethod(android_selector, String.class);
                    By by = (By) method.invoke(null, android_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                }else {
                    Method method = AppiumBy.class.getMethod(android_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, android_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                screenshotAndroid(element_name, android_locator);
                Assert.fail("Error: " + e.getMessage());
            }
        }
        else if("ios".equalsIgnoreCase(getPlatform())){
            driverIos = IosSettings.driverIos.get();
            WebDriverWait wait = new WebDriverWait(driverIos, Duration.ofSeconds(50));
            try {
                if (Objects.equals(ios_selector, "xpath")) {
                    Method method = By.class.getMethod(ios_selector, String.class);
                    By by = (By) method.invoke(null, ios_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                }else {
                    Method method = AppiumBy.class.getMethod(ios_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, ios_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                screenshotIos(element_name, ios_locator);
                Assert.fail("Error: " + e.getMessage());
            }
        }
        return null;
    }
    public String getText() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        String text = null;
        if("android".equalsIgnoreCase(getPlatform())){
            driverAndroid = AndroidSettings.driverAndroid.get();
            try {
                if (Objects.equals(android_selector, "xpath")) {
                    Method method = By.class.getMethod(android_selector, String.class);
                    By by = (By) method.invoke(null, android_locator);
                    text = driverAndroid.findElement(by).getText();
                    TestLogger.addLogMessage("The text '" + text + "' has been extracted from the " + element_name + " element");
                }else {
                    Method method = AppiumBy.class.getMethod(android_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, android_locator);
                    text = driverAndroid.findElement(by).getText();
                    TestLogger.addLogMessage("The text '" + text + "' has been extracted from the " + element_name + " element");
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                boolean fail = !true;
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                screenshotAndroid(element_name, android_locator);
                assert  fail = false : "Error" + e;
            }
        }
        else if("ios".equalsIgnoreCase(getPlatform())){
            driverIos = IosSettings.driverIos.get();
            try {
                if (Objects.equals(ios_selector, "xpath")) {
                    Method method = By.class.getMethod(ios_selector, String.class);
                    By by = (By) method.invoke(null, ios_locator);
                    text = driverIos.findElement(by).getText();
                    TestLogger.addLogMessage("The text '" + text + "' has been extracted from the " + element_name + " element");
                }else {
                    Method method = AppiumBy.class.getMethod(ios_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, ios_locator);
                    text = driverIos.findElement(by).getText();
                    TestLogger.addLogMessage("The text '" + text + "' has been extracted from the " + element_name + " element");
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                boolean fail = !true;
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                screenshotIos(element_name,ios_locator);
                assert  fail = false : "Error" + e;
            }
        }
        return text;
    }
    public Boolean isEnabled(Integer waitTime) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Boolean is_enabled = null;
        if("android".equalsIgnoreCase(getPlatform())){
            driverAndroid = AndroidSettings.driverAndroid.get();
            WebDriverWait wait = new WebDriverWait(driverAndroid, Duration.ofSeconds(waitTime));
            try {
                if (Objects.equals(android_selector, "xpath")) {
                    Method method = By.class.getMethod(android_selector, String.class);
                    By by = (By) method.invoke(null, android_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    is_enabled = driverAndroid.findElement(by).isEnabled();
                    TestLogger.addLogMessage("Element " + element_name + " is enabeled");
                }else {
                    Method method = AppiumBy.class.getMethod(android_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, android_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    is_enabled = driverAndroid.findElement(by).isEnabled();
                    TestLogger.addLogMessage("Element " + element_name + " is enabeled");
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                screenshotAndroid(element_name, android_locator);
                Assert.fail("Error: " + e.getMessage());
            }
        }
        else if("ios".equalsIgnoreCase(getPlatform())){
            driverIos = IosSettings.driverIos.get();
            WebDriverWait wait = new WebDriverWait(driverIos, Duration.ofSeconds(waitTime));
            try {
                if (Objects.equals(ios_selector, "xpath")) {
                    Method method = By.class.getMethod(ios_selector, String.class);
                    By by = (By) method.invoke(null, ios_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    is_enabled = driverIos.findElement(by).isEnabled();
                    TestLogger.addLogMessage("Element " + element_name + " is enabeled");
                }else {
                    Method method = AppiumBy.class.getMethod(ios_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, ios_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    is_enabled = driverIos.findElement(by).isEnabled();
                    TestLogger.addLogMessage("Element " + element_name + " is enabeled");
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                screenshotIos(element_name, ios_locator);
                Assert.fail("Error: " + e.getMessage());
            }
        }
        return is_enabled;
    }
    public Boolean isElementWithIndexEnabled(Integer waitTime, Integer index) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Boolean is_enabled = null;
        if("android".equalsIgnoreCase(getPlatform())){
            driverAndroid = AndroidSettings.driverAndroid.get();
            WebDriverWait wait = new WebDriverWait(driverAndroid, Duration.ofSeconds(waitTime));
            try {
                if (Objects.equals(android_selector, "xpath")) {
                    Method method = By.class.getMethod(android_selector, String.class);
                    By by = (By) method.invoke(null, android_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    List<WebElement> mobileElements = driverAndroid.findElements(by);
                    WebElement mobileElement = mobileElements.get(index);
                    is_enabled = mobileElement.isEnabled();
                    TestLogger.addLogMessage("Element " + element_name + " is enabeled");
                }else {
                    Method method = AppiumBy.class.getMethod(android_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, android_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    List<WebElement> mobileElements = driverAndroid.findElements(by);
                    WebElement mobileElement = mobileElements.get(index);
                    is_enabled = mobileElement.isEnabled();
                    TestLogger.addLogMessage("Element " + element_name + " is enabeled");
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                boolean fail = !true;
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                screenshotAndroid(element_name, android_locator);
                assert  fail = false : "Error" + e;
            }
        }
        else if("ios".equalsIgnoreCase(getPlatform())){
            driverIos = IosSettings.driverIos.get();
            WebDriverWait wait = new WebDriverWait(driverIos, Duration.ofSeconds(waitTime));
            try {
                if (Objects.equals(ios_selector, "xpath")) {
                    Method method = By.class.getMethod(ios_selector, String.class);
                    By by = (By) method.invoke(null, ios_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    List<WebElement> mobileElements = driverAndroid.findElements(by);
                    WebElement mobileElement = mobileElements.get(index);
                    is_enabled = mobileElement.isEnabled();
                    TestLogger.addLogMessage("Element " + element_name + " is enabeled");
                }else {
                    Method method = AppiumBy.class.getMethod(ios_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, ios_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    List<WebElement> mobileElements = driverAndroid.findElements(by);
                    WebElement mobileElement = mobileElements.get(index);
                    is_enabled = mobileElement.isEnabled();
                    TestLogger.addLogMessage("Element " + element_name + " is enabeled");
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                boolean fail = !true;
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                screenshotIos(element_name,ios_locator);
                assert  fail = false : "Error" + e;
            }
        }
        return is_enabled;
    }
    public Boolean isDisplayed(Integer waitTime) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Boolean is_displayed = false;
        if("android".equalsIgnoreCase(getPlatform())){
            driverAndroid = AndroidSettings.driverAndroid.get();
            WebDriverWait wait = new WebDriverWait(driverAndroid, Duration.ofSeconds(waitTime));
            try {
                if (Objects.equals(android_selector, "xpath")) {
                    Method method = By.class.getMethod(android_selector, String.class);
                    By by = (By) method.invoke(null, android_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    is_displayed = driverAndroid.findElement(by).isDisplayed();
                    TestLogger.addLogMessage("Element " + element_name + " is displayed");
                }else {
                    Method method = AppiumBy.class.getMethod(android_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, android_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    is_displayed = driverAndroid.findElement(by).isDisplayed();
                    TestLogger.addLogMessage("Element " + element_name + " is displayed");
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                screenshotAndroid(element_name, android_locator);
                Assert.fail("Error: " + e.getMessage());
            }
        }
        else if("ios".equalsIgnoreCase(getPlatform())){
            driverIos = IosSettings.driverIos.get();
            WebDriverWait wait = new WebDriverWait(driverIos, Duration.ofSeconds(waitTime));
            try {
                if (Objects.equals(ios_selector, "xpath")) {
                    Method method = By.class.getMethod(ios_selector, String.class);
                    By by = (By) method.invoke(null, ios_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    is_displayed = driverIos.findElement(by).isDisplayed();
                    TestLogger.addLogMessage("Element " + element_name + " is displayed");
                }else {
                    Method method = AppiumBy.class.getMethod(ios_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, ios_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    is_displayed = driverIos.findElement(by).isDisplayed();
                    TestLogger.addLogMessage("Element " + element_name + " is displayed");
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                screenshotIos(element_name, ios_locator);
                Assert.fail("Error: " + e.getMessage());
            }
        }
        return is_displayed;
    }
    public Boolean isSelected(Integer waitTime) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Boolean is_selected = null;
        if("android".equalsIgnoreCase(getPlatform())){
            driverAndroid = AndroidSettings.driverAndroid.get();
            WebDriverWait wait = new WebDriverWait(driverAndroid, Duration.ofSeconds(waitTime));
            try {
                if (Objects.equals(android_selector, "xpath")) {
                    Method method = By.class.getMethod(android_selector, String.class);
                    By by = (By) method.invoke(null, android_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    is_selected = driverAndroid.findElement(by).isSelected();
                    TestLogger.addLogMessage("Element " + element_name + " is selected");
                }else {
                    Method method = AppiumBy.class.getMethod(android_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, android_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    is_selected = driverAndroid.findElement(by).isSelected();
                    TestLogger.addLogMessage("Element " + element_name + " is selected");
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                screenshotAndroid(element_name, android_locator);
                Assert.fail("Error: " + e.getMessage());
            }
        }
        else if("ios".equalsIgnoreCase(getPlatform())){
            driverIos = IosSettings.driverIos.get();
            WebDriverWait wait = new WebDriverWait(driverIos, Duration.ofSeconds(waitTime));
            try {
                if (Objects.equals(ios_selector, "xpath")) {
                    Method method = By.class.getMethod(ios_selector, String.class);
                    By by = (By) method.invoke(null, ios_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    is_selected = driverIos.findElement(by).isSelected();
                    TestLogger.addLogMessage("Element " + element_name + " is selected");
                }else {
                    Method method = AppiumBy.class.getMethod(ios_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, ios_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    is_selected = driverIos.findElement(by).isSelected();
                    TestLogger.addLogMessage("Element " + element_name + " is selected");
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                screenshotIos(element_name, ios_locator);
                Assert.fail("Error: " + e.getMessage());
            }
        }
        return is_selected;
    }
    public Boolean ifIsDisplayed(Integer waitTime) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Boolean is_displayed = true;
        if("android".equalsIgnoreCase(getPlatform())){
            driverAndroid = AndroidSettings.driverAndroid.get();
            WebDriverWait wait = new WebDriverWait(driverAndroid, Duration.ofSeconds(waitTime));
            try {
                if (Objects.equals(android_selector, "xpath")) {
                    Method method = By.class.getMethod(android_selector, String.class);
                    By by = (By) method.invoke(null, android_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    is_displayed = driverAndroid.findElement(by).isDisplayed();
                    TestLogger.addLogMessage("Element " + element_name + " is displayed");
                }else {
                    Method method = AppiumBy.class.getMethod(android_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, android_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    is_displayed = driverAndroid.findElement(by).isDisplayed();
                    TestLogger.addLogMessage("Element " + element_name + " is displayed");
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                is_displayed = false;
            }
        }
        else if("ios".equalsIgnoreCase(getPlatform())){
            driverIos = IosSettings.driverIos.get();
            WebDriverWait wait = new WebDriverWait(driverIos, Duration.ofSeconds(waitTime));
            try {
                if (Objects.equals(ios_selector, "xpath")) {
                    Method method = By.class.getMethod(ios_selector, String.class);
                    By by = (By) method.invoke(null, ios_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    is_displayed = driverIos.findElement(by).isDisplayed();
                    TestLogger.addLogMessage("Element " + element_name + " is displayed");
                }else {
                    Method method = AppiumBy.class.getMethod(ios_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, ios_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    is_displayed = driverIos.findElement(by).isDisplayed();
                    TestLogger.addLogMessage("Element " + element_name + " is displayed");
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                is_displayed = false;
            }
        }
        return is_displayed;
    }
    /** Metoda koja provjerava da li je element s
     * @param text vidljiv
     * Metoda prvo ceka dok element ne bude visible, zatim ga validira s isDisplayed
     * Ako element nije vidljiv metoda ne baca error i ne prekida test
     */
    public Boolean ifIsDisplayedWithText(Integer waitTime, String text) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Boolean is_displayed = true;
        if("android".equalsIgnoreCase(getPlatform())){
            driverAndroid = AndroidSettings.driverAndroid.get();
            WebDriverWait wait = new WebDriverWait(driverAndroid, Duration.ofSeconds(waitTime));
            String android_xpath_locator = "//" + android_locator + "[@text=\"" + text + "\"]";
            try {
                if (Objects.equals(android_selector, "xpath")) {
                    Method method = By.class.getMethod(android_selector, String.class);
                    By by = (By) method.invoke(null, android_xpath_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    is_displayed = driverAndroid.findElement(by).isDisplayed();
                    TestLogger.addLogMessage("Element " + element_name + " is displayed");
                }else {
                    Method method = AppiumBy.class.getMethod(android_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, text);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    is_displayed = driverAndroid.findElement(by).isDisplayed();
                    TestLogger.addLogMessage("Element " + element_name + " is displayed");
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                is_displayed = false;
            }
        }
        else if("ios".equalsIgnoreCase(getPlatform())){
            driverIos = IosSettings.driverIos.get();
            WebDriverWait wait = new WebDriverWait(driverIos, Duration.ofSeconds(waitTime));
            String ios_xpath_locator = null;
            if (Objects.equals(ios_selector, "xpath")) {
                ios_xpath_locator = "//" + ios_locator + "[@name=\"" + text + "\"]";
            }
            else if (Objects.equals(ios_selector, "iOSNsPredicateString")) {
                ios_xpath_locator = "type == \"" + ios_locator + "\" AND name CONTAINS \"" + text + "\"";
            }
            else if (Objects.equals(ios_selector, "iOSClassChain")) {
                ios_xpath_locator = "**/" + ios_locator+ "[`label CONTAINS \"" + text + "\" `]";
            }
            else if (Objects.equals(ios_selector, "accessibilityId")) {
                ios_xpath_locator = text;
            }
            try {
                if (Objects.equals(ios_selector, "xpath")) {
                    Method method = By.class.getMethod(ios_selector, String.class);
                    By by = (By) method.invoke(null, ios_xpath_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    is_displayed = driverIos.findElement(by).isDisplayed();
                    TestLogger.addLogMessage("Element " + element_name + " is displayed");
                }else {
                    Method method = AppiumBy.class.getMethod(ios_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, ios_xpath_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    is_displayed = driverIos.findElement(by).isDisplayed();
                    TestLogger.addLogMessage("Element " + element_name + " is displayed");
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                is_displayed = false;
            }
        }
        return is_displayed;
    }
    /** Metoda za slanje teksta elemenatu s čekanjem.
     * @param text je tekst koji zelimo input-at i on se salje metodi
     * Metoda prvo ceka dok element ne bude visible, zatim ga validira s isDisplayed
     * i nakon toga salje tekst sa send_keys
     */
    public UiObject sendTextWithWait(String text) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        if("android".equalsIgnoreCase(getPlatform())){
            driverAndroid = AndroidSettings.driverAndroid.get();
            WebDriverWait wait = new WebDriverWait(driverAndroid, Duration.ofSeconds(50));
            try {
                if (Objects.equals(android_selector, "xpath")) {
                    Method method = By.class.getMethod(android_selector, String.class);
                    By by = (By) method.invoke(null, android_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    driverAndroid.findElement(by).sendKeys(text);
                    TestLogger.addLogMessage("The text '" + text + "' has been sent to the " + element_name + " element");
                }
                else {
                    Method method = AppiumBy.class.getMethod(android_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, android_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    driverAndroid.findElement(by).sendKeys(text);
                    TestLogger.addLogMessage("The text '" + text + "' has been sent to the " + element_name + " element");
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                screenshotAndroid(element_name, android_locator);
                Assert.fail("Error: " + e.getMessage());
            }
        }
        else if("ios".equalsIgnoreCase(getPlatform())){
            driverIos = IosSettings.driverIos.get();
            WebDriverWait wait = new WebDriverWait(driverIos, Duration.ofSeconds(50));
            try {
                if (Objects.equals(ios_selector, "xpath")) {
                    Method method = By.class.getMethod(ios_selector, String.class);
                    By by = (By) method.invoke(null, ios_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    driverIos.findElement(by).sendKeys(text);
                    TestLogger.addLogMessage("The text '" + text + "' has been sent to the " + element_name + " element");
                }
                else {
                    Method method = AppiumBy.class.getMethod(ios_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, ios_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    driverIos.findElement(by).sendKeys(text);
                    TestLogger.addLogMessage("The text '" + text + "' has been sent to the " + element_name + " element");
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                screenshotIos(element_name, ios_locator);
                Assert.fail("Error: " + e.getMessage());
            }
        }
        return null;
    }
    public UiObject clearTextWithWait() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        if("android".equalsIgnoreCase(getPlatform())){
            driverAndroid = AndroidSettings.driverAndroid.get();
            WebDriverWait wait = new WebDriverWait(driverAndroid, Duration.ofSeconds(50));
            try {
                if (Objects.equals(android_selector, "xpath")) {
                    Method method = By.class.getMethod(android_selector, String.class);
                    By by = (By) method.invoke(null, android_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    driverAndroid.findElement(by).clear();
                    TestLogger.addLogMessage("The text has been cleared from the " + element_name + " element");
                }
                else {
                    Method method = AppiumBy.class.getMethod(android_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, android_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    driverAndroid.findElement(by).clear();
                    TestLogger.addLogMessage("The text has been cleared from the " + element_name + " element");
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                screenshotAndroid(element_name, android_locator);
                Assert.fail("Error: " + e.getMessage());
            }
        }
        else if("ios".equalsIgnoreCase(getPlatform())){
            driverIos = IosSettings.driverIos.get();
            WebDriverWait wait = new WebDriverWait(driverIos, Duration.ofSeconds(50));
            try {
                if (Objects.equals(ios_selector, "xpath")) {
                    Method method = By.class.getMethod(ios_selector, String.class);
                    By by = (By) method.invoke(null, ios_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    driverIos.findElement(by).clear();
                    TestLogger.addLogMessage("The text has been cleared from the " + element_name + " element");
                }
                else {
                    Method method = AppiumBy.class.getMethod(ios_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, ios_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    driverIos.findElement(by).clear();
                    TestLogger.addLogMessage("The text has been cleared from the " + element_name + " element");
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                screenshotIos(element_name, ios_locator);
                Assert.fail("Error: " + e.getMessage());
            }
        }
        return null;
    }
    public UiObject sendEnterWithWait() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        if("android".equalsIgnoreCase(getPlatform())){
            driverAndroid = AndroidSettings.driverAndroid.get();
            WebDriverWait wait = new WebDriverWait(driverAndroid, Duration.ofSeconds(50));
            try {
                if (Objects.equals(android_selector, "xpath")) {
                    Method method = By.class.getMethod(android_selector, String.class);
                    By by = (By) method.invoke(null, android_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    driverAndroid.findElement(by).sendKeys(Keys.RETURN);
                    TestLogger.addLogMessage("Enter has been sent to the " + element_name + " element");
                }
                else {
                    Method method = AppiumBy.class.getMethod(android_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, android_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    driverAndroid.findElement(by).sendKeys(Keys.RETURN);
                    TestLogger.addLogMessage("Enter has been sent to the " + element_name + " element");
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                screenshotAndroid(element_name, android_locator);
                Assert.fail("Error: " + e.getMessage());
            }
        }
        else if("ios".equalsIgnoreCase(getPlatform())){
            driverIos = IosSettings.driverIos.get();
            WebDriverWait wait = new WebDriverWait(driverIos, Duration.ofSeconds(50));
            try {
                if (Objects.equals(ios_selector, "xpath")) {
                    Method method = By.class.getMethod(ios_selector, String.class);
                    By by = (By) method.invoke(null, ios_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    driverIos.findElement(by).sendKeys(Keys.RETURN);
                    TestLogger.addLogMessage("Enter has been sent to the " + element_name + " element");
                }
                else {
                    Method method = AppiumBy.class.getMethod(ios_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, ios_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    driverIos.findElement(by).sendKeys(Keys.RETURN);
                    TestLogger.addLogMessage("Enter has been sent to the " + element_name + " element");
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                screenshotIos(element_name, ios_locator);
                Assert.fail("Error: " + e.getMessage());
            }
        }
        return null;
    }
    /** Metoda za izvlacenje tekst iz elemenata s čekanjem.
     * Metoda prvo ceka dok element ne bude visible, zatim ga validira s isDisplayed
     * i nakon toga ga izvlaci tekst is sprema ga u varijablu
     * @param text.
     */
    public String getTextWithWait() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        String text = null;
        if("android".equalsIgnoreCase(getPlatform())){
            driverAndroid = AndroidSettings.driverAndroid.get();
            WebDriverWait wait = new WebDriverWait(driverAndroid, Duration.ofSeconds(50));
            try {
                if (Objects.equals(android_selector, "xpath")) {
                    Method method = By.class.getMethod(android_selector, String.class);
                    By by = (By) method.invoke(null, android_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    text = driverAndroid.findElement(by).getText();
                    TestLogger.addLogMessage("The text '" + text + "' has been extracted from the " + element_name + " element");
                }
                else {
                    Method method = AppiumBy.class.getMethod(android_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, android_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    text = driverAndroid.findElement(by).getText();
                    TestLogger.addLogMessage("The text '" + text + "' has been extracted from the " + element_name + " element");
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                screenshotAndroid(element_name, android_locator);
                Assert.fail("Error: " + e.getMessage());
            }
        }
        else if("ios".equalsIgnoreCase(getPlatform())){
            driverIos = IosSettings.driverIos.get();
            WebDriverWait wait = new WebDriverWait(driverIos, Duration.ofSeconds(50));
            try {
                if (Objects.equals(ios_selector, "xpath")) {
                    Method method = By.class.getMethod(ios_selector, String.class);
                    By by = (By) method.invoke(null, ios_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    text = driverIos.findElement(by).getText();
                    TestLogger.addLogMessage("The text '" + text + "' has been extracted from the " + element_name + " element");
                }
                else {
                    Method method = AppiumBy.class.getMethod(ios_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, ios_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    text = driverIos.findElement(by).getText();
                    TestLogger.addLogMessage("The text '" + text + "' has been extracted from the " + element_name + " element");
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                screenshotIos(element_name, ios_locator);
                Assert.fail("Error: " + e.getMessage());
            }
        }
        return text;
    }
    /** Metoda za klikanje elemenata s čekanjem.
     * Metoda prvo ceka dok element ne bude visible, zatim ga validira s isDisplayed
     * i nakon toga ga klika.
     */
    public UiObject clickWithWait() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        if("android".equalsIgnoreCase(getPlatform())){
            driverAndroid = AndroidSettings.driverAndroid.get();
            WebDriverWait wait = new WebDriverWait(driverAndroid, Duration.ofSeconds(50));
            try {
                if (Objects.equals(android_selector, "xpath")) {
                    Method method = By.class.getMethod(android_selector, String.class);
                    By by = (By) method.invoke(null, android_locator);
                    wait.until(ExpectedConditions.elementToBeClickable(by));
                    driverAndroid.findElement(by).click();
                    TestLogger.addLogMessage("Element " + element_name + " is clicked");
                }
                else {
                    Method method = AppiumBy.class.getMethod(android_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, android_locator);
                    wait.until(ExpectedConditions.elementToBeClickable(by));
                    driverAndroid.findElement(by).click();
                    TestLogger.addLogMessage("Element " + element_name + " is clicked");
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                screenshotAndroid(element_name, android_locator);
                Assert.fail("Error: " + e.getMessage());
            }
        }
        else if("ios".equalsIgnoreCase(getPlatform())){
            driverIos = IosSettings.driverIos.get();
            WebDriverWait wait = new WebDriverWait(driverIos, Duration.ofSeconds(50));
            try {
                if (Objects.equals(ios_selector, "xpath")) {
                    Method method = By.class.getMethod(ios_selector, String.class);
                    By by = (By) method.invoke(null, ios_locator);
                    wait.until(ExpectedConditions.elementToBeClickable(by));
                    driverIos.findElement(by).click();
                    TestLogger.addLogMessage("Element " + element_name + " is clicked");
                }
                else {
                    Method method = AppiumBy.class.getMethod(ios_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, ios_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    driverIos.findElement(by).click();
                    TestLogger.addLogMessage("Element " + element_name + " is clicked");
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                screenshotIos(element_name, ios_locator);
                Assert.fail("Error: " + e.getMessage());
            }
        }
        return null;
    }
    /** Metoda za izvlacenje lokacije koja se sprema u varijablu.
     * @param location varijabla koja sadrži koordinate elementa
     * na ekranu.
    */
    public Point getLocation() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Point location = null;
        if("android".equalsIgnoreCase(getPlatform())){
            driverAndroid = AndroidSettings.driverAndroid.get();
            WebDriverWait wait = new WebDriverWait(driverAndroid, Duration.ofSeconds(40));
            try {
                if (Objects.equals(android_selector, "xpath")) {
                    Method method = By.class.getMethod(android_selector, String.class);
                    By by = (By) method.invoke(null, android_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    location = driverAndroid.findElement(by).getLocation();
                }else {
                    Method method = AppiumBy.class.getMethod(android_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, android_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    location = driverAndroid.findElement(by).getLocation();
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                screenshotAndroid(element_name, android_locator);
                Assert.fail("Error: " + e.getMessage());
            }
        }
        else if("ios".equalsIgnoreCase(getPlatform())){
            driverIos = IosSettings.driverIos.get();
            WebDriverWait wait = new WebDriverWait(driverIos, Duration.ofSeconds(40));
            try {
                if (Objects.equals(ios_selector, "xpath")) {
                    Method method = By.class.getMethod(ios_selector, String.class);
                    By by = (By) method.invoke(null, ios_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    location = driverIos.findElement(by).getLocation();
                }else {
                    Method method = AppiumBy.class.getMethod(ios_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, ios_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    location = driverIos.findElement(by).getLocation();
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                screenshotIos(element_name, ios_locator);
                Assert.fail("Error: " + e.getMessage());
            }
        }
        return location;
    }
    /** Metoda za slanje tekst child elementu unutar parent elementa.
     * Kreira se UiObject na parent elementu i kao parametar metodi
     * se predaje ime child elementa kojem saljemo tekst.
     * Metoda traži i šalje tekst child elementu trazeci ga unutar parent elementa
     * @param child ime child elementa iz element.json tablice
    */
    public UiObject sendTextInsideWithWait(UiObject child, String text) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        this.child_android_selector = child.android_selector;
        this.child_android_locator = child.android_locator;

        this.child_ios_selector = child.ios_selector;
        this.child_ios_locator = child.ios_locator;
        if("android".equalsIgnoreCase(getPlatform())){
            driverAndroid = AndroidSettings.driverAndroid.get();
            WebDriverWait wait = new WebDriverWait(driverAndroid, Duration.ofSeconds(50));
            try {
                if (Objects.equals(child_android_selector, "xpath")) {
                    Method parent_method = By.class.getMethod(android_selector, String.class);
                    By parent_by = (By) parent_method.invoke(null, android_locator);
                    Method child_method = By.class.getMethod(child_android_selector, String.class);
                    By child_by = (By) child_method.invoke(null, child_android_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(parent_by));
                    WebElement parent_element = (WebElement) driverAndroid.findElements(parent_by);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(child_by));
                    parent_element.findElement(child_by).sendKeys(text);
                    TestLogger.addLogMessage("The text '" + text + "' has been sent to the " + child + " element");
                }
                else {
                    Method parent_method = AppiumBy.class.getMethod(android_selector, String.class);
                    AppiumBy parent_by = (AppiumBy) parent_method.invoke(null, android_locator);
                    Method child_method = AppiumBy.class.getMethod(child_android_selector, String.class);
                    AppiumBy child_by = (AppiumBy) child_method.invoke(null, child_android_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(parent_by));
                    WebElement parent_element = (WebElement) driverAndroid.findElements(parent_by);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(child_by));
                    parent_element.findElement(child_by).sendKeys(text);
                    TestLogger.addLogMessage("The text '" + text + "' has been sent to the " + child + " element");
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                boolean fail = !true;
                TestLogger.addLogMessage("Element '" + child + "' is not found");
                screenshotAndroid(element_name, android_locator);
                assert  fail = false : "Error" + e;
            }
        }
        else if("ios".equalsIgnoreCase(getPlatform())){
            driverIos = IosSettings.driverIos.get();
            WebDriverWait wait = new WebDriverWait(driverIos, Duration.ofSeconds(50));
            try {
                if (Objects.equals(child_ios_selector, "xpath")) {
                    Method parent_method = By.class.getMethod(ios_selector, String.class);
                    By parent_by = (By) parent_method.invoke(null, ios_locator);
                    Method child_method = By.class.getMethod(child_ios_selector, String.class);
                    By child_by = (By) child_method.invoke(null, child_ios_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(parent_by));
                    WebElement parent_element = (WebElement) driverIos.findElements(parent_by);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(child_by));
                    parent_element.findElement(child_by).sendKeys(text);
                    TestLogger.addLogMessage("The text '" + text + "' has been sent to the " + child + " element");
                }
                else {
                    Method parent_method = AppiumBy.class.getMethod(ios_selector, String.class);
                    AppiumBy parent_by = (AppiumBy) parent_method.invoke(null, ios_locator);
                    Method child_method = AppiumBy.class.getMethod(child_ios_selector, String.class);
                    AppiumBy child_by = (AppiumBy) child_method.invoke(null, child_ios_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(parent_by));
                    WebElement parent_element = (WebElement) driverIos.findElements(parent_by);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(child_by));
                    parent_element.findElement(child_by).sendKeys(text);
                    TestLogger.addLogMessage("The text '" + text + "' has been sent to the " + child + " element");
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                boolean fail = !true;
                TestLogger.addLogMessage("Element '" + child + "' is not found");
                screenshotIos(element_name,ios_locator);
                assert  fail = false : "Error" + e;
            }
        }
        return null;
    }
    /** Metoda za izvlacenje teksta child elementa unutar parent elementa.
     * Kreira se UiObject na parent elementu i kao parametar metodi
     * se predaje ime child elementa od kojeg zelimo izvuci tekst.
     * Metoda traži i izvlaci tekst child elementa trazeci ga unutar parent elementa
     * @param child ime child elementa iz element.json tablice
     * @param text tekst koji se nalazi u traženom child elementu
     */
    public String getTextInsideWithWait(UiObject child) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        String text = null;
        this.child_android_selector = child.android_selector;
        this.child_android_locator = child.android_locator;

        this.child_ios_selector = child.ios_selector;
        this.child_ios_locator = child.ios_locator;
        if("android".equalsIgnoreCase(getPlatform())){
            driverAndroid = AndroidSettings.driverAndroid.get();
            WebDriverWait wait = new WebDriverWait(driverAndroid, Duration.ofSeconds(50));
            try {
                if (Objects.equals(child_android_selector, "xpath")) {
                    Method parent_method = By.class.getMethod(android_selector, String.class);
                    By parent_by = (By) parent_method.invoke(null, android_locator);
                    Method child_method = By.class.getMethod(child_android_selector, String.class);
                    By child_by = (By) child_method.invoke(null, child_android_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(parent_by));
                    WebElement parent_element = (WebElement) driverAndroid.findElements(parent_by);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(child_by));
                    text = parent_element.findElement(child_by).getText();
                    TestLogger.addLogMessage("The text '" + text + "' has been extracted from the " + child + " element");
                }
                else {
                    Method parent_method = AppiumBy.class.getMethod(android_selector, String.class);
                    AppiumBy parent_by = (AppiumBy) parent_method.invoke(null, android_locator);
                    Method child_method = AppiumBy.class.getMethod(child_android_selector, String.class);
                    AppiumBy child_by = (AppiumBy) child_method.invoke(null, child_android_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(parent_by));
                    WebElement parent_element = (WebElement) driverAndroid.findElements(parent_by);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(child_by));
                    text = parent_element.findElement(child_by).getText();
                    TestLogger.addLogMessage("The text '" + text + "' has been extracted from the " + child + " element");
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                boolean fail = !true;
                TestLogger.addLogMessage("Element '" + child + "' is not found");
                screenshotAndroid(element_name, android_locator);
                assert  fail = false : "Error" + e;
            }
        }
        else if("ios".equalsIgnoreCase(getPlatform())){
            driverIos = IosSettings.driverIos.get();
            WebDriverWait wait = new WebDriverWait(driverIos, Duration.ofSeconds(50));
            try {
                if (Objects.equals(child_ios_selector, "xpath")) {
                    Method parent_method = By.class.getMethod(ios_selector, String.class);
                    By parent_by = (By) parent_method.invoke(null, ios_locator);
                    Method child_method = By.class.getMethod(child_ios_selector, String.class);
                    By child_by = (By) child_method.invoke(null, child_ios_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(parent_by));
                    WebElement parent_element = (WebElement) driverIos.findElements(parent_by);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(child_by));
                    text = parent_element.findElement(child_by).getText();
                    TestLogger.addLogMessage("The text '" + text + "' has been extracted from the " + child + " element");
                }
                else {
                    Method parent_method = AppiumBy.class.getMethod(ios_selector, String.class);
                    AppiumBy parent_by = (AppiumBy) parent_method.invoke(null, ios_locator);
                    Method child_method = AppiumBy.class.getMethod(child_ios_selector, String.class);
                    AppiumBy child_by = (AppiumBy) child_method.invoke(null, child_ios_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(parent_by));
                    WebElement parent_element = (WebElement) driverIos.findElements(parent_by);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(child_by));
                    text = parent_element.findElement(child_by).getText();
                    TestLogger.addLogMessage("The text '" + text + "' has been extracted from the " + child + " element");
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                boolean fail = !true;
                TestLogger.addLogMessage("Element '" + child + "' is not found");
                screenshotIos(element_name,ios_locator);
                assert  fail = false : "Error" + e;
            }
        }
        return text;
    }
    /** Metoda za klik na child element unutar parent elementa.
     * Kreira se UiObject na parent elementu i kao parametar metodi
     * se predaje ime child elementa koji zelimo is clicked.
     * Metoda traži i klika na child element trazeci ga unutar parent elementa
     */
    public UiObject clickInsideWithWait(UiObject child) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        this.child_android_selector = child.android_selector;
        this.child_android_locator = child.android_locator;

        this.child_ios_selector = child.ios_selector;
        this.child_ios_locator = child.ios_locator;
        if("android".equalsIgnoreCase(getPlatform())){
            driverAndroid = AndroidSettings.driverAndroid.get();
            WebDriverWait wait = new WebDriverWait(driverAndroid, Duration.ofSeconds(50));
            try {
                if (Objects.equals(child_android_selector, "xpath")) {
                    Method parent_method = By.class.getMethod(android_selector, String.class);
                    By parent_by = (By) parent_method.invoke(null, android_locator);
                    Method child_method = By.class.getMethod(child_android_selector, String.class);
                    By child_by = (By) child_method.invoke(null, child_android_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(parent_by));
                    WebElement parent_element = (WebElement) driverAndroid.findElement(parent_by);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(child_by));
                    parent_element.findElement(child_by).click();
                    TestLogger.addLogMessage("Element '" + child + "' is clicked");
                }
                else {
                    Method parent_method = AppiumBy.class.getMethod(android_selector, String.class);
                    AppiumBy parent_by = (AppiumBy) parent_method.invoke(null, android_locator);
                    Method child_method = AppiumBy.class.getMethod(child_android_selector, String.class);
                    AppiumBy child_by = (AppiumBy) child_method.invoke(null, child_android_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(parent_by));
                    WebElement parent_element = (WebElement) driverAndroid.findElement(parent_by);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(child_by));
                    parent_element.findElement(child_by).click();
                    TestLogger.addLogMessage("Element '" + child + "' is clicked");
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                boolean fail = !true;
                TestLogger.addLogMessage("Element '" + child + "' is not found");
                screenshotAndroid(element_name, android_locator);
                assert  fail = false : "Error" + e;
            }
        }
        else if("ios".equalsIgnoreCase(getPlatform())){
            driverIos = IosSettings.driverIos.get();
            WebDriverWait wait = new WebDriverWait(driverAndroid, Duration.ofSeconds(50));
            try {
                if (Objects.equals(ios_selector, "xpath")) {
                    Method parent_method = By.class.getMethod(ios_selector, String.class);
                    By parent_by = (By) parent_method.invoke(null, ios_locator);
                    Method child_method = By.class.getMethod(child_ios_selector, String.class);
                    By child_by = (By) child_method.invoke(null, child_ios_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(parent_by));
                    WebElement parent_element = (WebElement) driverIos.findElements(parent_by);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(child_by));
                    parent_element.findElement(child_by).click();
                    TestLogger.addLogMessage("Element '" + child + "' is clicked");
                }
                else {
                    Method parent_method = AppiumBy.class.getMethod(ios_selector, String.class);
                    AppiumBy parent_by = (AppiumBy) parent_method.invoke(null, ios_locator);
                    Method child_method = AppiumBy.class.getMethod(child_ios_selector, String.class);
                    AppiumBy child_by = (AppiumBy) child_method.invoke(null, child_ios_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(parent_by));
                    WebElement parent_element = (WebElement) driverIos.findElement(parent_by);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(child_by));
                    parent_element.findElement(child_by).click();
                    TestLogger.addLogMessage("Element '" + child + "' is clicked");
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                boolean fail = !true;
                TestLogger.addLogMessage("Element '" + child + "' is not found");
                screenshotIos(element_name,ios_locator);
                assert  fail = false : "Error" + e;
            }
        }
        return null;
    }
    /** Metoda za scroll do elementa. Koristi se na ekranima na kojima možemo
     * pretpostaviti da ce se element koji tražimo nalaziti izvan vidljivosti na
     * ekranu. Metoda traži element sa isDisplayed i ako ga ne nađe scrolla dolje
     * i trazi opet element na ekranu s isDisplayed. Kada isDisplayed bude true for petlja se prekida.
     */
    public UiObject scrollToElement() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final int MAX_SCROLL_ATTEMPTS = 12;
        int attempt = 0;
        boolean is_displayed = false;

        while (!is_displayed && attempt < MAX_SCROLL_ATTEMPTS) {
            try {
                if ("android".equalsIgnoreCase(getPlatform())) {
                    driverAndroid = AndroidSettings.driverAndroid.get();
                    WebDriverWait wait = new WebDriverWait(driverAndroid, Duration.ofSeconds(5));
                    if (Objects.equals(android_selector, "xpath")) {
                        Method method = By.class.getMethod(android_selector, String.class);
                        By by = (By) method.invoke(null, android_locator);
                        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                        is_displayed = driverAndroid.findElement(by).isDisplayed();
                    } else {
                        Method method = AppiumBy.class.getMethod(android_selector, String.class);
                        AppiumBy by = (AppiumBy) method.invoke(null, android_locator);
                        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                        is_displayed = driverAndroid.findElement(by).isDisplayed();
                    }
                } else if ("ios".equalsIgnoreCase(getPlatform())) {
                    driverIos = IosSettings.driverIos.get();
                    WebDriverWait wait = new WebDriverWait(driverIos, Duration.ofSeconds(5));
                    if (Objects.equals(ios_selector, "xpath")) {
                        Method method = By.class.getMethod(ios_selector, String.class);
                        By by = (By) method.invoke(null, ios_locator);
                        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                        is_displayed = driverIos.findElement(by).isDisplayed();
                    } else {
                        Method method = AppiumBy.class.getMethod(ios_selector, String.class);
                        AppiumBy by = (AppiumBy) method.invoke(null, ios_locator);
                        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                        is_displayed = driverIos.findElement(by).isDisplayed();
                    }
                }
            } catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                attempt++; // Increment the scroll attempt
                if (attempt >= MAX_SCROLL_ATTEMPTS) {
                    throw new NoSuchElementException("Element not found after " + MAX_SCROLL_ATTEMPTS + " scroll attempts");
                }

                // Perform scroll
                if ("android".equalsIgnoreCase(getPlatform())) {
                    Dimension screen_size = driverAndroid.manage().window().getSize();
                    PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
                    Sequence sequence = new Sequence(finger, 1);
                    sequence.addAction(finger.createPointerMove(ofMillis(0),
                            PointerInput.Origin.viewport(), screen_size.width / 2, screen_size.height * 8 / 10));
                    sequence.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
                    sequence.addAction(new Pause(finger, ofMillis(600)));
                    sequence.addAction(finger.createPointerMove(ofMillis(600),
                            PointerInput.Origin.viewport(), screen_size.width / 2, screen_size.height / 5));
                    sequence.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
                    driverAndroid.perform(singletonList(sequence));
                } else if ("ios".equalsIgnoreCase(getPlatform())) {
                    Dimension screen_size = driverIos.manage().window().getSize();
                    PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
                    Sequence sequence = new Sequence(finger, 1);
                    sequence.addAction(finger.createPointerMove(ofMillis(0),
                            PointerInput.Origin.viewport(), screen_size.width / 2, screen_size.height * 8 / 10));
                    sequence.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
                    sequence.addAction(new Pause(finger, ofMillis(600)));
                    sequence.addAction(finger.createPointerMove(ofMillis(600),
                            PointerInput.Origin.viewport(), screen_size.width / 2, screen_size.height / 5));
                    sequence.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
                    driverIos.perform(singletonList(sequence));
                }
            }
        }

        return null; // Optionally return the element instead of null if needed
    }
    public UiObject scrollToElementWithText(String text) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final int MAX_SCROLL_ATTEMPTS = 12;
        int attempt = 0;
        boolean is_displayed = false;

        while (!is_displayed && attempt < MAX_SCROLL_ATTEMPTS) {
            try {
                if ("android".equalsIgnoreCase(getPlatform())) {
                    driverAndroid = AndroidSettings.driverAndroid.get();
                    WebDriverWait wait = new WebDriverWait(driverAndroid, Duration.ofSeconds(5));

                    if (!Objects.equals(text, "noTextSearch")) {
                        if (Objects.equals(android_selector, "xpath")) {
                            String android_xpath_locator = "(//" + android_locator + "[contains(@text, \"" + text + "\")])";
                            Method method = By.class.getMethod(android_selector, String.class);
                            By by = (By) method.invoke(null, android_xpath_locator);
                            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                            is_displayed = driverAndroid.findElement(by).isDisplayed();
                        } else {
                            Method method = AppiumBy.class.getMethod(android_selector, String.class);
                            AppiumBy by = (AppiumBy) method.invoke(null, text);
                            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                            is_displayed = driverAndroid.findElement(by).isDisplayed();
                        }
                    } else {
                        if (Objects.equals(android_selector, "xpath")) {
                            Method method = By.class.getMethod(android_selector, String.class);
                            By by = (By) method.invoke(null, android_locator);
                            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                            is_displayed = driverAndroid.findElement(by).isDisplayed();
                        } else {
                            Method method = AppiumBy.class.getMethod(android_selector, String.class);
                            AppiumBy by = (AppiumBy) method.invoke(null, android_locator);
                            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                            is_displayed = driverAndroid.findElement(by).isDisplayed();
                        }
                    }
                } else if ("ios".equalsIgnoreCase(getPlatform())) {
                    driverIos = IosSettings.driverIos.get();
                    WebDriverWait wait = new WebDriverWait(driverIos, Duration.ofSeconds(5));

                    String ios_xpath_locator = null;

                    if (!Objects.equals(text, "noTextSearch")) {
                        if (Objects.equals(ios_selector, "xpath")) {
                            ios_xpath_locator = "//" + ios_locator + "[@name=\"" + text + "\"]";
                        } else if (Objects.equals(ios_selector, "iOSNsPredicateString")) {
                            ios_xpath_locator = "type == \"" + ios_locator + "\" AND name CONTAINS \"" + text + "\"";
                        } else if (Objects.equals(ios_selector, "iOSClassChain")) {
                            ios_xpath_locator = "**/" + ios_locator + "[`label CONTAINS \"" + text + "\"`]";
                        } else if (Objects.equals(ios_selector, "accessibilityId")) {
                            ios_xpath_locator = text;
                        }

                        if (Objects.equals(ios_selector, "xpath")) {
                            Method method = By.class.getMethod(ios_selector, String.class);
                            By by = (By) method.invoke(null, ios_xpath_locator);
                            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                            is_displayed = driverIos.findElement(by).isDisplayed();
                        } else {
                            Method method = AppiumBy.class.getMethod(ios_selector, String.class);
                            AppiumBy by = (AppiumBy) method.invoke(null, ios_xpath_locator);
                            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                            is_displayed = driverIos.findElement(by).isDisplayed();
                        }
                    } else {
                        if (Objects.equals(ios_selector, "xpath")) {
                            Method method = By.class.getMethod(ios_selector, String.class);
                            By by = (By) method.invoke(null, ios_locator);
                            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                            is_displayed = driverIos.findElement(by).isDisplayed();
                        } else {
                            Method method = AppiumBy.class.getMethod(ios_selector, String.class);
                            AppiumBy by = (AppiumBy) method.invoke(null, ios_locator);
                            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                            is_displayed = driverIos.findElement(by).isDisplayed();
                        }
                    }
                }
            } catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                attempt++;
                if (attempt >= MAX_SCROLL_ATTEMPTS) {
                    throw new NoSuchElementException("Element with text \"" + text + "\" not found after " + MAX_SCROLL_ATTEMPTS + " scroll attempts");
                }

                // Perform scroll
                if ("android".equalsIgnoreCase(getPlatform())) {
                    Dimension screen_size = driverAndroid.manage().window().getSize();
                    PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
                    Sequence sequence = new Sequence(finger, 1);
                    sequence.addAction(finger.createPointerMove(ofMillis(0),
                            PointerInput.Origin.viewport(), screen_size.width / 2, screen_size.height * 8 / 10));
                    sequence.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
                    sequence.addAction(new Pause(finger, ofMillis(600)));
                    sequence.addAction(finger.createPointerMove(ofMillis(600),
                            PointerInput.Origin.viewport(), screen_size.width / 2, screen_size.height / 5));
                    sequence.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
                    driverAndroid.perform(singletonList(sequence));
                } else if ("ios".equalsIgnoreCase(getPlatform())) {
                    Dimension screen_size = driverIos.manage().window().getSize();
                    PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
                    Sequence sequence = new Sequence(finger, 1);
                    sequence.addAction(finger.createPointerMove(ofMillis(0),
                            PointerInput.Origin.viewport(), screen_size.width / 2, screen_size.height * 8 / 10));
                    sequence.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
                    sequence.addAction(new Pause(finger, ofMillis(600)));
                    sequence.addAction(finger.createPointerMove(ofMillis(600),
                            PointerInput.Origin.viewport(), screen_size.width / 2, screen_size.height / 5));
                    sequence.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
                    driverIos.perform(singletonList(sequence));
                }
            }
        }

        return null;
    }
    public UiObject scrollUpToElement(String text) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final int MAX_SCROLL_ATTEMPTS = 12;
        int attempt = 0;
        Boolean is_displayed = false;
        while (!is_displayed && attempt < MAX_SCROLL_ATTEMPTS) {
            try {
                if("android".equalsIgnoreCase(getPlatform())){
                    driverAndroid = AndroidSettings.driverAndroid.get();
                    WebDriverWait wait = new WebDriverWait(driverAndroid, Duration.ofSeconds(5));
                    if (!Objects.equals(text, "noTextSearch")) {
                        if (Objects.equals(android_selector, "xpath")) {
                            String android_xpath_locator = "//" + android_locator + "[@text=\"" + text + "\"]";
                            Method method = By.class.getMethod(android_selector, String.class);
                            By by = (By) method.invoke(null, android_xpath_locator);
                            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                            is_displayed = driverAndroid.findElement(by).isDisplayed();
                        }else {
                            Method method = AppiumBy.class.getMethod(android_selector, String.class);
                            AppiumBy by = (AppiumBy) method.invoke(null, text);
                            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                            is_displayed = driverAndroid.findElement(by).isDisplayed();
                        }
                    }
                    else {
                        if (Objects.equals(android_selector, "xpath")) {
                            Method method = By.class.getMethod(android_selector, String.class);
                            By by = (By) method.invoke(null, android_locator);
                            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                            is_displayed = driverAndroid.findElement(by).isDisplayed();
                        }else {
                            Method method = AppiumBy.class.getMethod(android_selector, String.class);
                            AppiumBy by = (AppiumBy) method.invoke(null, android_locator);
                            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                            is_displayed = driverAndroid.findElement(by).isDisplayed();
                        }
                    }
                }
                else if("ios".equalsIgnoreCase(getPlatform())){
                    driverIos = IosSettings.driverIos.get();
                    WebDriverWait wait = new WebDriverWait(driverIos, Duration.ofSeconds(5));
                    if (!Objects.equals(text, "noTextSearch")) {
                        String ios_xpath_locator = null;
                        if (Objects.equals(ios_selector, "xpath")) {
                            ios_xpath_locator = "//" + ios_locator + "[contains(text()," + text + ")]";
                        }
                        else if (Objects.equals(ios_selector, "iOSNsPredicateString")) {
                            ios_xpath_locator = "type == '" + ios_locator + "' AND name CONTAINS '" + text + "'";
                        }
                        else if (Objects.equals(ios_selector, "iOSClassChain")) {
                            ios_xpath_locator = "**/" + ios_locator+ "[`label CONTAINS \"" + text + "\" `]";
                        }
                        else if (Objects.equals(ios_selector, "accessibilityId")) {
                            ios_xpath_locator = text;
                        }
                        if (Objects.equals(ios_selector, "xpath")) {
                            Method method = By.class.getMethod(ios_selector, String.class);
                            By by = (By) method.invoke(null, ios_xpath_locator);
                            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                            is_displayed = driverIos.findElement(by).isEnabled();
                        }else {
                            Method method = AppiumBy.class.getMethod(ios_selector, String.class);
                            AppiumBy by = (AppiumBy) method.invoke(null, ios_xpath_locator);
                            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                            is_displayed = driverIos.findElement(by).isEnabled();
                        }
                    }
                    else {
                        if (Objects.equals(ios_selector, "xpath")) {
                            Method method = By.class.getMethod(ios_selector, String.class);
                            By by = (By) method.invoke(null, ios_locator);
                            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                            is_displayed = driverIos.findElement(by).isEnabled();
                        }else {
                            Method method = AppiumBy.class.getMethod(ios_selector, String.class);
                            AppiumBy by = (AppiumBy) method.invoke(null, ios_locator);
                            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                            is_displayed = driverIos.findElement(by).isEnabled();
                        }
                    }
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                attempt++;
                if (attempt >= MAX_SCROLL_ATTEMPTS) {
                    throw new NoSuchElementException("Element with text '" + text + "' not found after " + MAX_SCROLL_ATTEMPTS + " scroll attempts");
                }
                if ("android".equalsIgnoreCase(getPlatform())) {
                    Dimension screen_size = driverAndroid.manage().window().getSize();
                    PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
                    Sequence sequence = new Sequence(finger, 1);
                    sequence.addAction(finger.createPointerMove(ofMillis(0),
                            PointerInput.Origin.viewport(), screen_size.width / 2, screen_size.height/5));
                    sequence.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
                    sequence.addAction(new Pause(finger, ofMillis(600)));
                    sequence.addAction(finger.createPointerMove(ofMillis(600),
                            PointerInput.Origin.viewport(), screen_size.width / 2, screen_size.height * 8 / 10));
                    sequence.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
                    driverAndroid.perform(singletonList(sequence));
                }
                else if ("ios".equalsIgnoreCase(getPlatform())) {
                    Dimension screen_size = driverIos.manage().window().getSize();
                    PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
                    Sequence sequence = new Sequence(finger, 1);
                    sequence.addAction(finger.createPointerMove(ofMillis(0),
                            PointerInput.Origin.viewport(), screen_size.width / 2, screen_size.height/5));
                    sequence.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
                    sequence.addAction(new Pause(finger, ofMillis(600)));
                    sequence.addAction(finger.createPointerMove(ofMillis(600),
                            PointerInput.Origin.viewport(), screen_size.width / 2, screen_size.height * 8 / 10));
                    sequence.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
                    driverIos.perform(singletonList(sequence));

                }
            }
        }
        return null;
    }
    public UiObject swipeElementRight() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Point location = null;
        if("android".equalsIgnoreCase(getPlatform())){
            driverAndroid = AndroidSettings.driverAndroid.get();
            WebDriverWait wait = new WebDriverWait(driverAndroid, Duration.ofSeconds(40));
            try {
                if (Objects.equals(android_selector, "xpath")) {
                    Method method = By.class.getMethod(android_selector, String.class);
                    By by = (By) method.invoke(null, android_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    location = driverAndroid.findElement(by).getLocation();
                    Dimension screen_size = driverAndroid.manage().window().getSize();
                    PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
                    Sequence sequence = new Sequence(finger, 1);
                    sequence.addAction(finger.createPointerMove(ofMillis(0),
                            PointerInput.Origin.viewport(), location.x + 32, location.y + 16));
                    sequence.addAction(finger.createPointerDown(PointerInput.MouseButton.MIDDLE.asArg()));
                    sequence.addAction(new Pause(finger, ofMillis(600)));
                    sequence.addAction(finger.createPointerMove(ofMillis(600),
                            PointerInput.Origin.viewport(), screen_size.width - 16, location.y + 16));
                    sequence.addAction(finger.createPointerUp(PointerInput.MouseButton.MIDDLE.asArg()));
                    driverAndroid.perform(singletonList(sequence));
                }else {
                    Method method = AppiumBy.class.getMethod(android_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, android_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    location = driverAndroid.findElement(by).getLocation();
                    Dimension screen_size = driverAndroid.manage().window().getSize();
                    PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
                    Sequence sequence = new Sequence(finger, 1);
                    sequence.addAction(finger.createPointerMove(ofMillis(0),
                            PointerInput.Origin.viewport(), location.x + 32, location.y + 16));
                    sequence.addAction(finger.createPointerDown(PointerInput.MouseButton.MIDDLE.asArg()));
                    sequence.addAction(new Pause(finger, ofMillis(600)));
                    sequence.addAction(finger.createPointerMove(ofMillis(600),
                            PointerInput.Origin.viewport(), screen_size.width - 16, location.y + 16));
                    sequence.addAction(finger.createPointerUp(PointerInput.MouseButton.MIDDLE.asArg()));
                    driverAndroid.perform(singletonList(sequence));
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                screenshotAndroid(element_name, android_locator);
                Assert.fail("Error: " + e.getMessage());
            }
        }
        else if("ios".equalsIgnoreCase(getPlatform())){
            driverIos = IosSettings.driverIos.get();
            WebDriverWait wait = new WebDriverWait(driverIos, Duration.ofSeconds(40));
            try {
                if (Objects.equals(ios_selector, "xpath")) {
                    Method method = By.class.getMethod(ios_selector, String.class);
                    By by = (By) method.invoke(null, ios_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    location = driverIos.findElement(by).getLocation();
                    Dimension screen_size = driverIos.manage().window().getSize();
                    PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
                    Sequence sequence = new Sequence(finger, 1);
                    sequence.addAction(finger.createPointerMove(ofMillis(0),
                            PointerInput.Origin.viewport(), location.x + 32, location.y + 16));
                    sequence.addAction(finger.createPointerDown(PointerInput.MouseButton.MIDDLE.asArg()));
                    sequence.addAction(new Pause(finger, ofMillis(600)));
                    sequence.addAction(finger.createPointerMove(ofMillis(600),
                            PointerInput.Origin.viewport(), screen_size.width - 16, location.y + 16));
                    sequence.addAction(finger.createPointerUp(PointerInput.MouseButton.MIDDLE.asArg()));
                    driverIos.perform(singletonList(sequence));
                }else {
                    Method method = AppiumBy.class.getMethod(ios_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, ios_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    location = driverIos.findElement(by).getLocation();
                    Dimension screen_size = driverIos.manage().window().getSize();
                    PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
                    Sequence sequence = new Sequence(finger, 1);
                    sequence.addAction(finger.createPointerMove(ofMillis(0),
                            PointerInput.Origin.viewport(), location.x + 32, location.y + 16));
                    sequence.addAction(finger.createPointerDown(PointerInput.MouseButton.MIDDLE.asArg()));
                    sequence.addAction(new Pause(finger, ofMillis(600)));
                    sequence.addAction(finger.createPointerMove(ofMillis(600),
                            PointerInput.Origin.viewport(), screen_size.width - 16, location.y + 16));
                    sequence.addAction(finger.createPointerUp(PointerInput.MouseButton.MIDDLE.asArg()));
                    driverIos.perform(singletonList(sequence));
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                screenshotIos(element_name, ios_locator);
                Assert.fail("Error: " + e.getMessage());
            }
        }
        return null;
    }
    public UiObject swipeElementLeft() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Point location = null;
        if("android".equalsIgnoreCase(getPlatform())){
            driverAndroid = AndroidSettings.driverAndroid.get();
            WebDriverWait wait = new WebDriverWait(driverAndroid, Duration.ofSeconds(40));
            try {
                if (Objects.equals(android_selector, "xpath")) {
                    Method method = By.class.getMethod(android_selector, String.class);
                    By by = (By) method.invoke(null, android_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    location = driverAndroid.findElement(by).getLocation();
                    Dimension screen_size = driverAndroid.manage().window().getSize();
                    PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
                    Sequence sequence = new Sequence(finger, 1);
                    sequence.addAction(finger.createPointerMove(ofMillis(0),
                            PointerInput.Origin.viewport(), screen_size.width - 16, location.y + 16));
                    sequence.addAction(finger.createPointerDown(PointerInput.MouseButton.MIDDLE.asArg()));
                    sequence.addAction(new Pause(finger, ofMillis(600)));
                    sequence.addAction(finger.createPointerMove(ofMillis(600),
                            PointerInput.Origin.viewport(), location.x + 32, location.y + 16));
                    sequence.addAction(finger.createPointerUp(PointerInput.MouseButton.MIDDLE.asArg()));
                    driverAndroid.perform(singletonList(sequence));
                }else {
                    Method method = AppiumBy.class.getMethod(android_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, android_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    location = driverAndroid.findElement(by).getLocation();
                    Dimension screen_size = driverAndroid.manage().window().getSize();
                    PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
                    Sequence sequence = new Sequence(finger, 1);
                    sequence.addAction(finger.createPointerMove(ofMillis(0),
                            PointerInput.Origin.viewport(), screen_size.width - 16, location.y + 16));
                    sequence.addAction(finger.createPointerDown(PointerInput.MouseButton.MIDDLE.asArg()));
                    sequence.addAction(new Pause(finger, ofMillis(600)));
                    sequence.addAction(finger.createPointerMove(ofMillis(600),
                            PointerInput.Origin.viewport(), location.x + 32, location.y + 16));
                    sequence.addAction(finger.createPointerUp(PointerInput.MouseButton.MIDDLE.asArg()));
                    driverAndroid.perform(singletonList(sequence));
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                screenshotAndroid(element_name, android_locator);
                Assert.fail("Error: " + e.getMessage());
            }
        }else if("ios".equalsIgnoreCase(getPlatform())){
            driverIos = IosSettings.driverIos.get();
            WebDriverWait wait = new WebDriverWait(driverIos, Duration.ofSeconds(40));
            try {
                if (Objects.equals(ios_selector, "xpath")) {
                    Method method = By.class.getMethod(ios_selector, String.class);
                    By by = (By) method.invoke(null, ios_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    location = driverIos.findElement(by).getLocation();
                    Dimension screen_size = driverIos.manage().window().getSize();
                    PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
                    Sequence sequence = new Sequence(finger, 1);
                    sequence.addAction(finger.createPointerMove(ofMillis(0),
                            PointerInput.Origin.viewport(), screen_size.width - 16, location.y + 16));
                    sequence.addAction(finger.createPointerDown(PointerInput.MouseButton.MIDDLE.asArg()));
                    sequence.addAction(new Pause(finger, ofMillis(600)));
                    sequence.addAction(finger.createPointerMove(ofMillis(600),
                            PointerInput.Origin.viewport(), location.x + 32, location.y + 16));
                    sequence.addAction(finger.createPointerUp(PointerInput.MouseButton.MIDDLE.asArg()));
                    driverIos.perform(singletonList(sequence));
                }else {
                    Method method = AppiumBy.class.getMethod(ios_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, ios_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    location = driverIos.findElement(by).getLocation();
                    Dimension screen_size = driverIos.manage().window().getSize();
                    PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
                    Sequence sequence = new Sequence(finger, 1);
                    sequence.addAction(finger.createPointerMove(ofMillis(0),
                            PointerInput.Origin.viewport(), screen_size.width - 16, location.y + 16));
                    sequence.addAction(finger.createPointerDown(PointerInput.MouseButton.MIDDLE.asArg()));
                    sequence.addAction(new Pause(finger, ofMillis(600)));
                    sequence.addAction(finger.createPointerMove(ofMillis(600),
                            PointerInput.Origin.viewport(), location.x + 32, location.y + 16));
                    sequence.addAction(finger.createPointerUp(PointerInput.MouseButton.MIDDLE.asArg()));
                    driverIos.perform(singletonList(sequence));
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                screenshotIos(element_name, ios_locator);
                Assert.fail("Error: " + e.getMessage());
            }
        }
        return null;
    }
    public UiObject swipeElementUp() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Point location = null;
        if("android".equalsIgnoreCase(getPlatform())){
            driverAndroid = AndroidSettings.driverAndroid.get();
            WebDriverWait wait = new WebDriverWait(driverAndroid, Duration.ofSeconds(40));
            try {
                if (Objects.equals(android_selector, "xpath")) {
                    Method method = By.class.getMethod(android_selector, String.class);
                    By by = (By) method.invoke(null, android_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    location = driverAndroid.findElement(by).getLocation();
                    Dimension screen_size = driverAndroid.manage().window().getSize();
                    PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
                    Sequence sequence = new Sequence(finger, 1);
                    sequence.addAction(finger.createPointerMove(ofMillis(0),
                            PointerInput.Origin.viewport(), screen_size.width - 16, location.y + 16));
                    sequence.addAction(finger.createPointerDown(PointerInput.MouseButton.MIDDLE.asArg()));
                    sequence.addAction(new Pause(finger, ofMillis(600)));
                    sequence.addAction(finger.createPointerMove(ofMillis(600),
                            PointerInput.Origin.viewport(), location.x + 16, location.y - 128));
                    sequence.addAction(finger.createPointerUp(PointerInput.MouseButton.MIDDLE.asArg()));
                    driverAndroid.perform(singletonList(sequence));
                }else {
                    Method method = AppiumBy.class.getMethod(android_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, android_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    location = driverAndroid.findElement(by).getLocation();
                    Dimension screen_size = driverAndroid.manage().window().getSize();
                    PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
                    Sequence sequence = new Sequence(finger, 1);
                    sequence.addAction(finger.createPointerMove(ofMillis(0),
                            PointerInput.Origin.viewport(), screen_size.width - 16, location.y + 16));
                    sequence.addAction(finger.createPointerDown(PointerInput.MouseButton.MIDDLE.asArg()));
                    sequence.addAction(new Pause(finger, ofMillis(600)));
                    sequence.addAction(finger.createPointerMove(ofMillis(600),
                            PointerInput.Origin.viewport(), location.x + 32, location.y + 16));
                    sequence.addAction(finger.createPointerUp(PointerInput.MouseButton.MIDDLE.asArg()));
                    driverAndroid.perform(singletonList(sequence));
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                boolean fail = !true;
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                screenshotAndroid(element_name, android_locator);
                assert  fail = false : "Error" + e;
            }
        }else if("ios".equalsIgnoreCase(getPlatform())){
            driverIos = IosSettings.driverIos.get();
            WebDriverWait wait = new WebDriverWait(driverIos, Duration.ofSeconds(40));
            try {
                if (Objects.equals(ios_selector, "xpath")) {
                    Method method = By.class.getMethod(ios_selector, String.class);
                    By by = (By) method.invoke(null, ios_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    location = driverIos.findElement(by).getLocation();
                    Dimension screen_size = driverIos.manage().window().getSize();
                    PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
                    Sequence sequence = new Sequence(finger, 1);
                    sequence.addAction(finger.createPointerMove(ofMillis(0),
                            PointerInput.Origin.viewport(), screen_size.width - 16, location.y + 16));
                    sequence.addAction(finger.createPointerDown(PointerInput.MouseButton.MIDDLE.asArg()));
                    sequence.addAction(new Pause(finger, ofMillis(600)));
                    sequence.addAction(finger.createPointerMove(ofMillis(600),
                            PointerInput.Origin.viewport(), location.x + 32, location.y + 16));
                    sequence.addAction(finger.createPointerUp(PointerInput.MouseButton.MIDDLE.asArg()));
                    driverIos.perform(singletonList(sequence));
                }else {
                    Method method = AppiumBy.class.getMethod(ios_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, ios_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    location = driverIos.findElement(by).getLocation();
                    Dimension screen_size = driverIos.manage().window().getSize();
                    PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
                    Sequence sequence = new Sequence(finger, 1);
                    sequence.addAction(finger.createPointerMove(ofMillis(0),
                            PointerInput.Origin.viewport(), screen_size.width - 16, location.y + 16));
                    sequence.addAction(finger.createPointerDown(PointerInput.MouseButton.MIDDLE.asArg()));
                    sequence.addAction(new Pause(finger, ofMillis(600)));
                    sequence.addAction(finger.createPointerMove(ofMillis(600),
                            PointerInput.Origin.viewport(), location.x + 32, location.y + 16));
                    sequence.addAction(finger.createPointerUp(PointerInput.MouseButton.MIDDLE.asArg()));
                    driverIos.perform(singletonList(sequence));
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                boolean fail = !true;
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                screenshotIos(element_name,ios_locator);
                assert  fail = false : "Error" + e;
            }
        }
        return null;
    }
    /** Metode tryClickWithWait, tryGetTextWithWait, trySendTextWithWait koriste se kada pretpostavljamo da možda element nad kojim
     * želimo obaviti neku od navedenih akcija neće biti prisutan. Metode su unutar try catch bloka koji ignorira NoSuchElementException i TimeoutException,
     * s smanjenim vremenom čekanja.
     */
    public UiObject tryClickWithWait(Integer waitTime) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        try {
            if("android".equalsIgnoreCase(getPlatform())){
                driverAndroid = AndroidSettings.driverAndroid.get();
                WebDriverWait wait = new WebDriverWait(driverAndroid, Duration.ofSeconds(waitTime));
                if (Objects.equals(android_selector, "xpath")) {
                    Method method = By.class.getMethod(android_selector, String.class);
                    By by = (By) method.invoke(null, android_locator);
                    wait.until(ExpectedConditions.elementToBeClickable(by));
                    driverAndroid.findElement(by).click();
                    TestLogger.addLogMessage("Element " + element_name + " is clicked");
                }
                else {
                    Method method = AppiumBy.class.getMethod(android_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, android_locator);
                    wait.until(ExpectedConditions.elementToBeClickable(by));
                    driverAndroid.findElement(by).click();
                    TestLogger.addLogMessage("Element " + element_name + " is clicked");
                }
            }
            else if("ios".equalsIgnoreCase(getPlatform())){
                driverIos = IosSettings.driverIos.get();
                WebDriverWait wait = new WebDriverWait(driverIos, Duration.ofSeconds(waitTime));
                if (Objects.equals(ios_selector, "xpath")) {
                    Method method = By.class.getMethod(ios_selector, String.class);
                    By by = (By) method.invoke(null, ios_locator);
                    wait.until(ExpectedConditions.elementToBeClickable(by));
                    driverIos.findElement(by).click();
                    TestLogger.addLogMessage("Element " + element_name + " is clicked");
                }
                else {
                    Method method = AppiumBy.class.getMethod(ios_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, ios_locator);
                    wait.until(ExpectedConditions.elementToBeClickable(by));
                    driverIos.findElement(by).click();
                    TestLogger.addLogMessage("Element " + element_name + " is clicked");
                }
            }
        }
        catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
            TestLogger.addLogMessage("Element " + element_name + " nije bio vidljiv");
        }

        return null;
    }
    public String tryGetTextWithWait(Integer waitTime) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String text = null;
        try {
            if("android".equalsIgnoreCase(getPlatform())){
                driverAndroid = AndroidSettings.driverAndroid.get();
                WebDriverWait wait = new WebDriverWait(driverAndroid, Duration.ofSeconds(waitTime));
                if (Objects.equals(android_selector, "xpath")) {
                    Method method = By.class.getMethod(android_selector, String.class);
                    By by = (By) method.invoke(null, android_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    text = driverAndroid.findElement(by).getText();
                    TestLogger.addLogMessage("The text '" + text + "' has been extracted from the " + element_name + " element");
                }
                else {
                    Method method = AppiumBy.class.getMethod(android_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, android_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    text = driverAndroid.findElement(by).getText();
                    TestLogger.addLogMessage("The text '" + text + "' has been extracted from the " + element_name + " element");
                }
            }
            else if("ios".equalsIgnoreCase(getPlatform())){
                driverIos = IosSettings.driverIos.get();
                WebDriverWait wait = new WebDriverWait(driverIos, Duration.ofSeconds(waitTime));
                if (Objects.equals(ios_selector, "xpath")) {
                    Method method = By.class.getMethod(ios_selector, String.class);
                    By by = (By) method.invoke(null, ios_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    text = driverIos.findElement(by).getText();
                    TestLogger.addLogMessage("The text '" + text + "' has been extracted from the " + element_name + " element");
                }
                else {
                    Method method = AppiumBy.class.getMethod(ios_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, ios_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    text = driverIos.findElement(by).getText();
                    TestLogger.addLogMessage("The text '" + text + "' has been extracted from the " + element_name + " element");
                }
            }
        }
        catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
            TestLogger.addLogMessage("Element " + element_name + " nije bio vidljiv");
        }
        return text;
    }
    public UiObject trySendTextWithWait(String text, Integer waitTime) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        try {
            if("android".equalsIgnoreCase(getPlatform())){
                driverAndroid = AndroidSettings.driverAndroid.get();
                WebDriverWait wait = new WebDriverWait(driverAndroid, Duration.ofSeconds(waitTime));
                if (Objects.equals(android_selector, "xpath")) {
                    Method method = By.class.getMethod(android_selector, String.class);
                    By by = (By) method.invoke(null, android_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    driverAndroid.findElement(by).sendKeys(text);
                    TestLogger.addLogMessage("The text '" + text + "' has been sent to the " + element_name + " element");
                }
                else {
                    Method method = AppiumBy.class.getMethod(android_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, android_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    driverAndroid.findElement(by).sendKeys(text);
                    TestLogger.addLogMessage("The text '" + text + "' has been sent to the " + element_name + " element");
                }
            }else if("ios".equalsIgnoreCase(getPlatform())){
                driverIos = IosSettings.driverIos.get();
                WebDriverWait wait = new WebDriverWait(driverIos, Duration.ofSeconds(waitTime));
                if (Objects.equals(ios_selector, "xpath")) {
                    Method method = By.class.getMethod(ios_selector, String.class);
                    By by = (By) method.invoke(null, ios_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    driverIos.findElement(by).sendKeys(text);
                    TestLogger.addLogMessage("The text '" + text + "' has been sent to the " + element_name + " element");
                }
                else {
                    Method method = AppiumBy.class.getMethod(ios_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, ios_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    driverIos.findElement(by).sendKeys(text);
                    TestLogger.addLogMessage("The text '" + text + "' has been sent to the " + element_name + " element");
                }
            }
        }
        catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
            TestLogger.addLogMessage("Element " + element_name + " nije bio vidljiv");
        }
        return null;
    }
    /** Metoda isElementWithTextDisplayed koriste se kada tražimo određeni tekst
     * Najčešće se koristi s xpath-om gdje po parametru className tražimo da li je određeni parametar text vidljiv
     */
    public Boolean isElementWithTextDisplayed(Integer waitTime, String text) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Boolean is_displayed = false;
        if("android".equalsIgnoreCase(getPlatform())){
            driverAndroid = AndroidSettings.driverAndroid.get();
            WebDriverWait wait = new WebDriverWait(driverAndroid, Duration.ofSeconds(waitTime));
            try {
                if (Objects.equals(android_selector, "xpath")) {
                    String android_xpath_locator = "//" + android_locator + "[@text=\"" + text + "\"]";
                    Method method = By.class.getMethod(android_selector, String.class);
                    By by = (By) method.invoke(null, android_xpath_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    is_displayed = driverAndroid.findElement(by).isDisplayed();
                    TestLogger.addLogMessage("Element " + element_name + " is displayed");
                }else {
                    Method method = AppiumBy.class.getMethod(android_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, text);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    is_displayed = driverAndroid.findElement(by).isDisplayed();
                    TestLogger.addLogMessage("Element " + element_name + " is displayed");
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                screenshotAndroid(element_name, android_locator);
                Assert.fail("Error: " + e.getMessage());
            }
        }
        else if("ios".equalsIgnoreCase(getPlatform())){
            driverIos = IosSettings.driverIos.get();
            WebDriverWait wait = new WebDriverWait(driverIos, Duration.ofSeconds(waitTime));
            String ios_xpath_locator = null;
            if (Objects.equals(ios_selector, "xpath")) {
                ios_xpath_locator = "//" + ios_locator + "[@name=\"" + text + "\"]";
            }
            else if (Objects.equals(ios_selector, "iOSNsPredicateString")) {
                ios_xpath_locator = "type == \"" + ios_locator + "\" AND name CONTAINS \"" + text + "\"";
            }
            else if (Objects.equals(ios_selector, "iOSClassChain")) {
                ios_xpath_locator = "**/" + ios_locator+ "[`label CONTAINS \"" + text + "\"`]";
            }
            else if (Objects.equals(ios_selector, "accessibilityId")) {
                ios_xpath_locator = text;
            }
            try {
                if (Objects.equals(ios_selector, "xpath")) {
                    Method method = By.class.getMethod(ios_selector, String.class);
                    By by = (By) method.invoke(null, ios_xpath_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    is_displayed = driverIos.findElement(by).isDisplayed();
                    TestLogger.addLogMessage("Element " + element_name + " is displayed");
                }else {
                    Method method = AppiumBy.class.getMethod(ios_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, ios_xpath_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    is_displayed = driverIos.findElement(by).isDisplayed();
                    TestLogger.addLogMessage("Element " + element_name + " is displayed");
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                screenshotIos(element_name, ios_xpath_locator);
                Assert.fail("Error: " + e.getMessage());
            }
        }
        return is_displayed;
    }
    public Boolean elementWithTextIsNotDisplayed(Integer waitTime, String text) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Boolean is_displayed = false;
        if ("android".equalsIgnoreCase(getPlatform())) {
            driverAndroid = AndroidSettings.driverAndroid.get();
            WebDriverWait wait = new WebDriverWait(driverAndroid, Duration.ofSeconds(waitTime));
            String android_xpath_locator = "//" + android_locator + "[@text=\"" + text + "\"]";
            try {
                if (Objects.equals(android_selector, "xpath")) {
                    Method method = By.class.getMethod(android_selector, String.class);
                    By by = (By) method.invoke(null, android_xpath_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    is_displayed = driverAndroid.findElement(by).isDisplayed();
                    TestLogger.addLogMessage("Element " + element_name + " is displayed");
                    screenshotAndroid(element_name,android_locator);
                    Assert.fail("Element " + element_name + " is not found");
                }else {
                    Method method = AppiumBy.class.getMethod(android_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, android_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    is_displayed = driverAndroid.findElement(by).isDisplayed();
                    TestLogger.addLogMessage("Element " + element_name + " is not found");
                    screenshotAndroid(element_name, android_locator);
                    Assert.fail("Element " + element_name + " is not found");
                }
            } catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                TestLogger.addLogMessage("Element not found or not visible as expected");
            }
        } else if ("ios".equalsIgnoreCase(getPlatform())) {
            driverIos = IosSettings.driverIos.get();
            WebDriverWait wait = new WebDriverWait(driverIos, Duration.ofSeconds(waitTime));
            String ios_xpath_locator = null;
            if (Objects.equals(ios_selector, "xpath")) {
                ios_xpath_locator = "//" + ios_locator + "[@name=\"" + text + "\"]";
            }
            else if (Objects.equals(ios_selector, "iOSNsPredicateString")) {
                ios_xpath_locator = "type == \"" + ios_locator + "\" AND name CONTAINS \"" + text + "\"";
            }
            else if (Objects.equals(ios_selector, "iOSClassChain")) {
                ios_xpath_locator = "**/" + ios_locator+ "[`label CONTAINS \"" + text + "\" `]";
            }
            else if (Objects.equals(ios_selector, "accessibilityId")) {
                ios_xpath_locator = text;
            }
            try {
                if (Objects.equals(ios_selector, "xpath")) {
                    Method method = By.class.getMethod(ios_selector, String.class);
                    By by = (By) method.invoke(null, ios_xpath_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    is_displayed = driverIos.findElement(by).isDisplayed();
                    TestLogger.addLogMessage("Element " + element_name + " is displayed");
                    screenshotIos(element_name,ios_locator);
                    Assert.fail("Element " + element_name + " is not found");
                }else {
                    Method method = AppiumBy.class.getMethod(ios_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, ios_xpath_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    is_displayed = driverIos.findElement(by).isDisplayed();
                    TestLogger.addLogMessage("Element " + element_name + " is displayed");
                    screenshotIos(element_name,ios_locator);
                    Assert.fail("Element " + element_name + " is not found");
                }
            } catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                TestLogger.addLogMessage("Element not found or not visible as expected");
            }
        }
        if (is_displayed) {
            throw new AssertionError("Element " + element_name + " is unexpectedly displayed.");
        }
        return is_displayed;
    }
    public UiObject clickElementWithText(String text) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        if("android".equalsIgnoreCase(getPlatform())){
            driverAndroid = AndroidSettings.driverAndroid.get();
            WebDriverWait wait = new WebDriverWait(driverAndroid, Duration.ofSeconds(50));
            String android_xpath_locator = "//" + android_locator + "[@text=\"" + text + "\"]";
            try {
                if (Objects.equals(android_selector, "xpath")) {
                    Method method = By.class.getMethod(android_selector, String.class);
                    By by = (By) method.invoke(null, android_xpath_locator);
                    wait.until(ExpectedConditions.elementToBeClickable(by));
                    driverAndroid.findElement(by).click();
                    TestLogger.addLogMessage("Element " + element_name + " is clicked");
                }else {
                    Method method = AppiumBy.class.getMethod(android_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, android_locator);
                    wait.until(ExpectedConditions.elementToBeClickable(by));
                    driverAndroid.findElement(by).click();
                    TestLogger.addLogMessage("Element " + element_name + " is clicked");
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                screenshotAndroid(element_name, android_locator);
                Assert.fail("Error: " + e.getMessage());
            }
        }
        else if("ios".equalsIgnoreCase(getPlatform())){
            driverIos = IosSettings.driverIos.get();
            WebDriverWait wait = new WebDriverWait(driverIos, Duration.ofSeconds(50));
            String ios_xpath_locator = null;
            if (Objects.equals(ios_selector, "xpath")) {
                ios_xpath_locator = "//" + ios_locator + "[@name=\"" + text + "\"]";
            }
            else if (Objects.equals(ios_selector, "iOSNsPredicateString")) {
                ios_xpath_locator = "type == \"" + ios_locator + "\" AND name CONTAINS \"" + text + "\"";
            }
            else if (Objects.equals(ios_selector, "iOSClassChain")) {
                ios_xpath_locator = "**/" + ios_locator + "[`label CONTAINS \"" + text + "\" `]";
            }
            else if (Objects.equals(ios_selector, "accessibilityId")) {
                ios_xpath_locator = text;
            }
            try {
                if (Objects.equals(ios_selector, "xpath")) {
                    Method method = By.class.getMethod(ios_selector, String.class);
                    By by = (By) method.invoke(null, ios_xpath_locator);
                    wait.until(ExpectedConditions.elementToBeClickable(by));
                    driverIos.findElement(by).click();
                    TestLogger.addLogMessage("Element " + element_name + " is clicked");
                }else {
                    Method method = AppiumBy.class.getMethod(ios_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, ios_xpath_locator);
                    wait.until(ExpectedConditions.elementToBeClickable(by));
                    driverIos.findElement(by).click();
                    TestLogger.addLogMessage("Element " + element_name + " is clicked");
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                screenshotIos(element_name, ios_locator);
                Assert.fail("Error: " + e.getMessage());
            }
        }
        return null;
    }
    public UiObject clickElementWithTextAndIndex(String text, int index) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        if("android".equalsIgnoreCase(getPlatform())){
            driverAndroid = AndroidSettings.driverAndroid.get();
            WebDriverWait wait = new WebDriverWait(driverAndroid, Duration.ofSeconds(50));
            String android_xpath_locator = "//" + android_locator + "[@text=\"" + text + "\"]";
            try {
                if (Objects.equals(android_selector, "xpath")) {
                    Method method = By.class.getMethod(android_selector, String.class);
                    By by = (By) method.invoke(null, android_xpath_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    List<WebElement> mobileElements = driverAndroid.findElements(by);
                    WebElement mobileElement = mobileElements.get(index);
                    mobileElement.click();
                    TestLogger.addLogMessage("Element " + element_name + " is clicked");
                }else {
                    Method method = AppiumBy.class.getMethod(android_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, android_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    List<WebElement> mobileElements = driverAndroid.findElements(by);
                    WebElement mobileElement = mobileElements.get(index);
                    mobileElement.click();
                    TestLogger.addLogMessage("Element " + element_name + " is clicked");
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                screenshotAndroid(element_name, android_locator);
                Assert.fail("Error: " + e.getMessage());
            }
        }
        else if("ios".equalsIgnoreCase(getPlatform())){
            driverIos = IosSettings.driverIos.get();
            WebDriverWait wait = new WebDriverWait(driverIos, Duration.ofSeconds(50));
            String ios_xpath_locator = null;
            if (Objects.equals(ios_selector, "xpath")) {
                ios_xpath_locator = "//" + ios_locator + "[@name=\"" + text + "\"]";
            }
            else if (Objects.equals(ios_selector, "iOSNsPredicateString")) {
                ios_xpath_locator = "type == \"" + ios_locator + "\" AND name CONTAINS \"" + text + "\"";
            }
            else if (Objects.equals(ios_selector, "iOSClassChain")) {
                ios_xpath_locator = "**/" + ios_locator + "[`label CONTAINS \"" + text + "\" `]";
            }
            else if (Objects.equals(ios_selector, "accessibilityId")) {
                ios_xpath_locator = text;
            }
            try {
                if (Objects.equals(ios_selector, "xpath")) {
                    Method method = By.class.getMethod(ios_selector, String.class);
                    By by = (By) method.invoke(null, ios_xpath_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    List<WebElement> mobileElements = driverIos.findElements(by);
                    WebElement mobileElement = mobileElements.get(index);
                    mobileElement.click();
                    TestLogger.addLogMessage("Element " + element_name + " is clicked");
                }else {
                    Method method = AppiumBy.class.getMethod(ios_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, ios_xpath_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    List<WebElement> mobileElements = driverIos.findElements(by);
                    WebElement mobileElement = mobileElements.get(index);
                    mobileElement.click();
                    TestLogger.addLogMessage("Element " + element_name + " is clicked");
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                screenshotIos(element_name, ios_locator);
                Assert.fail("Error: " + e.getMessage());
            }
        }
        return null;
    }
    public UiObject findElementWithText(String text) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        if("android".equalsIgnoreCase(getPlatform())){
            driverAndroid = AndroidSettings.driverAndroid.get();
            WebDriverWait wait = new WebDriverWait(driverAndroid, Duration.ofSeconds(50));
            String android_xpath_locator = "//" + android_locator + "[@text=\"" + text + "\"]";
            try {
                if (Objects.equals(android_selector, "xpath")) {
                    Method method = By.class.getMethod(android_selector, String.class);
                    By by = (By) method.invoke(null, android_xpath_locator);
                    wait.until(ExpectedConditions.elementToBeClickable(by));
                    driverAndroid.findElement(by);
                    TestLogger.addLogMessage("Element " + element_name + " is found");
                }else {
                    Method method = AppiumBy.class.getMethod(android_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, android_locator);
                    wait.until(ExpectedConditions.elementToBeClickable(by));
                    driverAndroid.findElement(by);
                    TestLogger.addLogMessage("Element " + element_name + " is found");
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                screenshotAndroid(element_name, android_locator);
                Assert.fail("Error: " + e.getMessage());
            }
        }
        else if("ios".equalsIgnoreCase(getPlatform())){
            driverIos = IosSettings.driverIos.get();
            WebDriverWait wait = new WebDriverWait(driverIos, Duration.ofSeconds(50));
            String ios_xpath_locator = null;
            if (Objects.equals(ios_selector, "xpath")) {
                ios_xpath_locator = "//" + ios_locator + "[@name=\"" + text + "\"]";
            }
            else if (Objects.equals(ios_selector, "iOSNsPredicateString")) {
                ios_xpath_locator = "type == \"" + ios_locator + "\" AND name CONTAINS \"" + text + "\"";
            }
            else if (Objects.equals(ios_selector, "iOSClassChain")) {
                ios_xpath_locator = "**/" + ios_locator+ "[`label CONTAINS \"" + text + "\" `]";
            }
            else if (Objects.equals(ios_selector, "accessibilityId")) {
                ios_xpath_locator = text;
            }
            try {
                if (Objects.equals(ios_selector, "xpath")) {
                    Method method = By.class.getMethod(ios_selector, String.class);
                    By by = (By) method.invoke(null, ios_xpath_locator);
                    wait.until(ExpectedConditions.elementToBeClickable(by));
                    driverIos.findElement(by);
                    TestLogger.addLogMessage("Element " + element_name + " is found");
                }else {
                    Method method = AppiumBy.class.getMethod(ios_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, ios_xpath_locator);
                    wait.until(ExpectedConditions.elementToBeClickable(by));
                    driverIos.findElement(by);
                    TestLogger.addLogMessage("Element " + element_name + " is found");
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                screenshotIos(element_name, ios_locator);
                Assert.fail("Error: " + e.getMessage());
            }
        }
        return null;
    }
    public UiObject clickElementWithIndexAndWait(Integer index) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, IOException {
        if("android".equalsIgnoreCase(getPlatform())){
            driverAndroid = AndroidSettings.driverAndroid.get();
            try {
                WebDriverWait wait = new WebDriverWait(driverAndroid, Duration.ofSeconds(50));
                if (Objects.equals(android_selector, "xpath")) {
                    Method method = By.class.getMethod(android_selector, String.class);
                    By by = (By) method.invoke(null, android_locator);
                    wait.until(ExpectedConditions.elementToBeClickable(by));
                    List<WebElement> mobileElements = driverAndroid.findElements(by);
                    WebElement mobileElement = mobileElements.get(index);
                    mobileElement.click();
                    TestLogger.addLogMessage("Elements " + element_name + " are found, and " + index + " is clicked");
                }else {
                    Method method = AppiumBy.class.getMethod(android_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, android_locator);
                    wait.until(ExpectedConditions.elementToBeClickable(by));
                    List<WebElement> mobileElements = driverAndroid.findElements(by);
                    WebElement mobileElement = mobileElements.get(index);
                    mobileElement.click();
                    TestLogger.addLogMessage("Elements " + element_name + " are found, and " + index + " is clicked");
                }
            }
            catch (NoSuchElementException | TimeoutException | IndexOutOfBoundsException | StaleElementReferenceException e) {
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                screenshotAndroid(element_name, android_locator);
                Assert.fail("Error: " + e.getMessage());
            }
        }
        else if("ios".equalsIgnoreCase(getPlatform())){
            driverIos = IosSettings.driverIos.get();
            try {
                WebDriverWait wait = new WebDriverWait(driverIos, Duration.ofSeconds(50));
                if (Objects.equals(ios_selector, "xpath")) {
                    Method method = By.class.getMethod(ios_selector, String.class);
                    By by = (By) method.invoke(null, ios_locator);
                    wait.until(ExpectedConditions.elementToBeClickable(by));
                    List<WebElement> mobileElements = driverIos.findElements(by);
                    WebElement mobileElement = mobileElements.get(index);
                    mobileElement.click();
                    TestLogger.addLogMessage("Elements " + element_name + " are found, and " + index + " is clicked");
                }else {
                    Method method = AppiumBy.class.getMethod(ios_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, ios_locator);
                    wait.until(ExpectedConditions.elementToBeClickable(by));
                    List<WebElement> mobileElements = driverIos.findElements(by);
                    WebElement mobileElement = mobileElements.get(index);
                    mobileElement.click();
                    TestLogger.addLogMessage("Elements " + element_name + " are found, and " + index + " is clicked");
                }
            }
            catch (NoSuchElementException | TimeoutException | IndexOutOfBoundsException | StaleElementReferenceException e) {
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                screenshotIos(element_name, ios_locator);
                Assert.fail("Error: " + e.getMessage());
            }
        }
        return this;
    }
    public UiObject sendTextToElementWithIndexAndWait(Integer index, String text) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, IOException {
        if("android".equalsIgnoreCase(getPlatform())){
            driverAndroid = AndroidSettings.driverAndroid.get();
            try {
                WebDriverWait wait = new WebDriverWait(driverAndroid, Duration.ofSeconds(50));
                if (Objects.equals(android_selector, "xpath")) {
                    Method method = By.class.getMethod(android_selector, String.class);
                    By by = (By) method.invoke(null, android_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    List<WebElement> mobileElements = driverAndroid.findElements(by);
                    WebElement mobileElement = mobileElements.get(index);
                    mobileElement.sendKeys(text);
                    TestLogger.addLogMessage("Elements " + element_name + " are found, and " + index + " index is clicked");
                }else {
                    Method method = AppiumBy.class.getMethod(android_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, android_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    List<WebElement> mobileElements = driverAndroid.findElements(by);
                    WebElement mobileElement = mobileElements.get(index);
                    mobileElement.sendKeys(text);
                    TestLogger.addLogMessage("Elements " + element_name + " are found, and " + index + " index is clicked");
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException | IndexOutOfBoundsException e) {
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                screenshotAndroid(element_name, android_locator);
                Assert.fail("Error: " + e.getMessage());
            }
        }
        else if("ios".equalsIgnoreCase(getPlatform())){
            driverIos = IosSettings.driverIos.get();
            try {
                WebDriverWait wait = new WebDriverWait(driverIos, Duration.ofSeconds(50));
                if (Objects.equals(ios_selector, "xpath")) {
                    Method method = By.class.getMethod(ios_selector, String.class);
                    By by = (By) method.invoke(null, ios_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    List<WebElement> mobileElements = driverIos.findElements(by);
                    WebElement mobileElement = mobileElements.get(index);
                    mobileElement.sendKeys(text);
                    TestLogger.addLogMessage("Elements " + element_name + " are found, and to " + index + " index, '" + text + "' is sent");
                }else {
                    Method method = AppiumBy.class.getMethod(ios_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, ios_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    List<WebElement> mobileElements = driverIos.findElements(by);
                    WebElement mobileElement = mobileElements.get(index);
                    mobileElement.sendKeys(text);
                    TestLogger.addLogMessage("Elements " + element_name + " are found, and to " + index + " index, '" + text + "' is sent");
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException | IndexOutOfBoundsException e) {
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                screenshotIos(element_name, ios_locator);
                Assert.fail("Error: " + e.getMessage());
            }
        }
        return this;
    }
    public String getTextFromElementWithIndexAndWait(Integer index) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, IOException {
        String text = null;
        if("android".equalsIgnoreCase(getPlatform())){
            driverAndroid = AndroidSettings.driverAndroid.get();
            try {
                WebDriverWait wait = new WebDriverWait(driverAndroid, Duration.ofSeconds(50));
                if (Objects.equals(android_selector, "xpath")) {
                    Method method = By.class.getMethod(android_selector, String.class);
                    By by = (By) method.invoke(null, android_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    List<WebElement> mobileElements = driverAndroid.findElements(by);
                    WebElement mobileElement = mobileElements.get(index);
                    text = mobileElement.getText();
                    TestLogger.addLogMessage("Elements " + element_name + " are found, and from " + index + " index, '" + text + "' is extracted");
                }else {
                    Method method = AppiumBy.class.getMethod(android_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, android_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    List<WebElement> mobileElements = driverAndroid.findElements(by);
                    WebElement mobileElement = mobileElements.get(index);
                    text = mobileElement.getText();
                    TestLogger.addLogMessage("Elements " + element_name + " are found, and from " + index + " index, '" + text + "' is extracted");
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                screenshotAndroid(element_name, android_locator);
                Assert.fail("Error: " + e.getMessage());
            }
        }
        else if("ios".equalsIgnoreCase(getPlatform())){
            driverIos = IosSettings.driverIos.get();
            try {
                WebDriverWait wait = new WebDriverWait(driverIos, Duration.ofSeconds(50));
                if (Objects.equals(ios_selector, "xpath")) {
                    Method method = By.class.getMethod(ios_selector, String.class);
                    By by = (By) method.invoke(null, ios_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    List<WebElement> mobileElements = driverIos.findElements(by);
                    WebElement mobileElement = mobileElements.get(index);
                    text = mobileElement.getText();
                    TestLogger.addLogMessage("Elements " + element_name + " are found, and from " + index + " index, '" + text + "' is extracted");
                }else {
                    Method method = AppiumBy.class.getMethod(ios_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, ios_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    List<WebElement> mobileElements = driverIos.findElements(by);
                    WebElement mobileElement = mobileElements.get(index);
                    text = mobileElement.getText();
                    TestLogger.addLogMessage("Elements " + element_name + " are found, and from " + index + " index, '" + text + "' is extracted");
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                screenshotIos(element_name, ios_locator);
                Assert.fail("Error: " + e.getMessage());
            }
        }
        return text;
    }
    public UiObject clearTextFromElementWithIndexAndWait(Integer index) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        if("android".equalsIgnoreCase(getPlatform())){
            driverAndroid = AndroidSettings.driverAndroid.get();
            WebDriverWait wait = new WebDriverWait(driverAndroid, Duration.ofSeconds(50));
            try {
                if (Objects.equals(android_selector, "xpath")) {
                    Method method = By.class.getMethod(android_selector, String.class);
                    By by = (By) method.invoke(null, android_locator);
                    wait.until(ExpectedConditions.elementToBeClickable(by));
                    List<WebElement> mobileElements = driverAndroid.findElements(by);
                    WebElement mobileElement = mobileElements.get(index);
                    mobileElement.clear();
                    TestLogger.addLogMessage("The text has been cleared from the " + element_name + " element");
                }
                else {
                    Method method = AppiumBy.class.getMethod(android_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, android_locator);
                    wait.until(ExpectedConditions.elementToBeClickable(by));
                    List<WebElement> mobileElements = driverAndroid.findElements(by);
                    WebElement mobileElement = mobileElements.get(index);
                    mobileElement.clear();
                    TestLogger.addLogMessage("The text has been cleared from the " + element_name + " element");
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                screenshotAndroid(element_name, android_locator);
                Assert.fail("Error: " + e.getMessage());
            }
        }
        else if("ios".equalsIgnoreCase(getPlatform())){
            driverIos = IosSettings.driverIos.get();
            WebDriverWait wait = new WebDriverWait(driverIos, Duration.ofSeconds(50));
            try {
                if (Objects.equals(ios_selector, "xpath")) {
                    Method method = By.class.getMethod(ios_selector, String.class);
                    By by = (By) method.invoke(null, ios_locator);
                    wait.until(ExpectedConditions.elementToBeClickable(by));
                    List<WebElement> mobileElements = driverIos.findElements(by);
                    WebElement mobileElement = mobileElements.get(index);
                    mobileElement.clear();
                    TestLogger.addLogMessage("The text has been cleared from the " + element_name + " element");
                }
                else {
                    Method method = AppiumBy.class.getMethod(ios_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, ios_locator);
                    wait.until(ExpectedConditions.elementToBeClickable(by));
                    List<WebElement> mobileElements = driverIos.findElements(by);
                    WebElement mobileElement = mobileElements.get(index);
                    mobileElement.clear();
                    TestLogger.addLogMessage("The text has been cleared from the " + element_name + " element");
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                screenshotIos(element_name, ios_locator);
                Assert.fail("Error: " + e.getMessage());
            }
        }
        return null;
    }
    public UiObject clickDoubleNestedElement(Integer iosIndexChild, Integer androidIndexChild, Integer iosIndexChildOfChild, Integer androidIndexChildOfChild, UiObject child, UiObject childOfChild) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        this.child_android_selector = child.android_selector;
        this.child_android_locator = child.android_locator;
        this.child_of_child_android_selector = childOfChild.android_selector;
        this.child_of_child_android_locator = childOfChild.android_locator;

        this.child_ios_selector = child.ios_selector;
        this.child_ios_locator = child.ios_locator;
        this.child_of_child_ios_selector = childOfChild.ios_selector;
        this.child_of_child_ios_locator = childOfChild.ios_locator;
        if("android".equalsIgnoreCase(getPlatform())){
            driverAndroid = AndroidSettings.driverAndroid.get();
            try {
                WebDriverWait wait = new WebDriverWait(driverAndroid, Duration.ofSeconds(50));
                if (Objects.equals(android_selector, "xpath")) {
                    Method method = By.class.getMethod(android_selector, String.class);
                    By by = (By) method.invoke(null, android_locator);
                    Method childMethod = By.class.getMethod(child_android_selector, String.class);
                    By childBy = (By) method.invoke(null, child_android_locator);
                    Method childOfChildMethod = By.class.getMethod(child_of_child_android_selector, String.class);
                    By childOfChildBy = (By) method.invoke(null, child_of_child_android_locator);
                    wait.until(ExpectedConditions.elementToBeClickable(by));
                    WebElement parentElement = driverAndroid.findElement(by);
                    List<WebElement> childElements = parentElement.findElements(childBy);
                    WebElement childElement = childElements.get(androidIndexChild);
                    List<WebElement> childOfChildElements = childElement.findElements(childOfChildBy);
                    WebElement childOfChildElement = childOfChildElements.get(androidIndexChildOfChild);
                    childOfChildElement.click();
                    TestLogger.addLogMessage("Nested element " + childOfChild + " is found, and is clicked");
                }else {
                    Method method = AppiumBy.class.getMethod(android_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, android_locator);
                    Method childMethod = AppiumBy.class.getMethod(child_android_selector, String.class);
                    AppiumBy childBy = (AppiumBy) method.invoke(null, child_android_locator);
                    Method childOfChildMethod = AppiumBy.class.getMethod(child_of_child_android_selector, String.class);
                    AppiumBy childOfChildBy = (AppiumBy) method.invoke(null, child_of_child_android_locator);
                    wait.until(ExpectedConditions.elementToBeClickable(by));
                    WebElement parentElement = driverAndroid.findElement(by);
                    List<WebElement> childElements = parentElement.findElements(childBy);
                    WebElement childElement = childElements.get(androidIndexChild);
                    List<WebElement> childOfChildElements = childElement.findElements(childOfChildBy);
                    WebElement childOfChildElement = childOfChildElements.get(androidIndexChildOfChild);
                    childOfChildElement.click();
                    TestLogger.addLogMessage("Nested element " + childOfChild + " is found, and is clicked");
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                screenshotAndroid(element_name, android_locator);
                Assert.fail("Error: " + e.getMessage());
            }
        }
        else if("ios".equalsIgnoreCase(getPlatform())){
            driverIos = IosSettings.driverIos.get();
            try {
                WebDriverWait wait = new WebDriverWait(driverIos, Duration.ofSeconds(50));
                if (Objects.equals(ios_selector, "xpath")) {
                    Method method = By.class.getMethod(ios_selector, String.class);
                    By by = (By) method.invoke(null, ios_locator);
                    Method childMethod = By.class.getMethod(child_ios_selector, String.class);
                    By childBy = (By) method.invoke(null, child_ios_locator);
                    Method childOfChildMethod = By.class.getMethod(child_of_child_ios_selector, String.class);
                    By childOfChildBy = (By) method.invoke(null, child_of_child_ios_locator);
                    wait.until(ExpectedConditions.elementToBeClickable(by));
                    WebElement parentElement = driverIos.findElement(by);
                    List<WebElement> childElements = parentElement.findElements(childBy);
                    WebElement childElement = childElements.get(iosIndexChild);
                    List<WebElement> childOfChildElements = childElement.findElements(childOfChildBy);
                    WebElement childOfChildElement = childOfChildElements.get(iosIndexChildOfChild);
                    childOfChildElement.click();
                    TestLogger.addLogMessage("Nested element " + childOfChild + " is found, and is clicked");
                }else {
                    Method method = AppiumBy.class.getMethod(ios_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, ios_locator);
                    Method childMethod = AppiumBy.class.getMethod(child_ios_selector, String.class);
                    AppiumBy childBy = (AppiumBy) method.invoke(null, child_ios_locator);
                    Method childOfChildMethod = AppiumBy.class.getMethod(child_of_child_ios_selector, String.class);
                    AppiumBy childOfChildBy = (AppiumBy) method.invoke(null, child_of_child_ios_locator);
                    wait.until(ExpectedConditions.elementToBeClickable(by));
                    WebElement parentElement = driverIos.findElement(by);
                    List<WebElement> childElements = parentElement.findElements(childBy);
                    WebElement childElement = childElements.get(iosIndexChild);
                    List<WebElement> childOfChildElements = childElement.findElements(childOfChildBy);
                    WebElement childOfChildElement = childOfChildElements.get(iosIndexChildOfChild);
                    childOfChildElement.click();
                    TestLogger.addLogMessage("Nested element " + childOfChild + " is found, and is clicked");
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                screenshotIos(element_name, ios_locator);
                Assert.fail("Error: " + e.getMessage());
            }
        }
        return this;
    }
    public String getTextFromDoubleNestedElement(Integer iosIndexChild, Integer androidIndexChild, Integer iosIndexChildOfChild, Integer androidIndexChildOfChild, UiObject child, UiObject childOfChild) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        String text = "";
        this.child_android_selector = child.android_selector;
        this.child_android_locator = child.android_locator;
        this.child_of_child_android_selector = childOfChild.android_selector;
        this.child_of_child_android_locator = childOfChild.android_locator;

        this.child_ios_selector = child.ios_selector;
        this.child_ios_locator = child.ios_locator;
        this.child_of_child_ios_selector = childOfChild.ios_selector;
        this.child_of_child_ios_locator = childOfChild.ios_locator;
        if("android".equalsIgnoreCase(getPlatform())){
            driverAndroid = AndroidSettings.driverAndroid.get();
            try {
                WebDriverWait wait = new WebDriverWait(driverAndroid, Duration.ofSeconds(50));
                if (Objects.equals(android_selector, "xpath")) {
                    Method method = By.class.getMethod(android_selector, String.class);
                    By by = (By) method.invoke(null, android_locator);
                    Method childMethod = By.class.getMethod(child_android_selector, String.class);
                    By childBy = (By) childMethod.invoke(null, child_android_locator);
                    Method childOfChildMethod = By.class.getMethod(child_of_child_android_selector, String.class);
                    By childOfChildBy = (By) childOfChildMethod.invoke(null, child_of_child_android_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    WebElement parentElement = driverAndroid.findElement(by);
                    List<WebElement> childElements = parentElement.findElements(childBy);
                    WebElement childElement = childElements.get(androidIndexChild);
                    List<WebElement> childOfChildElements = childElement.findElements(childOfChildBy);
                    WebElement childOfChildElement = childOfChildElements.get(androidIndexChildOfChild);
                    text = childOfChildElement.getText();
                    TestLogger.addLogMessage("Nested element " + childOfChild + " is found, and '" + text + "' is extracted");
                }else {
                    Method method = AppiumBy.class.getMethod(android_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, android_locator);
                    Method childMethod = AppiumBy.class.getMethod(child_android_selector, String.class);
                    AppiumBy childBy = (AppiumBy) childMethod.invoke(null, child_android_locator);
                    Method childOfChildMethod = AppiumBy.class.getMethod(child_of_child_android_selector, String.class);
                    AppiumBy childOfChildBy = (AppiumBy) childOfChildMethod.invoke(null, child_of_child_android_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    WebElement parentElement = driverAndroid.findElement(by);
                    List<WebElement> childElements = parentElement.findElements(childBy);
                    WebElement childElement = childElements.get(androidIndexChild);
                    List<WebElement> childOfChildElements = childElement.findElements(childOfChildBy);
                    WebElement childOfChildElement = childOfChildElements.get(androidIndexChildOfChild);
                    text = childOfChildElement.getText();
                    TestLogger.addLogMessage("Nested element " + childOfChild + " is found, and '" + text + "' is extracted");
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                screenshotAndroid(element_name, android_locator);
                Assert.fail("Error: " + e.getMessage());
            }
        }
        else if("ios".equalsIgnoreCase(getPlatform())){
            driverIos = IosSettings.driverIos.get();
            try {
                WebDriverWait wait = new WebDriverWait(driverIos, Duration.ofSeconds(50));
                if (Objects.equals(ios_selector, "xpath")) {
                    Method method = By.class.getMethod(ios_selector, String.class);
                    By by = (By) method.invoke(null, ios_locator);
                    Method childMethod = By.class.getMethod(child_ios_selector, String.class);
                    By childBy = (By) childMethod.invoke(null, child_ios_locator);
                    Method childOfChildMethod = By.class.getMethod(child_of_child_ios_selector, String.class);
                    By childOfChildBy = (By) childOfChildMethod.invoke(null, child_of_child_ios_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    WebElement parentElement = driverIos.findElement(by);
                    List<WebElement> childElements = parentElement.findElements(childBy);
                    WebElement childElement = childElements.get(iosIndexChild);
                    List<WebElement> childOfChildElements = childElement.findElements(childOfChildBy);
                    WebElement childOfChildElement = childOfChildElements.get(iosIndexChildOfChild);
                    text = childOfChildElement.getText();
                    TestLogger.addLogMessage("Nested element " + childOfChild + " is found, and '" + text + "' is extracted");
                }else {
                    Method method = AppiumBy.class.getMethod(ios_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, ios_locator);
                    Method childMethod = AppiumBy.class.getMethod(child_ios_selector, String.class);
                    AppiumBy childBy = (AppiumBy) childMethod.invoke(null, child_ios_locator);
                    Method childOfChildMethod = AppiumBy.class.getMethod(child_of_child_ios_selector, String.class);
                    AppiumBy childOfChildBy = (AppiumBy) childOfChildMethod.invoke(null, child_of_child_ios_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    WebElement parentElement = driverIos.findElement(by);
                    List<WebElement> childElements = parentElement.findElements(childBy);
                    WebElement childElement = childElements.get(iosIndexChild);
                    List<WebElement> childOfChildElements = childElement.findElements(childOfChildBy);
                    WebElement childOfChildElement = childOfChildElements.get(iosIndexChildOfChild);
                    text = childOfChildElement.getText();
                    TestLogger.addLogMessage("Nested element " + childOfChild + " is found, and '" + text + "' is extracted");
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                screenshotIos(element_name, ios_locator);
                Assert.fail("Error: " + e.getMessage());
            }
        }
        return text;
    }
    public UiObject sendTextToDoubleNestedElement(Integer iosIndexChild, Integer androidIndexChild, Integer iosIndexChildOfChild, Integer androidIndexChildOfChild, UiObject child, UiObject childOfChild, String text) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        this.child_android_selector = child.android_selector;
        this.child_android_locator = child.android_locator;
        this.child_of_child_android_selector = childOfChild.android_selector;
        this.child_of_child_android_locator = childOfChild.android_locator;

        this.child_ios_selector = child.ios_selector;
        this.child_ios_locator = child.ios_locator;
        this.child_of_child_ios_selector = childOfChild.ios_selector;
        this.child_of_child_ios_locator = childOfChild.ios_locator;
        if("android".equalsIgnoreCase(getPlatform())){
            driverAndroid = AndroidSettings.driverAndroid.get();
            try {
                WebDriverWait wait = new WebDriverWait(driverAndroid, Duration.ofSeconds(50));
                if (Objects.equals(android_selector, "xpath")) {
                    Method method = By.class.getMethod(android_selector, String.class);
                    By by = (By) method.invoke(null, android_locator);
                    Method childMethod = By.class.getMethod(child_android_selector, String.class);
                    By childBy = (By) method.invoke(null, child_android_locator);
                    Method childOfChildMethod = By.class.getMethod(child_of_child_android_selector, String.class);
                    By childOfChildBy = (By) method.invoke(null, child_of_child_android_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    WebElement parentElement = driverAndroid.findElement(by);
                    List<WebElement> childElements = parentElement.findElements(childBy);
                    WebElement childElement = childElements.get(androidIndexChild);
                    List<WebElement> childOfChildElements = childElement.findElements(childOfChildBy);
                    WebElement childOfChildElement = childOfChildElements.get(androidIndexChildOfChild);
                    childOfChildElement.sendKeys(text);
                    TestLogger.addLogMessage("Nested element " + childOfChild + " is found, and '" + text + "' is sent to element");
                }else {
                    Method method = AppiumBy.class.getMethod(android_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, android_locator);
                    Method childMethod = AppiumBy.class.getMethod(child_android_selector, String.class);
                    AppiumBy childBy = (AppiumBy) method.invoke(null, child_android_locator);
                    Method childOfChildMethod = AppiumBy.class.getMethod(child_of_child_android_selector, String.class);
                    AppiumBy childOfChildBy = (AppiumBy) method.invoke(null, child_of_child_android_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    WebElement parentElement = driverAndroid.findElement(by);
                    List<WebElement> childElements = parentElement.findElements(childBy);
                    WebElement childElement = childElements.get(androidIndexChild);
                    List<WebElement> childOfChildElements = childElement.findElements(childOfChildBy);
                    WebElement childOfChildElement = childOfChildElements.get(androidIndexChildOfChild);
                    childOfChildElement.sendKeys(text);
                    TestLogger.addLogMessage("Nested element " + childOfChild + " is found, and '" + text + "' is sent to element");
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                boolean fail = !true;
                TestLogger.addLogMessage("Elements " + element_name + " are not found");
                screenshotAndroid(element_name, android_locator);
                assert  fail = false : "Error" + e;
            }
        }
        else if("ios".equalsIgnoreCase(getPlatform())){
            driverIos = IosSettings.driverIos.get();
            try {
                WebDriverWait wait = new WebDriverWait(driverIos, Duration.ofSeconds(50));
                if (Objects.equals(ios_selector, "xpath")) {
                    Method method = AppiumBy.class.getMethod(ios_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, ios_locator);
                    Method childMethod = AppiumBy.class.getMethod(child_ios_selector, String.class);
                    AppiumBy childBy = (AppiumBy) method.invoke(null, child_ios_locator);
                    Method childOfChildMethod = By.class.getMethod(child_of_child_ios_selector, String.class);
                    AppiumBy childOfChildBy = (AppiumBy) method.invoke(null, child_of_child_ios_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    WebElement parentElement = driverIos.findElement(by);
                    List<WebElement> childElements = parentElement.findElements(childBy);
                    WebElement childElement = childElements.get(iosIndexChild);
                    List<WebElement> childOfChildElements = childElement.findElements(childOfChildBy);
                    WebElement childOfChildElement = childOfChildElements.get(iosIndexChildOfChild);
                    childOfChildElement.sendKeys(text);
                    TestLogger.addLogMessage("Nested element " + childOfChild + " is found, and '" + text + "' is sent to element");
                }else {
                    Method method = AppiumBy.class.getMethod(ios_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, ios_locator);
                    Method childMethod = AppiumBy.class.getMethod(child_ios_selector, String.class);
                    AppiumBy childBy = (AppiumBy) method.invoke(null, child_ios_locator);
                    Method childOfChildMethod = AppiumBy.class.getMethod(child_of_child_ios_selector, String.class);
                    AppiumBy childOfChildBy = (AppiumBy) method.invoke(null, child_of_child_ios_locator);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    WebElement parentElement = driverIos.findElement(by);
                    List<WebElement> childElements = parentElement.findElements(childBy);
                    WebElement childElement = childElements.get(iosIndexChild);
                    List<WebElement> childOfChildElements = childElement.findElements(childOfChildBy);
                    WebElement childOfChildElement = childOfChildElements.get(iosIndexChildOfChild);
                    childOfChildElement.sendKeys(text);
                    TestLogger.addLogMessage("Nested element " + childOfChild + " is found, and '" + text + "' is sent to element");
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                boolean fail = !true;
                TestLogger.addLogMessage("Element " + element_name + " are not found");
                screenshotIos(element_name,ios_locator);
                assert  fail = false : "Error" + e;
            }
        }
        return this;
    }
    public UiObject clickElementThatContainsText(String text) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        if("android".equalsIgnoreCase(getPlatform())){
            driverAndroid = AndroidSettings.driverAndroid.get();
            WebDriverWait wait = new WebDriverWait(driverAndroid, Duration.ofSeconds(50));
            String android_xpath_locator = "//" + android_locator + "[contains(text()," + text + ")]";
            try {
                if (Objects.equals(android_selector, "xpath")) {
                    Method method = By.class.getMethod(android_selector, String.class);
                    By by = (By) method.invoke(null, android_xpath_locator);
                    wait.until(ExpectedConditions.elementToBeClickable(by));
                    driverAndroid.findElement(by).click();
                    TestLogger.addLogMessage("Element " + element_name + " is clicked");
                }else {
                    Method method = AppiumBy.class.getMethod(android_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, android_locator);
                    wait.until(ExpectedConditions.elementToBeClickable(by));
                    driverAndroid.findElement(by).click();
                    TestLogger.addLogMessage("Element " + element_name + " is clicked");
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                screenshotAndroid(element_name, android_locator);
                Assert.fail("Error: " + e.getMessage());
            }
        }
        else if("ios".equalsIgnoreCase(getPlatform())){
            driverIos = IosSettings.driverIos.get();
            WebDriverWait wait = new WebDriverWait(driverIos, Duration.ofSeconds(50));
            String ios_xpath_locator = null;
            if (Objects.equals(ios_selector, "xpath")) {
                ios_xpath_locator = "//" + ios_locator + "[contains(text()," + text + ")]";
            }
            else if (Objects.equals(ios_selector, "iOSNsPredicateString")) {
                ios_xpath_locator = "type == '" + ios_locator + "' AND name CONTAINS '" + text + "'";
            }
            else if (Objects.equals(ios_selector, "iOSClassChain")) {
                ios_xpath_locator = "**/" + ios_locator+ "[`label CONTAINS \"" + text + "\" `]";
            }
            else if (Objects.equals(ios_selector, "accessibilityId")) {
                ios_xpath_locator = text;
            }

            try {
                if (Objects.equals(ios_selector, "xpath")) {
                    Method method = By.class.getMethod(ios_selector, String.class);
                    By by = (By) method.invoke(null, ios_xpath_locator);
                    wait.until(ExpectedConditions.elementToBeClickable(by));
                    driverIos.findElement(by).click();
                    TestLogger.addLogMessage("Element " + element_name + " is clicked");
                }else {
                    Method method = AppiumBy.class.getMethod(ios_selector, String.class);
                    AppiumBy by = (AppiumBy) method.invoke(null, ios_xpath_locator);
                    wait.until(ExpectedConditions.elementToBeClickable(by));
                    driverIos.findElement(by).click();
                    TestLogger.addLogMessage("Element " + element_name + " is clicked");
                }
            }
            catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                TestLogger.addLogMessage("Element " + element_name + " is not found");
                screenshotIos(element_name, ios_locator);
                Assert.fail("Error: " + e.getMessage());
            }
        }
        return null;
    }
}


