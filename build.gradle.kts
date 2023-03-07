import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "com.rescribet"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

@OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
kotlin {
    jvm {
        jvmToolchain(11)
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(compose.desktop.components.splitPane)

                implementation(libs.kotlin.logging)
                runtimeOnly(libs.log4j.slf4j2)
                runtimeOnly(libs.log4j.core)

                implementation("com.github.sya-ri:kgit:1.0.5")
                implementation("org.eclipse.jgit:org.eclipse.jgit.gpg.bc:5.11.0.202103091610-r")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
                implementation("com.alialbaali.kamel:kamel-image:0.4.1")
                implementation("io.ktor:ktor-client-java:2.2.3")
            }
        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "gitstar"
            packageVersion = "1.0.0"

            val iconsRoot = project.file("src/jvmMain/resources")

            macOS {
                iconFile.set(iconsRoot.resolve("gitstar.png"))
            }
        }
    }
}
