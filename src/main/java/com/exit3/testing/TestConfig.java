package com.exit3.testing;

/**
 * Centralized configuration class for the ClickNCheck mobile automation framework.
 * <p>
 * This class provides configurable constants for wait timeouts, screenshot management,
 * and other framework settings. All values can be customized via system properties,
 * making the framework flexible for different test environments.
 * </p>
 *
 * <h2>Usage Examples:</h2>
 * <pre>{@code
 * // Use default values
 * WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TestConfig.DEFAULT_WAIT));
 *
 * // Configure via Maven
 * mvn test -Ddefault.wait=40 -Dscreenshot.dir=/tmp/screenshots
 *
 * // Configure via code (before tests run)
 * System.setProperty("default.wait", "40");
 * System.setProperty("screenshot.dir", "/tmp/screenshots");
 * }</pre>
 *
 * <h2>Available Configuration Properties:</h2>
 * <ul>
 *   <li><b>default.wait</b> - Default wait timeout in seconds (default: 30)</li>
 *   <li><b>long.wait</b> - Long wait timeout in seconds (default: 60)</li>
 *   <li><b>short.wait</b> - Short wait timeout in seconds (default: 10)</li>
 *   <li><b>screenshot.dir</b> - Screenshot directory path (default: target/screenshots)</li>
 *   <li><b>screenshot.retention.days</b> - Days to keep screenshots (default: 7)</li>
 * </ul>
 *
 * @author ClickNCheck Framework
 * @version 1.0
 * @since 1.0
 */
public class TestConfig {
    /**
     * Default wait timeout for element operations (in seconds).
     * Configure via: -Ddefault.wait=30
     * Default: 30 seconds
     */
    public static final int DEFAULT_WAIT = Integer.parseInt(
        System.getProperty("default.wait", "30")
    );

    /**
     * Long wait timeout for slow operations (in seconds).
     * Configure via: -Dlong.wait=60
     * Default: 60 seconds
     */
    public static final int LONG_WAIT = Integer.parseInt(
        System.getProperty("long.wait", "60")
    );

    /**
     * Short wait timeout for quick checks (in seconds).
     * Configure via: -Dshort.wait=10
     * Default: 10 seconds
     */
    public static final int SHORT_WAIT = Integer.parseInt(
        System.getProperty("short.wait", "10")
    );

    /**
     * Screenshot directory path.
     * Configure via: -Dscreenshot.dir=target/screenshots
     * Default: target/screenshots
     */
    public static final String SCREENSHOT_DIR = System.getProperty(
        "screenshot.dir", "target/screenshots"
    );

    /**
     * Number of days to keep old screenshots before cleanup.
     * Configure via: -Dscreenshot.retention.days=7
     * Default: 7 days
     */
    public static final int SCREENSHOT_RETENTION_DAYS = Integer.parseInt(
        System.getProperty("screenshot.retention.days", "7")
    );
}
