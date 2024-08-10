import java.io.FileInputStream
import java.util.Properties

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.teamwable.wable.application")
    id("com.teamwable.wable.test")
    alias(libs.plugins.google.services)
}

val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
keystoreProperties.load(FileInputStream(keystorePropertiesFile))

android {
    namespace = "com.teamwable.wable"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.teamwable.wable"
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.appVersion.get()
    }

    signingConfigs {
        getByName("debug") {
            storeFile = file("./keystore/debug.keystore")
        }
        create("release") {
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
            storeFile = file(keystoreProperties["storeFile"] as String)
            storePassword = keystoreProperties["storePassword"] as String
        }
    }

    buildTypes {
        debug {
            signingConfig = signingConfigs.getByName("debug")
        }
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
}

dependencies {
    implementation(project(":feature:main"))
    implementation(project(":feature:main-compose"))
    implementation(project(":core:ui"))
    implementation(project(":core:common"))

    //firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
}
