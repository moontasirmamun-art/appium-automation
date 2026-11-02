package net.therap.mobile.ui.page;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class OfflineAgendaPage extends BasePage {

    private final By offlineSlotsHeader = AppiumBy.xpath("//android.widget.TextView[@text='Offline Slots']");
    private final By moreOptionsButton = AppiumBy.xpath("//*[contains(@content-desc, 'More Option')]");
    private final By logoutMenuItem = AppiumBy.xpath("//android.widget.TextView[@text='Logout']");
    private final By logoutConfirmationHeader = AppiumBy.xpath("//android.widget.TextView[@text='Confirmation!']");
    private final By confirmLogoutYesButton = AppiumBy.xpath("//android.widget.TextView[@text='Yes']");
    private final By confirmLogoutNoButton = AppiumBy.xpath("//android.widget.TextView[@text='No']");
    private final By addNewSlotButton = AppiumBy.xpath("//android.widget.TextView[@text='Add New Slot']");

    @Step("Verify Offline Slots header is displayed")
    public boolean isOfflineSlotsHeaderDisplayed() {
        try {
            return waitForPresence(offlineSlotsHeader).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Get Offline Slots header text")
    public String getOfflineSlotsHeaderText() {
        try {
            return waitForPresence(offlineSlotsHeader).getText();
        } catch (Exception e) {
            return "";
        }
    }

    @Step("Verify Add New Slot button is displayed")
    public boolean isAddNewSlotDisplayed() {
        try {
            return waitForPresence(addNewSlotButton).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Perform Logout from Offline mode")
    public void logout() {
        WebElement moreBtn = waitForClickable(moreOptionsButton);
        moreBtn.click();
        pause(1000);

        WebElement logoutBtn = waitForClickable(logoutMenuItem);
        logoutBtn.click();
        pause(1000);

        WebElement yesBtn = waitForClickable(confirmLogoutYesButton);
        yesBtn.click();
        pause(2000);
        attachScreenshot("Logged out from Offline Mode");
    }

    @Step("Cancel Logout from Offline mode")
    public void cancelLogout() {
        WebElement moreBtn = waitForClickable(moreOptionsButton);
        moreBtn.click();
        pause(1000);

        WebElement logoutBtn = waitForClickable(logoutMenuItem);
        logoutBtn.click();
        pause(1000);

        WebElement noBtn = waitForClickable(confirmLogoutNoButton);
        noBtn.click();
        pause(1000);
        attachScreenshot("Cancelled Offline Logout");
    }
}
