package net.therap.mobile.e2e;

import net.therap.mobile.testdata.LoginTestData;
import net.therap.mobile.util.provider.TestDataSource;
import net.therap.mobile.util.tag.Regression;
import net.therap.mobile.util.tag.Smoke;
import net.therap.mobile.util.tag.Validation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;

@DisplayName("Therap Mobile App - Offline Login Suite")
public class TherapOfflineLoginTest extends BaseMobileTest {

    @Smoke
    @ParameterizedTest(name = "{0}")
    @TestDataSource(id = "TC-MOBILE-OFFLINE-01", files = {"therap_offline_login.csv"}, targetType = LoginTestData.class)
    @DisplayName("Verify switching between Online and Offline Login modes")
    public void testSwitchLoginModes(LoginTestData testData) {
        loginPage.openTherapApp();

        loginPage.switchToOnlineLogin();
        Assertions.assertTrue(loginPage.isLoginButtonDisplayed(),
                "Online login button should be displayed in online mode");

        loginPage.switchToOfflineLogin();
        Assertions.assertTrue(loginPage.isOfflineLoginHeaderDisplayed(),
                "Offline EVV Login header should be displayed after switching to offline mode");
        Assertions.assertTrue(loginPage.isOfflinePinInputDisplayed(),
                "Offline PIN field should be displayed in offline mode");

        loginPage.switchToOnlineLogin();
        Assertions.assertTrue(loginPage.isLoginButtonDisplayed(),
                "Online login button should be displayed after switching back to online mode");
        Assertions.assertFalse(loginPage.isOfflinePinInputDisplayed(),
                "Offline PIN field should not be displayed in online mode");
    }

    @Validation
    @ParameterizedTest(name = "{0}")
    @TestDataSource(id = "TC-MOBILE-OFFLINE-02", files = {"therap_offline_login.csv"}, targetType = LoginTestData.class)
    @DisplayName("Verify empty Offline PIN validation")
    public void testEmptyOfflinePinValidation(LoginTestData testData) {
        String loginName = testData.getLoginName().isEmpty() ? config.therapLoginName() : testData.getLoginName();
        String providerCode = testData.getProviderCode();

        loginPage.openTherapApp()
                .switchToOfflineLogin()
                .enterLoginName(loginName)
                .enterProviderCode(providerCode)
                .enterOfflinePin("")
                .clickOfflineLoginButton();

        Assertions.assertTrue(loginPage.isOfflineLoginHeaderDisplayed(),
                "User should remain on Offline EVV Login screen when submitting empty PIN");
    }

    @Validation
    @ParameterizedTest(name = "{0}")
    @TestDataSource(id = "TC-MOBILE-OFFLINE-03", files = {"therap_offline_login.csv"}, targetType = LoginTestData.class)
    @DisplayName("Verify invalid Offline PIN error handling")
    public void testInvalidOfflinePinValidation(LoginTestData testData) {
        String loginName = testData.getLoginName().isEmpty() ? config.therapLoginName() : testData.getLoginName();
        String providerCode = testData.getProviderCode();
        String pin = testData.getOfflinePin().isEmpty() ? "999999" : testData.getOfflinePin();

        loginPage.openTherapApp()
                .switchToOfflineLogin()
                .enterLoginName(loginName)
                .enterProviderCode(providerCode)
                .enterOfflinePin(pin)
                .clickOfflineLoginButton();

        Assertions.assertTrue(loginPage.isAlertDialogDisplayed(),
                "Alert dialog should be displayed for invalid Offline PIN");

        if (testData.getErrorMessage() != null && !testData.getErrorMessage().isEmpty()) {
            Assertions.assertTrue(loginPage.getAlertDialogMessage().contains(testData.getErrorMessage()),
                    "Alert dialog should contain expected error message");
        }

        loginPage.dismissAlertDialog();
        Assertions.assertTrue(loginPage.isOfflineLoginHeaderDisplayed(),
                "Should return to Offline Login screen after dismissing alert dialog");
    }

    @Smoke
    @Regression
    @ParameterizedTest(name = "{0}")
    @TestDataSource(id = "TC-MOBILE-OFFLINE-04", files = {"therap_offline_login.csv"}, targetType = LoginTestData.class)
    @DisplayName("Verify valid Offline Login and Logout")
    public void testValidOfflineLoginAndLogout(LoginTestData testData) {
        String loginName = testData.getLoginName().isEmpty() ? config.therapLoginName() : testData.getLoginName();
        String providerCode = testData.getProviderCode();
        String pin = testData.getOfflinePin().isEmpty() ? config.therapOfflinePin() : testData.getOfflinePin();

        loginPage.openTherapApp()
                .switchToOfflineLogin()
                .enterLoginName(loginName)
                .enterProviderCode(providerCode)
                .enterOfflinePin(pin)
                .clickOfflineLoginButton();

        Assertions.assertTrue(offlineAgendaPage.isOfflineSlotsHeaderDisplayed(),
                "Offline Slots landing screen should be displayed after successful offline login");
        Assertions.assertTrue(offlineAgendaPage.isAddNewSlotDisplayed(),
                "'Add New Slot' action should be available on the Offline Slots screen");

        offlineAgendaPage.logout();
        Assertions.assertTrue(loginPage.isLoginButtonDisplayed() || loginPage.isOfflineLoginHeaderDisplayed(),
                "Should return to login screen after logging out from offline mode");
    }
}
