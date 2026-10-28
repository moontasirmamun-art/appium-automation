package net.therap.mobile.ui.page;

import io.appium.java_client.AppiumBy;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class DashboardPage extends BasePage {

    private final By welcomeHeader = AppiumBy.id("net.therap.app:id/welcome");
    private final By profileName = AppiumBy.id("net.therap.app:id/profileName");
    private final By logoutButton = AppiumBy.id("net.therap.app:id/logout");
    private final By confirmLogoutButton = AppiumBy.xpath("//android.widget.Button[@text='LOGOUT' or @resource-id='android:id/button1']");
    private final By cancelLogoutButton = AppiumBy.xpath("//android.widget.Button[@text='CANCEL' or @resource-id='android:id/button2']");

    @Step("Verify Dashboard screen is displayed")
    public DashboardPage verifyDashboardLoaded() {
        waitForPresence(welcomeHeader);
        waitForPresence(profileName);
        attachScreenshot("Dashboard Screen");
        return this;
    }

    public String getWelcomeText() {
        return waitForPresence(welcomeHeader).getText();
    }

    public String getProfileName() {
        return waitForPresence(profileName).getText();
    }

    @Step("Click Logout and cancel dialog")
    public DashboardPage cancelLogout() {
        WebElement btn = waitForClickable(logoutButton);
        btn.click();
        pause(1500);

        WebDriverWait dialogWait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement cancelBtn = dialogWait.until(ExpectedConditions.elementToBeClickable(cancelLogoutButton));
        cancelBtn.click();
        pause(1500);
        attachScreenshot("Cancelled Logout Dialog");
        return this;
    }

    @Step("Click Logout and confirm dialog")
    public void logout() {
        WebElement btn = waitForClickable(logoutButton);
        btn.click();
        pause(1500);

        try {
            WebDriverWait dialogWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement confirmBtn = dialogWait.until(ExpectedConditions.elementToBeClickable(confirmLogoutButton));
            confirmBtn.click();
            pause(2000);
        } catch (Exception ignored) {
        }

        attachScreenshot("After Logout");
    }
}
