package net.therap.mobile.ui.page;

import io.appium.java_client.android.AndroidDriver;
import net.therap.mobile.config.ConfigurationManager;
import net.therap.mobile.config.ConfigurationProps;
import net.therap.mobile.report.AllureManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BasePage {

    protected AndroidDriver driver;
    protected WebDriverWait wait;
    protected ConfigurationProps configuration = ConfigurationManager.getConfigurationProps();

    public void setAndConfigureDriver(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(configuration.appiumTimeoutSeconds()));
    }

    public void initComponents() {
    }

    protected WebElement waitForVisibility(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected WebElement waitForPresence(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public void attachScreenshot(String stepName) {
        AllureManager.attachScreenshotOnReport(stepName);
    }

    protected void hideKeyboardSafely() {
        try {
            driver.hideKeyboard();
            pause(500);
        } catch (Exception ignored) {
        }
    }

    protected void pause(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
