import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

// ✅ Remove force resolution to let Gradle pick compatible versions
configurations.all {
    resolutionStrategy {
        // Only force Material and AppCompat if you need specific versions
        force("androidx.appcompat:appcompat:1.7.0")
        force("com.google.android.material:material:1.12.0")
    }
}

// Read API_KEY from local.properties
val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localPropertiesFile.inputStream().use { localProperties.load(it) }
}
val apiKey: String = localProperties.getProperty("API_KEY", "")
val venueName: String = localProperties.getProperty("VENUE_NAME", "")

android {
    namespace = "com.example.moudleandroid"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.moudleandroid"
        minSdk = 23
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "API_KEY", "\"$apiKey\"")
        buildConfigField("String", "VENUE_NAME", "\"$venueName\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    // ✅ Let activity:1.9.2 automatically pull the compatible androidx.core version it needs
    implementation("androidx.activity:activity:1.9.2")
    // ✅ Remove explicit androidx.core dependencies - they'll be pulled transitively
    // This allows activity to use the androidx.core version it's compatible with
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")
    implementation("com.example.navigation_sdk:flutter_release:1.0")
}