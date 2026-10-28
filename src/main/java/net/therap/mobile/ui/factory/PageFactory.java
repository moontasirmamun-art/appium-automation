package net.therap.mobile.ui.factory;

import io.appium.java_client.android.AndroidDriver;
import net.therap.mobile.ui.page.BasePage;

import java.util.function.Supplier;

public final class PageFactory {

    private PageFactory() {}

    public static <T extends BasePage> T initPage(AndroidDriver driver, Supplier<T> supplier) {
        try {
            T instance = supplier.get();
            instance.setAndConfigureDriver(driver);
            instance.initComponents();
            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Page class instantiation failed.", e);
        }
    }
}
