package net.therap.mobile.report;

import com.google.common.collect.ImmutableMap;
import io.qameta.allure.Allure;
import net.therap.mobile.config.ConfigurationManager;
import net.therap.mobile.config.ConfigurationProps;
import net.therap.mobile.driver.DriverFactory;

import java.io.ByteArrayInputStream;

import static com.github.automatedowl.tools.AllureEnvironmentWriter.allureEnvironmentWriter;

public final class AllureManager {

    private AllureManager() {}

    public static void setAllureEnvironmentInfo() {
        ConfigurationProps config = ConfigurationManager.getConfigurationProps();
        allureEnvironmentWriter(
                ImmutableMap.<String, String>builder()
                        .put("Platform", config.appiumPlatformName())
                        .put("Automation", config.appiumAutomationName())
                        .put("Device Name", config.appiumDeviceName())
                        .put("Appium Server", config.appiumServerUrl())
                        .put("OS Name", System.getProperty("os.name"))
                        .build(),
                config.allureResultsDir());
    }

    public static void attachScreenshotOnReport(String name) {
        byte[] screenshot = DriverFactory.captureScreenshot();
        if (screenshot.length > 0) {
            Allure.attachment(name, new ByteArrayInputStream(screenshot));
        }
    }
}
