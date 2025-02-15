plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlinKapt)
}

android {
    namespace = "com.example.demoapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.demoapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }

        debug {
            applicationIdSuffix = ".debug"
            isMinifyEnabled = false
        }
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    implementation(libs.dagger)
    implementation(libs.androidx.browser)
    implementation(libs.androidx.paging.common.android)
    implementation(libs.androidx.paging.runtime.ktx)
    kapt(libs.daggerCompiler)

    implementation(libs.retrofit)
    implementation(libs.gson)

    implementation (libs.androidx.navigation.fragment.ktx)

    implementation(libs.glide)

    implementation (libs.lottie)

    debugImplementation (libs.chucker)
    releaseImplementation (libs.chucker.noop)
    
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}