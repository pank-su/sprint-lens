import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinxSerialization)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    jvm("desktop")
    
    sourceSets {
        val desktopMain by getting
        
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.client.okhttp)
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.android)

        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)


            implementation(libs.voyager.navigator)
            implementation(libs.voyager.tab.navigator)
            implementation(libs.voyager.koin)
            implementation(libs.voyager.screenmodel)
            implementation(libs.voyager.transitions)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)


            implementation(project.dependencies.platform(libs.koin.bom))

            implementation(libs.koin.compose)
            implementation(libs.koin.core)

            implementation(compose.materialIconsExtended)

            implementation("com.valentinilk.shimmer:compose-shimmer:1.3.1")

            // Enables FileKit without Compose dependencies
            implementation("io.github.vinceglb:filekit-core:0.8.7")

            // Enables FileKit with Composable utilities
            implementation("io.github.vinceglb:filekit-compose:0.8.7")

            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.1")

            implementation("io.github.luca992.getenv-kt:getenv:0.4.0")

            implementation("org.jetbrains.kotlinx:kotlinx-io-core:0.5.4")
            implementation("io.ktor:ktor-client-logging:3.0.1")
            implementation("org.slf4j:slf4j-simple:2.0.16")

            implementation("io.github.koalaplot:koalaplot-core:0.7.0")


        }




        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.ktor.client.java)



        }
    }
}

android {
    namespace = "su.pank.sprintlens"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "su.pank.sprintlens"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "su.pank.sprintlens.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "su.pank.sprintlens"
            packageVersion = "1.0.0"
        }
    }
}
