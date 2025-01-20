import shadow.bundletool.com.android.ddmlib.Log
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
    id("vkid.manifest.placeholders")
}

android {
    namespace = "android.learn.vkapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "android.learn.vkapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val keystoreFile = project.rootProject.file("keys.properties")
        val properties = Properties()
        properties.load(keystoreFile.inputStream())

        val vkidRedirectHost = properties.getProperty("VKIDRedirectHost") ?: ""
        val vkidRedirectScheme = properties.getProperty("VKIDRedirectScheme") ?: ""
        val vkidClientId = properties.getProperty("VKIDClientID") ?: ""
        val vkidClientSecret = properties.getProperty("VKIDClientSecret") ?: ""

        addManifestPlaceholders(
            mapOf(
                "VKIDClientID" to vkidClientId,
                "VKIDClientSecret" to vkidClientSecret,
                "VKIDRedirectHost" to vkidRedirectHost,
                "VKIDRedirectScheme" to vkidRedirectScheme,
            )
        )
    }
    kotlinOptions {
        jvmTarget = "1.8"
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
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.vk.sdk.core)
    implementation(libs.vk.sdk.api)
    implementation(libs.vk.onetap)
    implementation(libs.vk.id)
    implementation(libs.vk.sdk)

    coreLibraryDesugaring(libs.desugar)

    implementation(libs.okHttp)
    implementation(libs.okHttpLog)
    implementation(libs.dagger)
    implementation(libs.dagger.support)
    ksp(libs.dagger.compiler)
    ksp(libs.dagger.processor)
    implementation(libs.lifecycle)
    implementation(libs.androidx.room)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)
    implementation(libs.cardiew)
    implementation(libs.recyclerview)
    implementation(libs.retrofit.gson)
    implementation(libs.retrofit)
    implementation(libs.picasso)
    implementation(libs.work.runtime)
    implementation(libs.fragment)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}