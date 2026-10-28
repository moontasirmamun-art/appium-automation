package net.therap.mobile.ui.page;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class LoginPage extends BasePage {

    private final By therapAppIcon = AppiumBy.xpath("//android.widget.TextView[@text='Therap' or @content-desc='Therap']");
    private final By loginNameInput = AppiumBy.id("net.therap.app:id/edt_login_name");
    private final By providerCodeInput = AppiumBy.id("net.therap.app:id/edt_provider_code");
    private final By loginButton = AppiumBy.id("net.therap.app:id/login");
    private final By passwordInput = AppiumBy.xpath("//android.widget.EditText[@password='true']");
    private final By submitCredentialsButton = AppiumBy.xpath("//android.widget.Button[contains(@text, 'Submit credentials') or contains(@text, 'log in') or contains(@text, 'Log in')]");
    private final By loginFailedHeader = AppiumBy.xpath("//*[contains(@text, 'Login Failed')]");
    private final By closeCustomTabButton = AppiumBy.xpath("//android.widget.ImageButton[@resource-id='com.android.chrome:id/close_button' or @content-desc='Close tab']");
    private final By cancelLoginButton = AppiumBy.xpath("//android.widget.Button[contains(@text, 'Cancel login')]");

    @Step("Launch Therap app from home screen")
    public LoginPage openTherapApp() {
        try {
            driver.terminateApp("com.android.chrome");
            driver.terminateApp("net.therap.app");
        } catch (Exception ignored) {
        }
        driver.pressKey(new KeyEvent(AndroidKey.HOME));
        pause(1500);
        WebElement icon = waitForClickable(therapAppIcon);
        icon.click();
        pause(2000);
        attachScreenshot("Therap App Launched");
        return this;
    }

    @Step("Enter Login Name: {loginName}")
    public LoginPage enterLoginName(String loginName) {
        WebElement element = waitForPresence(loginNameInput);
        element.clear();
        if (loginName != null && !loginName.isEmpty()) {
            element.sendKeys(loginName);
        }
        pause(500);
        return this;
    }

    @Step("Enter Provider Code: {providerCode}")
    public LoginPage enterProviderCode(String providerCode) {
        WebElement element = waitForPresence(providerCodeInput);
        element.clear();
        if (providerCode != null && !providerCode.isEmpty()) {
            element.sendKeys(providerCode);
        }
        pause(500);
        hideKeyboardSafely();
        return this;
    }

    @Step("Click Initial Login Button")
    public LoginPage clickLoginButton() {
        WebElement button = waitForClickable(loginButton);
        button.click();
        pause(2500);
        attachScreenshot("Clicked Login Button");
        return this;
    }

    @Step("Enter Password")
    public LoginPage enterPassword(String password) {
        WebElement element = waitForPresence(passwordInput);
        element.clear();
        if (password != null && !password.isEmpty()) {
            element.sendKeys(password);
        }
        pause(500);
        hideKeyboardSafely();
        return this;
    }

    @Step("Click Submit Credentials Button")
    public LoginPage clickSubmitCredentials() {
        WebElement button = waitForClickable(submitCredentialsButton);
        button.click();
        pause(3000);
        attachScreenshot("Submitted Credentials");
        return this;
    }

    @Step("Verify Login Screen is Displayed")
    public boolean isLoginButtonDisplayed() {
        try {
            return waitForPresence(loginButton).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Verify Login Failed banner/text is displayed")
    public boolean isLoginFailedDisplayed() {
        try {
            return waitForPresence(loginFailedHeader).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Get Login Failed text")
    public String getLoginFailedText() {
        try {
            return waitForPresence(loginFailedHeader).getText();
        } catch (Exception e) {
            return "";
        }
    }

    @Step("Dismiss login failure and return to login screen")
    public LoginPage dismissLoginFailure() {
        try {
            WebElement closeBtn = waitForClickable(closeCustomTabButton);
            closeBtn.click();
            pause(1500);
        } catch (Exception e) {
            driver.pressKey(new KeyEvent(AndroidKey.BACK));
            pause(1500);
        }
        attachScreenshot("Returned to Login Screen");
        return this;
    }

    @Step("Cancel login and return to login screen")
    public LoginPage cancelPasswordLogin() {
        WebElement cancelBtn = waitForClickable(cancelLoginButton);
        cancelBtn.click();
        pause(1500);
        attachScreenshot("Cancelled Password Login");
        return this;
    }
}
