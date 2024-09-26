@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.teamwable.wable.feature")
}

fun String.removeQuotes(): String {
    return this.replace("\"", "")
}

android {
    namespace = "com.teamwable.common"

    defaultConfig {
        val amplitudeApiKey = properties["amplitude.api.key"].toString().removeQuotes()
        buildConfigField("String", "AMPLITUDE_API_KEY", "\"${amplitudeApiKey}\"")
    }
}

dependencies {
    // Third Party
    implementation(libs.amplitude)
}
