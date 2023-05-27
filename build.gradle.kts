import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.shadow)
    application
}

group = "com.russellbanks"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    jvm()
    linuxX64()
    macosX64()
    macosArm64()

    sourceSets {
        val commonMain by getting {
            dependencies {
                // Clikt - https://github.com/ajalt/clikt
                implementation(libs.clikt)

                // KotlinX Coroutines - https://github.com/Kotlin/kotlinx.coroutines
                implementation(libs.kotlinx.coroutines.core)

                // Ktor - https://github.com/ktorio/ktor
                implementation(libs.ktor.network)
            }
        }
    }
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

java {
    sourceCompatibility = JavaVersion.current()
    targetCompatibility = JavaVersion.VERSION_17
}


application {
    mainClass.set("MainKt")
}
