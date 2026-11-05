package net.therap.mobile.ui.page;

import io.appium.java_client.AppiumBy;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class SettingsPage extends BasePage {

    private final By headerTitle = AppiumBy.xpath("//android.widget.TextView[@text='Settings']");
    private final By navigateUpButton = AppiumBy.xpath("//android.widget.ImageButton[@content-desc='Navigate up']");
    private final By offlineEvvOption = AppiumBy.xpath("//*[@resource-id='android:id/title' and @text='Offline EVV']");
    private final By offlineEvvSummary = AppiumBy.xpath("//android.widget.TextView[@text='Offline EVV']/following-sibling::android.widget.TextView[@resource-id='android:id/summary']");

    @Step("Verify Settings screen is displayed")
    public SettingsPage verifySettingsLoaded() {
        waitForPresence(headerTitle);
        waitForPresence(offlineEvvOption);
        attachScreenshot("Settings Screen Loaded");
        return this;
    }

    @Step("Get Offline EVV summary status")
    public String getOfflineEvvStatus() {
        try {
            return waitForPresence(offlineEvvSummary).getText();
        } catch (Exception e) {
            return "";
        }
    }

    @Step("Open Offline EVV configuration from Settings")
    public SettingsPage openOfflineEvv() {
        WebElement element = waitForClickable(offlineEvvOption);
        element.click();
        pause(1500);
        attachScreenshot("Clicked Offline EVV in Settings");
        return this;
    }

    @Step("Navigate back up to Dashboard")
    public SettingsPage navigateBack() {
        try {
            WebElement upBtn = waitForClickable(navigateUpButton);
            upBtn.click();
        } catch (Exception e) {
            driver.navigate().back();
        }
        pause(1500);
        attachScreenshot("Navigated Back from Settings");
        return this;
    }
}
