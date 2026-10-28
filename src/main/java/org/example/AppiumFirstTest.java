package org.example;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URI;
import java.time.Duration;
import java.util.Set;

public class AppiumFirstTest {

    private static void pause(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void handleChromeFirstRunUI(AndroidDriver driver) {
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(6));

        System.out.println("Switching context to NATIVE_APP to process onboarding screens...");
        driver.context("NATIVE_APP");

        // 1. "Welcome to Chrome" -> Click "Continue as Moontasir" or "Use without an account"
        try {
            WebElement continueBtn = shortWait.until(ExpectedConditions.elementToBeClickable(
                    AppiumBy.xpath("//android.widget.Button[contains(@text, 'Continue') or contains(@text, 'Moontasir')]")
            ));
            continueBtn.click();
            System.out.println("Clicked: Welcome screen button");
            pause(2000);
        } catch (Exception ignored) {}

        // 2. "Turn on sync?" -> Click "Yes, I'm in" or "No thanks"
        try {
            WebElement syncBtn = shortWait.until(ExpectedConditions.elementToBeClickable(
                    AppiumBy.xpath("//android.widget.Button[contains(@text, \"Yes, I'm in\") or contains(@text, 'No thanks')]")
            ));
            syncBtn.click();
            System.out.println("Clicked: Sync screen button");
            pause(2000);
        } catch (Exception ignored) {}

        // 3. "Enhanced ad privacy" -> Click "More" / "Got it"
        try {
            WebElement moreBtn = shortWait.until(ExpectedConditions.elementToBeClickable(
                    AppiumBy.xpath("//android.widget.Button[contains(@text, 'More') or contains(@text, 'Got it')]")
            ));
            moreBtn.click();
            System.out.println("Clicked: Ad privacy button");
            pause(1500);

            // Handle second tap on "Got it" if required
            WebElement gotItBtn = shortWait.until(ExpectedConditions.elementToBeClickable(
                    AppiumBy.xpath("//android.widget.Button[contains(@text, 'Got it')]")
            ));
            gotItBtn.click();
            System.out.println("Clicked: Final Got it button");
            pause(2000);
        } catch (Exception ignored) {}

        // Switch back to Webview/Chromedriver context
        Set<String> contexts = driver.getContextHandles();
        for (String context : contexts) {
            if (context.contains("CHROMIUM") || context.contains("WEBVIEW")) {
                driver.context(context);
                System.out.println("Switched back to Web Context: " + context);
                break;
            }
        }
    }

    public static void main(String[] args) {
        UiAutomator2Options options = new UiAutomator2Options();
        options.setPlatformName("Android");
        options.setAutomationName("UiAutomator2");
        options.setDeviceName("Android Device");
        options.withBrowserName("Chrome");

        AndroidDriver driver = null;

        try {
            System.out.println("Step 1: Connecting to Appium server...");
            driver = new AndroidDriver(URI.create("http://127.0.0.1:4723").toURL(), options);
            pause(2000);

            // Pass native context handler
            handleChromeFirstRunUI(driver);

            System.out.println("Step 2: Navigating to Wikipedia...");
            driver.get("https://www.wikipedia.org");
            pause(3000);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

            System.out.println("Step 3: Locating search box by stable ID...");
            WebElement searchBox = wait.until(
                    ExpectedConditions.presenceOfElementLocated(By.id("searchInput"))
            );

            System.out.println("Step 4: Typing search query...");
            String query = "Software testing";
            for (char ch : query.toCharArray()) {
                searchBox.sendKeys(String.valueOf(ch));
                pause(120);
            }
            pause(1500);

            System.out.println("Step 5: Clicking search button...");
            WebElement searchButton = wait.until(
                    ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit']"))
            );
            searchButton.click();
            pause(4000);

            System.out.println("Step 6: Scrolling article page...");
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollBy(0, 600)");
            pause(3000);

            System.out.println("Step 7: Fetching first paragraph text...");
            WebElement firstParagraph = wait.until(
                    ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.mw-content-ltr p:not(.mw-empty-elt)"))
            );

            String paragraphText = firstParagraph.getText();
            System.out.println("--- First Paragraph Content ---");
            System.out.println(paragraphText);
            System.out.println("--------------------------------");
            System.out.println("All automation steps completed successfully!");

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