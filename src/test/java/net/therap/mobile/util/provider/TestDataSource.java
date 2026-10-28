package net.therap.mobile.util.provider;

import net.therap.mobile.testdata.BaseTestData;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ArgumentsSource(TestArgumentsProvider.class)
public @interface TestDataSource {

    String id();

    String[] files();

    Class<? extends BaseTestData> targetType();
}
