package net.therap.mobile.ui.page;

import io.appium.java_client.AppiumBy;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AgreementPromptPage extends BasePage {

    private final By agreeButton = AppiumBy.xpath("//android.widget.Button[@text='I Agree' or contains(@text, 'Agree')]");

    @Step("Handle any Signup Agreement prompt screens")
    public AgreementPromptPage handleAgreements() {
        WebDriverWait agreementWait = new WebDriverWait(driver, Duration.ofSeconds(6));

        while (true) {
            try {
                WebElement btn = agreementWait.until(ExpectedConditions.elementToBeClickable(agreeButton));
                btn.click();
                attachScreenshot("Clicked 'I Agree'");
                pause(2000);
            } catch (Exception e) {
                break;
            }
        }
        return this;
    }
}
