package com.exit3.testing;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

/**
 * Manages Appium server lifecycle and Android emulator operations.
 * <p>
 * This class provides utilities for starting/stopping Appium servers,
 * managing Android emulators, and installing applications. It supports
 * configurable Appium paths and includes proper resource management
 * and timeout handling.
 * </p>
 *
 * <h2>Appium Server Management:</h2>
 * <pre>{@code
 * // Start Appium server on localhost:4723
 * AppiumManager.startAppiumServer("127.0.0.1", "4723");
 *
 * // ... run tests ...
 *
 * // Stop Appium server
 * AppiumManager.stopAppiumServer();
 * }</pre>
 *
 * <h2>Configuration:</h2>
 * <ul>
 *   <li><b>APPIUM_PATH</b> environment variable - Path to Appium executable</li>
 *   <li><b>appium.path</b> system property - Path to Appium executable</li>
 *   <li>Default: "appium" (assumes Appium is in system PATH)</li>
 * </ul>
 *
 * <h2>Emulator Management:</h2>
 * <pre>{@code
 * // Start emulator (waits up to 5 minutes for boot)
 * AppiumManager.startAndroidEmulator("Pixel_4_API_30", "5554");
 *
 * // Install app on emulator
 * AppiumManager.installApp("emulator-5554");
 * }</pre>
 *
 * <h2>Constants:</h2>
 * <ul>
 *   <li><b>EMULATOR_BOOT_TIMEOUT_SECONDS</b> - 300 seconds (5 minutes)</li>
 *   <li><b>EMULATOR_BOOT_CHECK_INTERVAL_MS</b> - 5000 ms (5 seconds)</li>
 * </ul>
 *
 * @author ClickNCheck Framework
 * @version 2.0
 * @since 1.0
 */
public class AppiumManager {
    private static final Logger logger = LoggerFactory.getLogger(AppiumManager.class);

    private static AppiumDriverLocalService service;
    static String appPath = new File("app/app.apk").getAbsolutePath();

    /**
     * Starts an Appium server on the specified IP address and port.
     * <p>
     * The Appium executable path can be configured via:
     * <ul>
     *   <li>System property: <code>-Dappium.path=/path/to/appium</code></li>
     *   <li>Environment variable: <code>APPIUM_PATH=/path/to/appium</code></li>
     *   <li>Default: <code>appium</code> (assumes it's in system PATH)</li>
     * </ul>
     * </p>
     *
     * @param ipAddress the IP address to bind Appium server (e.g., "127.0.0.1")
     * @param port the port number for Appium server (e.g., "4723")
     * @throws RuntimeException if Appium server fails to start
     *
     * @see #stopAppiumServer()
     */
    public static void startAppiumServer(String ipAddress, String port) {
        // Use environment variable or system property for Appium path
        // Defaults to "appium" (assumes it's in PATH)
        String appiumPath = System.getProperty("appium.path",
            System.getenv().getOrDefault("APPIUM_PATH", "appium"));

        AppiumServiceBuilder builder = new AppiumServiceBuilder()
                .withIPAddress(ipAddress)
                .withAppiumJS(new File(appiumPath))
                .usingPort(Integer.parseInt(port))
                .withArgument(GeneralServerFlag.LOG_LEVEL, "error");


        service = AppiumDriverLocalService.buildService(builder);

        // Start the Appium server
        service.start();
        logger.info("Appium server started on {}:{}", ipAddress, port);

        // Ensure the server started successfully
        if (!service.isRunning()) {
            logger.error("Appium server failed to start on port {}", port);
            throw new RuntimeException("Appium server failed to start on port: " + port);
        }
    }

    /**
     * Stops the Appium server if it's currently running.
     * <p>
     * This method is safe to call even if the server was never started
     * or has already been stopped.
     * </p>
     *
     * @see #startAppiumServer(String, String)
     */
    public static void stopAppiumServer() {
        if (service != null && service.isRunning()) {
            service.stop();
            logger.info("Appium server stopped successfully");
        } else {
            logger.debug("Appium server was not running or already stopped");
        }
    }
    /**
     * Timeout for emulator boot completion in seconds.
     * Default: 300 seconds (5 minutes)
     */
    private static final int EMULATOR_BOOT_TIMEOUT_SECONDS = 300;

    /**
     * Interval between emulator boot status checks in milliseconds.
     * Default: 5000 ms (5 seconds)
     */
    private static final int EMULATOR_BOOT_CHECK_INTERVAL_MS = 5000;

    /**
     * Starts an Android emulator and waits for it to fully boot.
     * <p>
     * This method launches the specified emulator AVD with optimized settings
     * (no animations, boot snapshots disabled) and waits up to 5 minutes
     * for the emulator to complete booting.
     * </p>
     *
     * @param emulatorName the AVD name (e.g., "Pixel_4_API_30")
     * @param port the emulator port (e.g., "5554")
     * @throws IOException if the emulator process fails to start
     * @throws RuntimeException if emulator fails to boot within timeout
     *
     * @see #waitForEmulatorToBoot()
     */
    public static void startAndroidEmulator(String emulatorName, String port) throws IOException {
        String[] command = {"emulator", "-avd", emulatorName, "-port", port,
                "-memory", "2048", "-cores", "2",
                "-writable-system", "-no-snapshot-load", "-no-boot-anim",
                "-verbose"};

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.start();
        logger.info("Starting Android emulator: {} on port {}", emulatorName, port);

        // Wait until the emulator is fully booted
        waitForEmulatorToBoot();
    }

