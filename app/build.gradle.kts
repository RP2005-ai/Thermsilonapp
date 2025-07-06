plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.roheet.thermsilon"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.roheet.thermsilon"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions { jvmTarget = "11" }

    buildFeatures { compose = true }

    composeOptions {
        // 🔄  MATCHES Compose 1.6.x libraries
        kotlinCompilerExtensionVersion = "1.6.7"
    }
}

dependencies {
    /* ───────── Android core ───────── */
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    /* ───────── Compose BOM (2024‑10‑00) keeps every Compose lib in sync ───────── */
    implementation(platform("androidx.compose:compose-bom:2024.10.00"))

    // Core UI + tooling
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")

    // Material 3 (inherits version from BOM)
    implementation("androidx.compose.material3:material3")

    // Extended Material icons (inherits version from BOM)
    implementation("androidx.compose.material:material-icons-extended")

    // Animation (inherits version from BOM)
    implementation("androidx.compose.animation:animation")

    /* ───────── Navigation & extras ───────── */
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("io.coil-kt:coil-compose:2.6.0")
    implementation("com.jakewharton.threetenabp:threetenabp:1.4.4")

    /* ───────── Debug / tooling ───────── */
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    /* ───────── Testing ───────── */
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.10.00"))
    androidTestImplementation(libs.androidx.ui.test.junit4)
}
