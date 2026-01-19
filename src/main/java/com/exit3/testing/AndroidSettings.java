package com.exit3.testing;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class AndroidSettings extends AppiumManager {
    public static URL url;
    public static ThreadLocal<AndroidDriver> driverAndroid = new ThreadLocal<>();
    public static DesiredCapabilities capabilities;
    public static AndroidDriver initialize(String phoneName, String phoneVersion, String ipAddress, String port) throws IOException, InterruptedException {
        startAppiumServer(ipAddress,port);

        final String URL_STRING = "http://localhost:" + port;
        url = new URL(URL_STRING);

        capabilities = new DesiredCapabilities();
        capabilities.setCapability("useNewWDA", false);
        capabilities.setCapability("noReset", true);
        capabilities.setCapability("appium:automationName", "Uiautomator2");
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("appium:deviceName", phoneName);
        capabilities.setCapability("appium:platformVersion", phoneVersion);
        capabilities.setCapability("useFirstMatch", true);
        capabilities.setCapability("appium:waitForIdleTimeout", 1.0);
        capabilities.setCapability("appium:settings[reduceMotion]", true);
        capabilities.setCapability("appium:settings[animationCoolOffTimeout]", 0);
        capabilities.setCapability("appium:settings[customSnapshotTimeout]", 2);
        capabilities.setCapability("appium:settings[waitForQuiescence]", false);
        capabilities.setCapability("appium:settings[snapshotMaxDepth]", 30);
        capabilities.setCapability("appium:settings[pageSourceExcludedAttributes]", "visible,enabled,x,y,width,height");
        // capabilities.setCapability("appium:app", appPath);
        // capabilities.setCapability("appium:appPackage", appPackage);
        // capabilities.setCapability("appium:appActivity", appActivity);


        driverAndroid.set(new AndroidDriver(url, capabilities));
        return driverAndroid.get();
    }
}
