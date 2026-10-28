package org.example;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.util.Properties;

public class AppiumLoginTest {

    private static Properties loadConfig() {
        Properties props = new Properties();
        File configFile = new File("config.properties");
        if (configFile.exists()) {
            try (FileInputStream fis = new FileInputStream(configFile)) {
                props.load(fis);
            } catch (IOException e) {
                System.err.println("Could not read config.properties: " + e.getMessage());
            }
        }
        return props;
    }

    private static void pause(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        Properties config = loadConfig();
        String loginName = config.getProperty("therap.login.name", System.getenv().getOrDefault("THERAP_LOGIN_NAME", ""));
        String providerCode = config.getProperty("therap.provider.code", System.getenv().getOrDefault("THERAP_PROVIDER_CODE", ""));
        String password = config.getProperty("therap.password", System.getenv().getOrDefault("THERAP_PASSWORD", ""));

        if (loginName.isEmpty() || providerCode.isEmpty() || password.isEmpty()) {
            System.err.println("Error: Missing credentials. Please define them in config.properties or set environment variables.");
            return;
        }

        UiAutomator2Options options = new UiAutomator2Options();
        options.setPlatformName("Android");
        options.setAutomationName("UiAutomator2");
        options.setDeviceName("emulator-5554");
        options.setNoReset(true);

        AndroidDriver driver = null;

        try {
            System.out.println("Step 1: Connecting to Appium server on 127.0.0.1:4723...");
            driver = new AndroidDriver(URI.create("http://127.0.0.1:4723").toURL(), options);
            pause(2000);

            System.out.println("Step 2: Pressing HOME to ensure launcher home screen is active...");
            driver.pressKey(new KeyEvent(AndroidKey.HOME));
            pause(1500);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            System.out.println("Step 3: Locating the 'Therap' app icon on screen...");
            WebElement therapAppIcon = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            AppiumBy.xpath("//android.widget.TextView[@text='Therap' or @content-desc='Therap']")
                    )
            );

            System.out.println("Step 4: Clicking on Therap App...");
            therapAppIcon.click();
            pause(2000);

            System.out.println("Step 5: Locating form fields...");
            WebElement loginNameField = wait.until(
                    ExpectedConditions.presenceOfElementLocated(
                            AppiumBy.id("net.therap.app:id/edt_login_name")
                    )
            );
            WebElement providerCodeField = wait.until(
                    ExpectedConditions.presenceOfElementLocated(
                            AppiumBy.id("net.therap.app:id/edt_provider_code")
                    )
            );
            WebElement loginButton = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            AppiumBy.id("net.therap.app:id/login")
                    )
            );

            System.out.println("Step 6: Entering Login Name: " + loginName + "...");
            loginNameField.clear();
            loginNameField.sendKeys(loginName);
            pause(1000);

            System.out.println("Step 7: Entering Provider Code: " + providerCode + "...");
            providerCodeField.clear();
            providerCodeField.sendKeys(providerCode);
            pause(1000);

            // Hide keyboard if visible so it doesn't obstruct the button
            try {
                driver.hideKeyboard();
                pause(500);
            } catch (Exception ignored) {}

            System.out.println("Step 8: Clicking Login button...");
            loginButton.click();
            pause(3000);

            System.out.println("Step 9: Waiting for authentication / password screen...");
            WebElement passwordField = wait.until(
                    ExpectedConditions.presenceOfElementLocated(
                            AppiumBy.xpath("//android.widget.EditText[@password='true']")
                    )
            );

            System.out.println("Step 10: Entering password...");
            passwordField.clear();
            passwordField.sendKeys(password);
            pause(1000);

            // Hide keyboard if visible
            try {
                driver.hideKeyboard();
                pause(500);
            } catch (Exception ignored) {}

            System.out.println("Step 11: Locating and clicking final Login / Submit button...");
            WebElement submitLoginButton = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            AppiumBy.xpath("//android.widget.Button[contains(@text, 'Submit credentials') or contains(@text, 'log in') or contains(@text, 'Log in')]")
                    )
            );
            submitLoginButton.click();
            pause(3000);

            System.out.println("Step 12: Checking for Signup Agreement screens...");
            WebDriverWait agreementWait = new WebDriverWait(driver, Duration.ofSeconds(8));

            while (true) {
                try {
                    WebElement agreeButton = agreementWait.until(
                            ExpectedConditions.elementToBeClickable(
                                    AppiumBy.xpath("//android.widget.Button[@text='I Agree' or contains(@text, 'Agree')]")
                            )
                    );
                    System.out.println("Signup Agreement page detected. Clicking 'I Agree'...");
                    agreeButton.click();
                    pause(2500);
                } catch (Exception e) {
                    System.out.println("No more Signup Agreement pages found. Continuing...");
                    break;
                }
            }

            System.out.println("Step 13: Verifying Dashboard screen...");
            WebDriverWait dashboardWait = new WebDriverWait(driver, Duration.ofSeconds(15));
            WebElement welcomeText = dashboardWait.until(
                    ExpectedConditions.presenceOfElementLocated(
                            AppiumBy.id("net.therap.app:id/welcome")
                    )
            );
            WebElement profileName = driver.findElement(AppiumBy.id("net.therap.app:id/profileName"));

            System.out.println("Landed on Dashboard successfully!");
            System.out.println("Dashboard Header: " + welcomeText.getText());
            System.out.println("User Profile: " + profileName.getText());
            pause(2000);

            System.out.println("Step 14: Clicking Logout button on Dashboard...");
            WebElement logoutButton = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            AppiumBy.id("net.therap.app:id/logout")
                    )
            );
            logoutButton.click();
            pause(1500);

            // Confirm logout dialog if displayed
            try {
                WebDriverWait dialogWait = new WebDriverWait(driver, Duration.ofSeconds(5));
                WebElement confirmLogoutBtn = dialogWait.until(
                        ExpectedConditions.elementToBeClickable(
                                AppiumBy.xpath("//android.widget.Button[@text='LOGOUT' or @resource-id='android:id/button1']")
                        )
                );
                System.out.println("Confirmation dialog shown. Confirming logout...");
                confirmLogoutBtn.click();
                pause(2000);
            } catch (Exception e) {
                System.out.println("No confirmation dialog appeared or auto-dismissed.");
            }

            System.out.println("Step 15: Verifying successful logout (returning to Login screen)...");
            WebDriverWait logoutWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement backOnLoginScreen = logoutWait.until(
                    ExpectedConditions.presenceOfElementLocated(
                            AppiumBy.id("net.therap.app:id/login")
                    )
            );

            System.out.println("Logout verified successfully! Login button is displayed.");

        } catch (Exception e) {
            System.err.println("Execution Failed:");
            e.printStackTrace();
        } finally {
            if (driver != null) {
                driver.quit();
                System.out.println("Appium session closed.");
            }
        }
    }
}
