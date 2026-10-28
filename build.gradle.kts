plugins {
    id("java")
    id("application")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Modern Selenium Java version (patches transitive vulnerabilities)
    implementation("org.seleniumhq.selenium:selenium-java:4.28.0")

    // Appium Java Client
    implementation("io.appium:java-client:9.4.0")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("org.example.AppiumFirstTest")
}

tasks.register<JavaExec>("runLoginTest") {
    group = "application"
    description = "Runs the AppiumLoginTest script"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("org.example.AppiumLoginTest")
}