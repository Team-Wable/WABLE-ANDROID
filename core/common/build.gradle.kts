import java.util.Properties

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.teamwable.wable.feature")
}

val properties = Properties().apply {
    load(rootProject.file("local.properties").inputStream())
}

android {
    namespace = "com.teamwable.common"

    buildTypes {
        getByName("debug") {
            val amplitudeDevApiKey = properties["amplitude.dev.api.key"] as? String ?: ""
            buildConfigField("String", "AMPLITUDE_API_KEY", "\"${amplitudeDevApiKey}\"")
        }

        getByName("release") {
            val amplitudeRelApiKey = properties["amplitude.rel.api.key"] as? String ?: ""
            buildConfigField("String", "AMPLITUDE_API_KEY", "\"${amplitudeRelApiKey}\"")
        }
    }

    buildFeatures.apply {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":core:model"))

    // Third Party
    implementation(libs.amplitude)
    implementation(libs.google.play.services)
}
