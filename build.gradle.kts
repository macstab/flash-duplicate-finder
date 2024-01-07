import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    // Apply the Kotlin JVM plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm") version "1.8.10"
    id("com.diffplug.spotless") version "6.23.3"
    id("org.graalvm.buildtools.native") version "0.9.8"
    // Apply the application plugin to add support for building a CLI application.
    application
}

repositories {
    // Use jcenter for resolving dependencies.
    // You can declare any Maven/Ivy/file repository here.
    mavenCentral()
}

group = "com.macstab.tools"
project.properties["version"]?.toString() ?: "0.0.1-SNAPSHOT"

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    api( "commons-io:commons-io:2.8.0")
    api( "commons-cli:commons-cli:1.4")
    api( "commons-codec:commons-codec:1.15")

    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
    testImplementation("io.mockk:mockk:1.13.8")
//    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.1")
//    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.1")
}

application {
    // Define the main class for the application.
    mainClass.set("com.macstab.duplicatefinder.DuplicateFinderKt")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    sourceCompatibility = "1.8"
    targetCompatibility = "1.8"
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

spotless {
    kotlin {
        target("**/*.kt")
        ktlint("1.1.0")
    }
}