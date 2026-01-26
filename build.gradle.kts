import org.gradle.internal.os.OperatingSystem

plugins {
    id("java")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

group = "dev.misieur"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    compileOnly("org.jetbrains:annotations:26.0.2-1")

    testImplementation("org.junit.jupiter:junit-jupiter:6.0.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:6.0.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

val os = OperatingSystem.current()
val libName = when {
    os.isLinux -> "librust.so"
    os.isMacOsX -> "librust.dylib"
    os.isWindows -> "rust.dll"
    else -> error("Unsupported OS")
}

tasks.register<Exec>("buildRust") {
    workingDir = file("rust")
    commandLine("cargo", "build", "--release")
}

tasks.processResources {
    dependsOn("buildRust")

    from("rust/target/release/$libName") {
        into("native")
    }
    from("rust/THIRD_PARTY_LICENSES.MD") {
        into("native")
    }
    from("LICENSE.MD")
}


configurations {
    testImplementation {
        extendsFrom(configurations.compileOnly.get())
    }
    testRuntimeOnly {
        extendsFrom(configurations.compileOnly.get())
    }
}

tasks.test {
    useJUnitPlatform()
}