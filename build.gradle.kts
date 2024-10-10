import com.google.cloud.tools.jib.api.Containerizer
import com.google.cloud.tools.jib.api.Jib
import com.google.cloud.tools.jib.api.RegistryImage
import com.google.cloud.tools.jib.api.buildplan.AbsoluteUnixPath
import com.google.cloud.tools.jib.api.buildplan.FileEntriesLayer
import com.google.cloud.tools.jib.api.buildplan.FilePermissions
import org.jetbrains.kotlin.cli.jvm.main
import org.jetbrains.kotlin.fir.expressions.builder.buildArgumentList
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.nio.file.Paths

// The pipeline supports the execution of the app, regular JVM build and native image build for ARM64 and AMD64
// further we provide the option via the TAG env var to define the current release tag for a container image release


plugins {
    // Apply the Kotlin JVM plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm") version "2.0.21"
    id("com.diffplug.spotless") version "6.25.0"
    id("org.graalvm.buildtools.native") version "0.9.28"
    id("com.google.cloud.tools.jib") version "3.4.0"
    // Apply the application plugin to add support for building a CLI application.
    application
}

repositories {
    // Use jcenter for resolving dependencies.
    // You can declare any Maven/Ivy/file repository here.
    mavenCentral()
}

val group = "com.macstab.tools"
val version = project.properties["version"]?.toString() ?: "0.0.1"

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    api( "commons-io:commons-io:2.15.1")
    api( "commons-cli:commons-cli:1.6.0")
    api( "commons-codec:commons-codec:1.16.0")

    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
    testImplementation("io.mockk:mockk:1.13.9")
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
    var tag : String? = System.getenv()["TAG"]
    if (tag.isNullOrEmpty()) {
        tag = "latest"
    }

    from {
        image = "eclipse-temurin:latest" // or any other base image you prefer
    }
    to {

        image = "macstab/flash-duplicate-finder" // your desired image name
        tags = setOf(tag) // optional
    }
}

graalvmNative {
    binaries.all {
        buildArgs.add("--static")
    }
}

/**
 * This should provide the architecture of the base image to use ( even if currently we use a multi arch base image )
 */
fun determineBaseImage(): String {
    val osArch = determineTagSuffix()
    return when {
        "amd64" in osArch || "x86_64" in osArch -> "oraclelinux:8-slim"
        "arm64" in osArch || "aarch64" in osArch -> "oraclelinux:8-slim"
        else -> "docker.io/default/base-image"
    }
}

fun determineTagSuffix(): String {
    val envArchitecture = System.getenv("ARCHITECTURE")
    if (envArchitecture != null && envArchitecture.isNotEmpty()) {
        return envArchitecture
    }
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

        var tag : String? = System.getenv()["TAG"]
        if (tag.isNullOrEmpty()) {
            tag = "latest"
        }

        val baseImage = determineBaseImage()
        val targetImage = "docker.io/macstab/flash-duplicate-finder-native:${tag}-" + determineTagSuffix()
        val nativeImagePath = Paths.get(layout.buildDirectory.get().asFile.absolutePath, "native/nativeCompile/FlashDuplicateFinder")

        println("Using base image: $baseImage")
        println("Pushing to image: $targetImage")


        val nativeImageEntry = FileEntriesLayer.builder()
            .addEntry(nativeImagePath, AbsoluteUnixPath.get("/app/FlashDuplicateFinder"), FilePermissions.fromOctalString("755"))
            .build()

        val build = Jib.from(baseImage)
            .setEntrypoint("/app/FlashDuplicateFinder")
            .addFileEntriesLayer(nativeImageEntry)

        val registryImage = Containerizer.to(RegistryImage.named(targetImage).addCredential(System.getenv("DOCKER_USERNAME"), System.getenv("DOCKER_PASSWORD")))
        build.containerize(registryImage)

        println("Jib native image building and pushing task is running...")
    }
}

