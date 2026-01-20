package com.exit3.testing;

import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;

import static com.exit3.testing.AppiumManager.startAppiumServer;


public class IosSettings {
    public static URL url;
    public static DesiredCapabilities capabilities;
    public static ThreadLocal<IOSDriver> driverIos = new ThreadLocal<>();
    public static IOSDriver initialize(String appName, String appPackage, String phoneName, String phoneId, String phoneVersion, String xcodeOrgId, String xcodeSigningId, String ipAddress, String port) throws MalformedURLException, InterruptedException {
        startAppiumServer(ipAddress,port);

        final String URL_STRING = "http://localhost:" + port;
        url = new URL(URL_STRING);
        capabilities = new DesiredCapabilities();
        capabilities.setCapability("appium:automationName", "XCUITest");
        capabilities.setCapability("useNewWDA", false);
        capabilities.setCapability("appium:noReset", true);
        capabilities.setCapability("appium:includeSafariInWebviews", false);
        capabilities.setCapability("appium:appName", appName);
        capabilities.setCapability("appium:bundleId", appPackage);
        capabilities.setCapability("appium:deviceName", phoneName);
        capabilities.setCapability("platformName", "iOS");
        capabilities.setCapability("appium:platformVersion", phoneVersion);
        capabilities.setCapability("appium:udid", phoneId);
        capabilities.setCapability("useFirstMatch", true);
        capabilities.setCapability("appium:waitForIdleTimeout", 0);
        capabilities.setCapability("appium:settings[reduceMotion]", true);
        capabilities.setCapability("appium:settings[animationCoolOffTimeout]", 1);
        capabilities.setCapability("appium:settings[customSnapshotTimeout]", 2);
        capabilities.setCapability("appium:settings[waitForQuiescence]", false);
        capabilities.setCapability("appium:settings[snapshotMaxDepth]", 30);
        capabilities.setCapability("appium:settings[pageSourceExcludedAttributes]", "visible,enabled,x,y,width,height");
        capabilities.setCapability("appium:UpdatedWDABundleID", "com.shape.WebDriverAgentRunner");

        driverIos.set(new IOSDriver(url, capabilities));
        return driverIos.get();
    }
}
