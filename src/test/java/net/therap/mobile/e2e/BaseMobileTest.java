package net.therap.mobile.e2e;

import io.appium.java_client.android.AndroidDriver;
import net.therap.mobile.config.ConfigurationManager;
import net.therap.mobile.config.ConfigurationProps;
import net.therap.mobile.driver.DriverFactory;
import net.therap.mobile.report.AllureManager;
import net.therap.mobile.ui.factory.PageFactory;
import net.therap.mobile.ui.page.AgreementPromptPage;
import net.therap.mobile.ui.page.DashboardPage;
import net.therap.mobile.ui.page.LoginPage;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.Optional;

import static org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public abstract class BaseMobileTest {

    protected AndroidDriver driver;
    protected LoginPage loginPage;
    protected AgreementPromptPage agreementPromptPage;
    protected DashboardPage dashboardPage;
    protected ConfigurationProps config = ConfigurationManager.getConfigurationProps();

    @RegisterExtension
    AfterTestExecutionCallback callback = context -> {
        Optional<Throwable> exception = context.getExecutionException();
        if (exception.isPresent()) {
            AllureManager.attachScreenshotOnReport("Failure Screenshot - " + context.getDisplayName());
        }
    };

    @BeforeAll
    public void setupSuite() {
        AllureManager.setAllureEnvironmentInfo();
    }

    @BeforeEach
    public void setupTest() {
        driver = DriverFactory.createDriver();
        loginPage = PageFactory.initPage(driver, LoginPage::new);
        agreementPromptPage = PageFactory.initPage(driver, AgreementPromptPage::new);
        dashboardPage = PageFactory.initPage(driver, DashboardPage::new);
    }

    @AfterEach
    public void tearDownTest() {
        DriverFactory.quitDriver();
    }
}
