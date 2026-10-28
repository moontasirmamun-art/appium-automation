package net.therap.mobile.util.provider;

import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.csv.CsvRoutines;
import net.therap.mobile.testdata.BaseTestData;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class TestDataCsvLoader {

    private TestDataCsvLoader() {}

    public static <T extends BaseTestData> Object[] load(Class<T> clazz, String file, String id) {
        return readAndProcess(clazz, file, id).toArray();
    }

    private static CsvRoutines createRoutines() {
        final CsvParserSettings settings = new CsvParserSettings();
        settings.getFormat().setLineSeparator("\n");
        settings.trimValues(true);
        return new CsvRoutines(settings);
    }

    private static <T extends BaseTestData> List<T> readAndProcess(Class<T> clazz, String file, String id) {
        CsvRoutines routines = createRoutines();

        try (var reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            var testData = new ArrayList<T>();

            routines.iterate(clazz, reader).forEach(e -> {
                if (id != null && id.equals(e.getTestCaseId())) {
                    testData.add(e);
                }
            });

            return testData;
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read CSV test data from: " + file, e);
        }
    }
}
