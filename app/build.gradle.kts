import com.android.aaptcompiler.parseNavigation
//mport org.jetbrains.kotlin.gradle.internal.kapt.incremental.UnknownSnapshot.classpath

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
    id ("dagger.hilt.android.plugin")

}

android {
    namespace = "com.example.mynewsapp"
    compileSdk = 34
    viewBinding{
        enable = true
    }

    defaultConfig {
        applicationId = "com.example.mynewsapp"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.ui.desktop)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //my dependencies
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    implementation (libs.androidx.room.runtime)

    // Kotlin Extensions and Coroutines support for Room
    implementation (libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler.v225)

    // Coroutines
    implementation (libs.kotlinx.coroutines.core)
    implementation (libs.kotlinx.coroutines.android)

    // Coroutine Lifecycle Scopes
    implementation (libs.androidx.lifecycle.viewmodel.ktx.v220)
    implementation (libs.androidx.lifecycle.runtime.ktx)

    // Retrofit
    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.logging.interceptor)

    // Navigation Components
    implementation (libs.androidx.navigation.fragment.ktx)
    implementation (libs.androidx.navigation.ui.ktx)
    implementation (libs.material.v180)
    // Glide
    implementation (libs.glide)
    kapt (libs.compiler)
    //val nav_version = "2.7.7"
    (libs.androidx.navigation.safe.args.gradle.plugin)
    (libs.androidx.navigation.safe.args.gradle.plugin.v230alpha04)

    //hilt dependencies
    /*
    annotationProcessor ("com.google.dagger:hilt-compiler:2.44")
    implementation ("com.google.dagger:hilt-android:2.44")
    kapt ("com.google.dagger:hilt-compiler:2.44")
     */
    implementation ("com.google.dagger:hilt-android:2.52")
    kapt ("com.google.dagger:hilt-compiler:2.52")
    //implementation (libs.androidx.hilt.lifecycle.viewmodel)
    kapt ("androidx.hilt:hilt-compiler:1.2.0")

    //paging
    implementation("androidx.paging:paging-runtime:3.3.2")

}