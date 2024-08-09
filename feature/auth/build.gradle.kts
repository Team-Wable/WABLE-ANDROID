plugins {
    id("com.teamwable.wable.compose.feature")
    alias(libs.plugins.ktlint)
}

android {
    namespace = "com.teamwable.auth"
}

dependencies {
}

ktlint {
    version = "0.49.1"
    android.set(true)
    debug.set(true)
    coloredOutput.set(true)
    verbose.set(true)
    outputToConsole.set(true)
}
