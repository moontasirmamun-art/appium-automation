# Appium Mobile Automation

End-to-end mobile automation suite built with **Appium (Java Client 9.4.0)**, **Selenium WebDriver 4.28.0**, and **Gradle**.

## Features

- **Therap Mobile App Automation (`AppiumLoginTest.java`)**:
  - Connects to Android emulator (e.g., LDPlayer on `emulator-5554`).
  - Launches and interacts with Therap native and web views.
  - Automates credential entry (Login Name, Provider Code, Password).
  - Automatically handles one or multiple Signup Agreement screens ("I Agree").
  - Verifies landing on the user Dashboard.
  - Automates Logout, handles confirmation dialog, and verifies return to Login screen.
- **Chrome Web Automation (`AppiumFirstTest.java`)**:
  - Automates Google Chrome on Android.
  - Handles Chrome first-run onboarding screens and dialogs.
  - Navigates Wikipedia, performs searches, scrolls, and extracts content.

## Setup & Configuration

1. **Prerequisites**:
   - Java 17+
   - Android Emulator (e.g. LDPlayer or Android Studio AVD) with USB Debugging enabled.
   - Appium Server running locally on port `4723` with `uiautomator2` driver installed:
     ```bash
     appium
     ```

2. **Credentials Configuration**:
   - Copy `config.properties.example` to `config.properties`:
     ```bash
     cp config.properties.example config.properties
     ```
   - Update `config.properties` with your login details:
     ```properties
     therap.login.name=your_login_name
     therap.provider.code=your_provider_code
     therap.password=your_password
     ```
   - *(Note: `config.properties` is excluded by `.gitignore` to prevent committing secrets).*

## Running Tests

- Run the Therap App Login & Logout Test:
  ```bash
  ./gradlew runLoginTest
  ```

- Run the Wikipedia Chrome Test:
  ```bash
  ./gradlew run
  ```
