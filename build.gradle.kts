// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    //id("androidx.navigation.safeargs")
}

buildscript {
    //ext.kotlin_version = '1.3.71'
    repositories {
        google()
        mavenCentral()
    }

    dependencies {

        classpath (libs.androidx.navigation.safe.args.gradle.plugin)
        classpath (libs.navigation.safe.args.gradle.plugin)
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.52")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.0.20")
    }

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }