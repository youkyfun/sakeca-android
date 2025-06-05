plugins {
    alias(libs.plugins.android.dynamic.feature)
    alias(libs.plugins.kotlin.android)
    kotlin("plugin.serialization") version "2.1.21"
    id("androidx.navigation.safeargs")
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
}
android {
    namespace = "com.youkydesign.favorite"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":app"))

    // Shimmer
    implementation(libs.shimmer)

    // Lottie
    implementation(libs.lottie)

    // Kotlin Flow
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // Dagger
    implementation(libs.dagger)
    ksp(libs.dagger.compiler)

    implementation(libs.glide)

    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)

    // Views/Fragments integration/Dynamic Feature support
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    implementation(libs.androidx.navigation.dynamic.features.fragment)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.mockito.inline)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.androidx.core.testing)
    implementation(libs.androidx.paging.runtime.ktx)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    debugImplementation(libs.leakcanary.android)
}