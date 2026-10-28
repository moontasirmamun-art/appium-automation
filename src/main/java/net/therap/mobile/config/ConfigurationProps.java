package net.therap.mobile.config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.Sources;

@LoadPolicy(Config.LoadType.MERGE)
@Sources({
    "system:properties",
    "system:env",
    "file:config.properties",
    "classpath:config.properties"
})
public interface ConfigurationProps extends Config {

    @Key("appium.server.url")
    @DefaultValue("http://127.0.0.1:4723")
    String appiumServerUrl();

    @Key("appium.device.name")
    @DefaultValue("emulator-5554")
    String appiumDeviceName();

    @Key("appium.platform.name")
    @DefaultValue("Android")
    String appiumPlatformName();

    @Key("appium.automation.name")
    @DefaultValue("UiAutomator2")
    String appiumAutomationName();

    @Key("appium.no.reset")
    @DefaultValue("true")
    boolean appiumNoReset();

    @Key("appium.timeout.seconds")
    @DefaultValue("15")
    int appiumTimeoutSeconds();

    @Key("allure.resultsDir")
    @DefaultValue("build/allure-results")
    String allureResultsDir();

    @Key("testdata.base.path")
    @DefaultValue("src/test/resources/testdata/mobile")
    String testDataBasePath();

    @Key("therap.login.name")
    @DefaultValue("")
    String therapLoginName();

    @Key("therap.provider.code")
    @DefaultValue("")
    String therapProviderCode();

    @Key("therap.password")
    @DefaultValue("")
    String therapPassword();
}