    /**
     * Waits for an Android emulator to complete booting with timeout.
     * <p>
     * Polls the emulator's boot status every 5 seconds using
     * <code>adb shell getprop sys.boot_completed</code>. Returns when
     * boot is complete or throws exception after 5 minutes timeout.
     * </p>
     * <p>
     * <b>Resource Management:</b> Properly closes BufferedReader and
     * destroys Process in finally block to prevent resource leaks.
     * </p>
     *
     * @throws RuntimeException if emulator fails to boot within
     *         {@value #EMULATOR_BOOT_TIMEOUT_SECONDS} seconds, or if
     *         interrupted while waiting
     *
     * @see #EMULATOR_BOOT_TIMEOUT_SECONDS
     * @see #EMULATOR_BOOT_CHECK_INTERVAL_MS
     */
    public static void waitForEmulatorToBoot() {
        long startTime = System.currentTimeMillis();
        long timeout = EMULATOR_BOOT_TIMEOUT_SECONDS * 1000L;

        logger.info("Waiting for emulator to boot (timeout: {}s)...", EMULATOR_BOOT_TIMEOUT_SECONDS);

        while (System.currentTimeMillis() - startTime < timeout) {
            Process process = null;
            BufferedReader reader = null;
            try {
                process = Runtime.getRuntime().exec("adb shell getprop sys.boot_completed");
                reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String bootStatus = reader.readLine();

                // Check if boot completed
                if ("1".equals(bootStatus)) {
                    long bootTime = (System.currentTimeMillis() - startTime) / 1000;
                    logger.info("Emulator is ready! Boot time: {}s", bootTime);
                    return;
                }

                logger.debug("Emulator still booting... (elapsed: {}s)",
                    (System.currentTimeMillis() - startTime) / 1000);

                // Wait before checking again
                Thread.sleep(EMULATOR_BOOT_CHECK_INTERVAL_MS);
            } catch (IOException e) {
                logger.warn("Error checking emulator boot status: {}", e.getMessage());
            } catch (InterruptedException e) {
                logger.warn("Interrupted while waiting for emulator to boot", e);
                Thread.currentThread().interrupt();
                throw new RuntimeException("Interrupted while waiting for emulator to boot", e);
            } finally {
                // Clean up resources
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        logger.debug("Error closing reader", e);
                    }
                }
                if (process != null) {
                    process.destroy();
                }
            }
        }

        logger.error("Emulator failed to boot within {} seconds", EMULATOR_BOOT_TIMEOUT_SECONDS);
        throw new RuntimeException("Emulator failed to boot within " + EMULATOR_BOOT_TIMEOUT_SECONDS + " seconds");
    }

    /**
     * Installs the application APK on the specified emulator.
     * <p>
     * Uses <code>adb install</code> command to install the app located at
     * the path specified by {@link #appPath}.
     * </p>
     *
     * @param emulatorName the emulator serial/name (e.g., "emulator-5554")
     *
     * @see #executeShellCommand(String)
     */
    public static void installApp(String emulatorName) {
        // Construct the ADB command for installing the APK
        String command = "adb -s " + emulatorName + " install " + appPath;
        logger.info("Installing app on emulator {}: {}", emulatorName, appPath);
        // Execute the command
        executeShellCommand(command);
    }

    /**
     * Executes a shell command in a detached process.
     * <p>
     * Detects the operating system and uses the appropriate method to
     * run the command in the background:
     * <ul>
     *   <li><b>Windows:</b> Uses <code>cmd.exe /c start</code></li>
     *   <li><b>macOS:</b> Uses <code>nohup /bin/bash -c</code></li>
     *   <li><b>Linux:</b> Uses <code>nohup /bin/bash -c</code></li>
     * </ul>
     * </p>
     *
     * @param command the shell command to execute
     * @throws UnsupportedOperationException if the OS is not supported
     * @throws RuntimeException if command execution fails
     */
    private static void executeShellCommand(String command) {
        try {
            // Detect the OS and modify the command to run detached
            String os = System.getProperty("os.name").toLowerCase();
            ProcessBuilder processBuilder;

            if (os.contains("win")) {
                // For Windows, use 'start' to launch an independent process
                processBuilder = new ProcessBuilder("cmd.exe", "/c", "start", command);
                logger.debug("Executing command on Windows: {}", command);
            } else if (os.contains("mac") || os.contains("darwin")) {
                // For macOS, use 'nohup' to detach the process
                processBuilder = new ProcessBuilder("nohup", "/bin/bash", "-c", command);
                logger.debug("Executing command on macOS: {}", command);
            } else if (os.contains("nix") || os.contains("nux")) {
                // For Linux, use 'nohup' to detach the process
                processBuilder = new ProcessBuilder("nohup", "/bin/bash", "-c", command);
                logger.debug("Executing command on Linux: {}", command);
            } else {
                logger.error("Unsupported operating system: {}", os);
                throw new UnsupportedOperationException("Unsupported OS: " + os);
            }

            // Start the process
            processBuilder.redirectErrorStream(true);
            processBuilder.start();

            logger.info("Shell command started successfully: {}", command);
        } catch (IOException e) {
            logger.error("Failed to execute shell command: {}", command, e);
            throw new RuntimeException("Failed to execute shell command: " + command, e);
        }
    }
}
