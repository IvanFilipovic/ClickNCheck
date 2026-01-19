package com.exit3.testing;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

public class AppiumManager {

    private static AppiumDriverLocalService service;
    static String appPath = new File("app/app.apk").getAbsolutePath();
    /** Starting appium server on
     * @param ipAddress,port
     *
     * **/
    public static void startAppiumServer(String ipAddress, String port) {
        AppiumServiceBuilder builder = new AppiumServiceBuilder()
                .withIPAddress(ipAddress)
                .withAppiumJS(new File("/opt/homebrew/bin/appium"))
                .usingPort(Integer.parseInt(port))
                .withArgument(GeneralServerFlag.LOG_LEVEL, "error");


        service = AppiumDriverLocalService.buildService(builder);

        // Start the Appium server
        service.start();
        System.out.println("Appium server started on port: " + port);

        // Ensure the server started successfully
        if (!service.isRunning()) {
            throw new RuntimeException("Appium server failed to start.");
        }
    }
    // Stop the Appium server
    public static void stopAppiumServer() {
        if (service != null && service.isRunning()) {
            service.stop();
            System.out.println("Appium server stopped.");
        }
    }
    // Start Android emulator
    public static void startAndroidEmulator(String emulatorName, String port) throws IOException {
        String[] command = {"emulator", "-avd", emulatorName, "-port", port,
                "-memory", "2048", "-cores", "2",
                "-writable-system", "-no-snapshot-load", "-no-boot-anim",
                "-verbose"};

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.start();
        System.out.println("Starting emulator: " + emulatorName);

        // Wait until the emulator is fully booted
        waitForEmulatorToBoot();
    }
    // Wait until the emulator is booted
    public static void waitForEmulatorToBoot() {
        try {
            System.out.println("Waiting for emulator to boot...");
            Process process;
            while (true) {
                process = Runtime.getRuntime().exec("adb shell getprop sys.boot_completed");
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String bootStatus = reader.readLine();

                // Check if boot completed
                if ("1".equals(bootStatus)) {
                    System.out.println("Emulator is ready!");
                    break;
                }

                // Wait a bit before checking again
                Thread.sleep(5000); // Check every 5 seconds
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void installApp(String emulatorName) {
        // Construct the ADB command for installing the APK
        String command = "adb -s " + emulatorName + " install " + appPath;
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
            } else if (os.contains("mac") || os.contains("darwin")) {
                // For macOS, use 'nohup' to detach the process
                processBuilder = new ProcessBuilder("nohup", "/bin/bash", "-c", command);
            } else if (os.contains("nix") || os.contains("nux")) {
                // For Linux, use 'nohup' to detach the process
                processBuilder = new ProcessBuilder("nohup", "/bin/bash", "-c", command);
            } else {
                throw new UnsupportedOperationException("Unsupported OS: " + os);
            }

            // Start the process
            processBuilder.redirectErrorStream(true);
            processBuilder.start();

            System.out.println("Command started: " + command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
