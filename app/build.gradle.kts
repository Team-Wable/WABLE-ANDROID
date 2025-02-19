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

val properties = Properties().apply {
    load(rootProject.file("local.properties").inputStream())
}

fun String.removeQuotes(): String {
    return this.replace("\"", "")
}

android {
    namespace = "com.teamwable.wable"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.teamwable.wable"
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.appVersion.get()

        val kakaoAppKey = properties["kakao.app.key"].toString().removeQuotes()

        buildConfigField("String", "KAKAO_APP_KEY", "\"$kakaoAppKey\"")
        manifestPlaceholders["kakaoAppkey"] = kakaoAppKey
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
            applicationIdSuffix = ".debug"
            manifestPlaceholders["appName"] = "@string/dev_app_name"
            manifestPlaceholders["roundAppIcon"] = "@mipmap/ic_launcher_dev_round"
            manifestPlaceholders["appIcon"] = "@mipmap/ic_launcher_dev"
        }
        release {
            signingConfig = signingConfigs.getByName("release")
            manifestPlaceholders["appName"] = "@string/rel_app_name"
            manifestPlaceholders["roundAppIcon"] = "@mipmap/ic_launcher_round"
            manifestPlaceholders["appIcon"] = "@mipmap/ic_launcher"
            // TO DO: 추후 난독화 설정하겠습니다.
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

    // firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.google.play.services)
    implementation(libs.firebase.messaging.ktx)

    // kakao
    implementation(libs.kakao.login)
}
