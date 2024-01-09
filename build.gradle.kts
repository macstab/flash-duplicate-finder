import com.google.cloud.tools.jib.api.Containerizer
import com.google.cloud.tools.jib.api.Jib
import com.google.cloud.tools.jib.api.RegistryImage
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.nio.file.Paths

plugins {
    // Apply the Kotlin JVM plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm") version "1.8.10"
    id("com.diffplug.spotless") version "6.23.3"
    id("org.graalvm.buildtools.native") version "0.9.8"
    id("com.google.cloud.tools.jib") version "3.4.0"
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
}

application {
    // Define the main class for the application.
    mainClass.set("com.macstab.duplicatefinder.DuplicateFinderKt")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "11"
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    sourceCompatibility = "11"
    targetCompatibility = "11"
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

jib {
    from {
        image = "eclipse-temurin:latest" // or any other base image you prefer
    }
    to {
        image = "macstab/flash-duplicate-finder" // your desired image name
        tags = setOf("latest") // optional
    }
}

fun determineBaseImage(): String {
    val osArch = System.getProperty("os.arch")
    return when {
        "amd64" in osArch || "x86_64" in osArch -> "frolvlad/alpine-glibc"
        "arm" in osArch || "aarch64" in osArch -> "arm64v8/alpine"
        else -> "docker.io/default/base-image"
    }
}

fun determineTagSuffix(): String {
    val osArch = System.getProperty("os.arch")
    return when {
        "amd64" in osArch || "x86_64" in osArch -> "amd64"
        "arm" in osArch || "aarch64" in osArch -> "arm64"
        else -> "docker.io/default/base-image"
    }
}

tasks.named("nativeCompile") {
    dependsOn("build")
}

tasks.register("jibNativeImage") {
    dependsOn("nativeCompile")
    doLast {
        val version = project.properties["version"]?.toString() ?: "0.0.1"

        val baseImage = determineBaseImage()
        val targetImage = "docker.io/macstab/flash-duplicate-finder-native:latest-" + determineTagSuffix()
        val nativeImagePath = Paths.get(buildDir.absolutePath, "native/nativeCompile/FlashDuplicateFinder")

        println("Using base image: $baseImage")
        println("Pushing to image: $targetImage")

        val build = Jib.from(baseImage)
            .setEntrypoint("/app/FlashDuplicateFinder/FlashDuplicateFinder")

            .addLayer(listOf(nativeImagePath), "/app/FlashDuplicateFinder")

        val registryImage = Containerizer.to(RegistryImage.named(targetImage).addCredential(System.getenv("DOCKER_USERNAME"), System.getenv("DOCKER_PASSWORD")))
        build.containerize(registryImage)

        println("Jib native image building and pushing task is running...")
    }
}

