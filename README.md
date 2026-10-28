# Appium Mobile Test Automation Framework

End-to-end mobile test automation framework for the **Therap Mobile App**, designed using the enterprise architecture from **Project Gemini / Playwright-Java** (Java 21, Appium Java Client 9.4.0, Selenium 4.28.0, JUnit 5, Allure Report, Aeonbits Owner, and Univocity CSV Test Data).

---

## 1. Core Architecture

The framework follows a **Package-by-Feature / Page Object Model (POM)** structure under `net.therap.mobile`:

```
appium-automation/
├── gradle/wrapper/                 # Gradle Wrapper (8.14.3 supporting Java 21)
├── settings.gradle                 # Project settings (Groovy DSL)
├── build.gradle                    # Main build configuration with AspectJ & Allure agent
├── config.properties               # Local environment credentials & driver settings
├── src/
│   ├── main/
│   │   ├── java/net/therap/mobile/
│   │   │   ├── config/             # Aeonbits Owner Config (ConfigurationProps, ConfigurationManager)
│   │   │   ├── driver/             # Appium AndroidDriver lifecycle & DriverFactory
│   │   │   ├── report/             # Allure reporting & screenshot attachment manager
│   │   │   ├── testdata/           # Strongly-typed Univocity POJOs (BaseTestData, LoginTestData)
│   │   │   └── ui/
│   │   │       ├── factory/        # PageFactory for lazy instantiation of pages
│   │   │       └── page/           # Page Objects (BasePage, LoginPage, AgreementPromptPage, DashboardPage)
│   │   └── resources/
│   │       ├── config.properties   # Default framework properties
│   │       └── allure.properties   # Allure report configuration
│   └── test/
│       ├── java/net/therap/mobile/
│       │   ├── e2e/                # Test suites extending BaseMobileTest (TherapLoginTest)
│       │   └── util/
│       │       ├── provider/       # CSV data provider engine (@TestDataSource, TestArgumentsProvider)
│       │       └── tag/            # Test categorization annotations (@Smoke, @Regression, @Validation)
│       └── resources/
│           ├── junit-platform.properties
│           └── testdata/mobile/    # CSV test data files (therap_login.csv)
```

---

## 2. Key Features

- **Page Object Model (POM):**
  - `LoginPage`: App launching, credential inputs, submitting.
  - `AgreementPromptPage`: Handles variable number of Signup Agreement "I Agree" screens.
  - `DashboardPage`: Dashboard verification, welcome header/profile verification, logout dialog confirmation.
- **Data-Driven with CSV:**
  - Tests utilize `@ParameterizedTest` combined with `@TestDataSource(id = "...", files = {"..."}, targetType = ...)` to inject test records directly from CSV into test methods.
- **Type-Safe Configuration:**
  - Powered by `org.aeonbits.owner` with fallback resolution across System Properties, Environment Variables, and local `config.properties`.
- **Automated Failure Reporting:**
  - Automatic failure screenshot capture attached to Allure test reports via JUnit 5 `AfterTestExecutionCallback`.
- **Categorized Test Execution:**
  - Run subsets of tests using `@Smoke`, `@Regression`, or `@Validation`.

---

## 3. Setup & Configuration

1. **Prerequisites:**
   - **Java 21+**
   - **Android Emulator** (e.g. LDPlayer or Android Studio AVD) with USB Debugging enabled.
   - **Appium Server 2.x** with `uiautomator2` driver:
     ```bash
     appium
     ```

2. **Credentials & Appium Settings:**
   - Ensure `config.properties` in the project root contains your test environment credentials and emulator details:
     ```properties
     appium.server.url=http://127.0.0.1:4723
     appium.device.name=emulator-5554
     appium.platform.name=Android
     appium.automation.name=UiAutomator2
     appium.no.reset=true
     appium.timeout.seconds=15

     therap.login.name=your_login_name
     therap.provider.code=your_provider_code
     therap.password=your_password
     ```

---

## 4. Running Tests

- **Run all tests:**
  ```bash
  ./gradlew test
  ```

- **Run only Smoke tests:**
  ```bash
  ./gradlew test -PrunTag=smoke
  ```

- **Run only Regression tests:**
  ```bash
  ./gradlew test -PrunTag=regression
  ```

- **View Allure Report:**
  ```bash
  allure serve build/allure-results
  ```
