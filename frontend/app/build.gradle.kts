import java.util.Properties
import java.io.FileInputStream

val configProperties = Properties()
val configFile = rootProject.file("config.properties")
if (configFile.exists()) {
    configProperties.load(FileInputStream(configFile))
}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    buildFeatures {
        buildConfig = true
    }
    namespace = "com.example.deliverable_1_seg"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.deliverable_1_seg"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            buildConfigField("String", "API_KEY", "\"${configProperties["API_KEY"]}\"")
            buildConfigField("String", "API_SECRET", "\"${configProperties["API_SECRET"]}\"")
            buildConfigField("String", "EMAIL", "\"${configProperties["EMAIL"]}\"")
            buildConfigField("boolean", "IS_PRODUCTION", "false")
        }
        release {
            isMinifyEnabled = false
            buildConfigField("boolean", "IS_PRODUCTION", "true")
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
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.database)
    implementation(libs.firebase.auth)

    testImplementation(libs.junit)

    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.androidx.espresso.intents)
    androidTestImplementation(libs.androidx.rules)
    androidTestImplementation(libs.androidx.junit.v115)
}
