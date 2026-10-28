package net.therap.mobile.driver;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import net.therap.mobile.config.ConfigurationManager;
import net.therap.mobile.config.ConfigurationProps;
import org.openqa.selenium.OutputType;

import java.net.URI;
import java.time.Duration;

public final class DriverFactory {

    private static final ThreadLocal<AndroidDriver> DRIVER = new ThreadLocal<>();

    private DriverFactory() {}

    public static AndroidDriver createDriver() {
        ConfigurationProps config = ConfigurationManager.getConfigurationProps();

        UiAutomator2Options options = new UiAutomator2Options();
        options.setPlatformName(config.appiumPlatformName());
        options.setAutomationName(config.appiumAutomationName());
        options.setDeviceName(config.appiumDeviceName());
        options.setNoReset(config.appiumNoReset());

        try {
            AndroidDriver driver = new AndroidDriver(URI.create(config.appiumServerUrl()).toURL(), options);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(config.appiumTimeoutSeconds()));
            DRIVER.set(driver);
            return driver;
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize AndroidDriver on " + config.appiumServerUrl(), e);
        }
    }

    public static AndroidDriver getDriver() {
        return DRIVER.get();
    }

    public static void quitDriver() {
        AndroidDriver driver = DRIVER.get();
        if (driver != null) {
            try {
                driver.quit();
            } finally {
                DRIVER.remove();
            }
        }
    }

    public static byte[] captureScreenshot() {
        AndroidDriver driver = DRIVER.get();
        if (driver != null) {
            return driver.getScreenshotAs(OutputType.BYTES);
        }
        return new byte[0];
    }
}
