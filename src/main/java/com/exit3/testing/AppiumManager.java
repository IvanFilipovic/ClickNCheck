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

public class AppiumManager {
    private static final Logger logger = LoggerFactory.getLogger(AppiumManager.class);

    private static AppiumDriverLocalService service;
    static String appPath = new File("app/app.apk").getAbsolutePath();
    /** Starting appium server on
     * @param ipAddress,port
     *
     * **/
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
    // Stop the Appium server
    public static void stopAppiumServer() {
        if (service != null && service.isRunning()) {
            service.stop();
            logger.info("Appium server stopped successfully");
        } else {
            logger.debug("Appium server was not running or already stopped");
        }
    }
    // Constants for emulator management
    private static final int EMULATOR_BOOT_TIMEOUT_SECONDS = 300; // 5 minutes
    private static final int EMULATOR_BOOT_CHECK_INTERVAL_MS = 5000; // 5 seconds

    // Start Android emulator
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

    // Wait until the emulator is booted with timeout
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
    public static void installApp(String emulatorName) {
        // Construct the ADB command for installing the APK
        String command = "adb -s " + emulatorName + " install " + appPath;
        logger.info("Installing app on emulator {}: {}", emulatorName, appPath);
        // Execute the command
        executeShellCommand(command);
    }

    // Execute shell commands
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
