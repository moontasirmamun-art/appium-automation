package net.therap.mobile.ui.page;

import io.appium.java_client.AppiumBy;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class OfflineConfigurationPage extends BasePage {

    private final By headerTitle = AppiumBy.xpath("//android.widget.TextView[@text='Offline Configuration']");
    private final By subtitleText = AppiumBy.xpath("//android.widget.TextView[contains(@text, 'offline PIN')]");
    private final By offlinePinInput = AppiumBy.xpath("(//android.widget.EditText)[1]");
    private final By confirmOfflinePinInput = AppiumBy.xpath("(//android.widget.EditText)[2]");
    private final By submitPinButton = AppiumBy.xpath("//android.widget.TextView[@text='SET PIN' or @text='CHANGE PIN']");
    private final By backButton = AppiumBy.xpath("//android.widget.ImageView[@content-desc='Back']");
    private final By successDialogMessage = AppiumBy.xpath("//android.widget.TextView[@text='Offline mode is now configured' or contains(@text, 'PIN successfully changed')]");
    private final By successDialogOkButton = AppiumBy.xpath("//android.widget.TextView[@text='OK']");
    private final By validationErrorText = AppiumBy.xpath("//android.widget.TextView[contains(@text, 'Match') or contains(@text, 'six digits')]");
    private final By confirmLeaveOkButton = AppiumBy.xpath("//android.widget.TextView[@text='OK' or @text='Leave']");

    @Step("Verify Offline Configuration screen is displayed")
    public OfflineConfigurationPage verifyOfflineConfigurationLoaded() {
        waitForPresence(headerTitle);
        waitForPresence(subtitleText);
        attachScreenshot("Offline Configuration Screen Loaded");
        return this;
    }

    @Step("Get subtitle instructions text")
    public String getSubtitleText() {
        try {
            return waitForPresence(subtitleText).getText();
        } catch (Exception e) {
            return "";
        }
    }

    @Step("Enter Offline PIN: {pin}")
    public OfflineConfigurationPage enterOfflinePin(String pin) {
        WebElement element = waitForPresence(offlinePinInput);
        element.click();
        element.clear();
        if (pin != null && !pin.isEmpty()) {
            element.sendKeys(pin);
        }
        pause(500);
        hideKeyboardSafely();
        return this;
    }

    @Step("Enter Confirm Offline PIN: {pin}")
    public OfflineConfigurationPage enterConfirmOfflinePin(String pin) {
        WebElement element = waitForPresence(confirmOfflinePinInput);
        element.click();
        element.clear();
        if (pin != null && !pin.isEmpty()) {
            element.sendKeys(pin);
        }
        pause(500);
        hideKeyboardSafely();
        return this;
    }

    @Step("Click Set / Change PIN button")
    public OfflineConfigurationPage clickSubmitPin() {
        WebElement button = waitForClickable(submitPinButton);
        button.click();
        pause(1500);
        attachScreenshot("Clicked Submit PIN Button");
        return this;
    }

    @Step("Verify Success Dialog is displayed")
    public boolean isSuccessDialogDisplayed() {
        try {
            return waitForPresence(successDialogMessage).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Get Success Dialog message text")
    public String getSuccessMessage() {
        try {
            return waitForPresence(successDialogMessage).getText();
        } catch (Exception e) {
            return "";
        }
    }

    @Step("Confirm Success Dialog by clicking OK")
    public OfflineConfigurationPage confirmSuccessDialog() {
        try {
            WebElement okBtn = waitForClickable(successDialogOkButton);
            okBtn.click();
            pause(1500);
        } catch (Exception ignored) {
        }
        attachScreenshot("Confirmed Success Dialog");
        return this;
    }

    @Step("Check if validation error is displayed on screen")
    public boolean isValidationErrorDisplayed() {
        try {
            return waitForPresence(validationErrorText).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Get validation error message text")
    public String getValidationErrorMessage() {
        try {
            return waitForPresence(validationErrorText).getText();
        } catch (Exception e) {
            return "";
        }
    }

    @Step("Navigate back from Offline Configuration")
    public OfflineConfigurationPage navigateBack() {
        try {
            WebElement back = waitForClickable(backButton);
            back.click();
            pause(1000);
            if (driver.findElements(confirmLeaveOkButton).size() > 0) {
                driver.findElement(confirmLeaveOkButton).click();
                pause(1000);
            }
        } catch (Exception e) {
            driver.navigate().back();
        }
        pause(1500);
        attachScreenshot("Navigated Back from Offline Configuration");
        return this;
    }
}
