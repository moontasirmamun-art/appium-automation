package net.therap.mobile.e2e;

import net.therap.mobile.testdata.LoginTestData;
import net.therap.mobile.util.provider.TestDataSource;
import net.therap.mobile.util.tag.Regression;
import net.therap.mobile.util.tag.Smoke;
import net.therap.mobile.util.tag.Validation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;

@DisplayName("Therap Mobile App - Authentication Suite")
public class TherapLoginTest extends BaseMobileTest {

    @Smoke
    @ParameterizedTest(name = "{0}")
    @TestDataSource(id = "TC-MOBILE-LOGIN-01", files = {"therap_login.csv"}, targetType = LoginTestData.class)
    @DisplayName("Verify successful login, agreement acceptance, and logout")
    public void testLoginAndLogout(LoginTestData testData) {
        String loginName = testData.getLoginName().isEmpty() ? config.therapLoginName() : testData.getLoginName();
        String providerCode = testData.getProviderCode().isEmpty() ? config.therapProviderCode() : testData.getProviderCode();
        String password = testData.getPassword().isEmpty() ? config.therapPassword() : testData.getPassword();

        loginPage.openTherapApp()
                .enterLoginName(loginName)
                .enterProviderCode(providerCode)
                .clickLoginButton()
                .enterPassword(password)
                .clickSubmitCredentials();

        agreementPromptPage.handleAgreements();

        dashboardPage.verifyDashboardLoaded();
        Assertions.assertTrue(dashboardPage.getWelcomeText().length() > 0, "Welcome text should be visible on dashboard");

        dashboardPage.logout();

        Assertions.assertTrue(loginPage.isLoginButtonDisplayed(), "Login screen should be displayed after logout");
    }

    @Validation
    @ParameterizedTest(name = "{0}")
    @TestDataSource(id = "TC-MOBILE-LOGIN-02", files = {"therap_login.csv"}, targetType = LoginTestData.class)
    @DisplayName("Verify error handling when submitting invalid Provider Code")
    public void testInvalidProviderCode(LoginTestData testData) {
        String loginName = testData.getLoginName().isEmpty() ? config.therapLoginName() : testData.getLoginName();
        String providerCode = testData.getProviderCode();
        String password = testData.getPassword().isEmpty() ? config.therapPassword() : testData.getPassword();

        loginPage.openTherapApp()
                .enterLoginName(loginName)
                .enterProviderCode(providerCode)
                .clickLoginButton()
                .enterPassword(password)
                .clickSubmitCredentials();

        Assertions.assertTrue(loginPage.isLoginFailedDisplayed(), "Login Failed screen should be displayed");

        loginPage.dismissLoginFailure();
        Assertions.assertTrue(loginPage.isLoginButtonDisplayed(), "Should return to Login screen after dismissing failure");
    }

    @Validation
    @ParameterizedTest(name = "{0}")
    @TestDataSource(id = "TC-MOBILE-LOGIN-03", files = {"therap_login.csv"}, targetType = LoginTestData.class)
    @DisplayName("Verify error handling when submitting invalid Login Name")
    public void testInvalidLoginName(LoginTestData testData) {
        String loginName = testData.getLoginName();
        String providerCode = testData.getProviderCode().isEmpty() ? config.therapProviderCode() : testData.getProviderCode();
        String password = testData.getPassword().isEmpty() ? config.therapPassword() : testData.getPassword();

        loginPage.openTherapApp()
                .enterLoginName(loginName)
                .enterProviderCode(providerCode)
                .clickLoginButton()
                .enterPassword(password)
                .clickSubmitCredentials();

        Assertions.assertTrue(loginPage.isLoginFailedDisplayed(), "Login Failed screen should be displayed");

        loginPage.dismissLoginFailure();
        Assertions.assertTrue(loginPage.isLoginButtonDisplayed(), "Should return to Login screen after dismissing failure");
    }

    @Validation
    @ParameterizedTest(name = "{0}")
    @TestDataSource(id = "TC-MOBILE-LOGIN-04", files = {"therap_login.csv"}, targetType = LoginTestData.class)
    @DisplayName("Verify error handling when submitting invalid Password")
    public void testInvalidPassword(LoginTestData testData) {
        String loginName = testData.getLoginName().isEmpty() ? config.therapLoginName() : testData.getLoginName();
        String providerCode = testData.getProviderCode().isEmpty() ? config.therapProviderCode() : testData.getProviderCode();
        String password = testData.getPassword();

        loginPage.openTherapApp()
                .enterLoginName(loginName)
                .enterProviderCode(providerCode)
                .clickLoginButton()
                .enterPassword(password)
                .clickSubmitCredentials();

        Assertions.assertTrue(loginPage.isLoginFailedDisplayed(), "Login Failed screen should be displayed");

        loginPage.dismissLoginFailure();
        Assertions.assertTrue(loginPage.isLoginButtonDisplayed(), "Should return to Login screen after dismissing failure");
    }

    @Validation
    @ParameterizedTest(name = "{0}")
    @TestDataSource(id = "TC-MOBILE-LOGIN-05", files = {"therap_login.csv"}, targetType = LoginTestData.class)
    @DisplayName("Verify validation when Login Name is empty")
    public void testEmptyFieldsValidation(LoginTestData testData) {
        loginPage.openTherapApp()
                .enterLoginName("")
                .clickLoginButton();

        Assertions.assertTrue(loginPage.isLoginButtonDisplayed(),
                "Login button should still be displayed when login name is empty");
    }

    @Regression
    @ParameterizedTest(name = "{0}")
    @TestDataSource(id = "TC-MOBILE-LOGIN-06", files = {"therap_login.csv"}, targetType = LoginTestData.class)
    @DisplayName("Verify cancelling logout dialog keeps user on Dashboard")
    public void testCancelLogout(LoginTestData testData) {
        String loginName = testData.getLoginName().isEmpty() ? config.therapLoginName() : testData.getLoginName();
        String providerCode = testData.getProviderCode().isEmpty() ? config.therapProviderCode() : testData.getProviderCode();
        String password = testData.getPassword().isEmpty() ? config.therapPassword() : testData.getPassword();

        loginPage.openTherapApp()
                .enterLoginName(loginName)
                .enterProviderCode(providerCode)
                .clickLoginButton()
                .enterPassword(password)
                .clickSubmitCredentials();

        agreementPromptPage.handleAgreements();

        dashboardPage.verifyDashboardLoaded();
        Assertions.assertTrue(dashboardPage.getWelcomeText().length() > 0, "Welcome text should be visible on dashboard");

        dashboardPage.cancelLogout();
        dashboardPage.verifyDashboardLoaded();
        Assertions.assertTrue(dashboardPage.getWelcomeText().length() > 0, "Welcome text should still be visible after cancelling logout");

        dashboardPage.logout();
        Assertions.assertTrue(loginPage.isLoginButtonDisplayed(), "Login screen should be displayed after logout");
    }
}
