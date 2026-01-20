package com.exit3.testing;

/**
 * Configuration class for test framework settings.
 * Values can be configured via system properties or use sensible defaults.
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
