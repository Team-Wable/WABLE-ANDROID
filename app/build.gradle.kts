@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.teamwable.wable.application")
    id("com.teamwable.wable.test")
}

android {
    namespace = "com.teamwable.wable"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.teamwable.wable"
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.appVersion.get()
    }

    buildTypes {
        release {
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
    implementation(project(":core:ui"))
    implementation(project(":core:designsystem:xml"))
}
