package net.therap.mobile.util.provider;

import net.therap.mobile.config.ConfigurationManager;
import net.therap.mobile.testdata.BaseTestData;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

public class TestArgumentsProvider implements ArgumentsProvider, AnnotationConsumer<TestDataSource> {

    private String id;
    private String[] files;
    private Class<? extends BaseTestData> targetType;

    @Override
    public void accept(TestDataSource source) {
        this.id = Objects.requireNonNull(source.id(), "Test data ID cannot be null");
        this.targetType = Objects.requireNonNull(source.targetType(), "Test data target type cannot be null");
        this.files = source.files();

        if (this.files == null || this.files.length == 0) {
            throw new IllegalArgumentException("You must specify at least one file in @TestDataSource");
        }
    }

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        String basePath = ConfigurationManager.getConfigurationProps().testDataBasePath();
        String file1 = Paths.get(basePath, files[0]).toString();

        return Arrays.stream(TestDataCsvLoader.load(targetType, file1, id)).map(Arguments::of);
    }
}
