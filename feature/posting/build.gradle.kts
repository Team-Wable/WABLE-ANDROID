@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.teamwable.wable.feature")
    id("com.teamwable.wable.test")
    alias(libs.plugins.ktlint)
}
android {
    namespace = "com.teamwable.posting"
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:designsystem:xml"))
    implementation(project(":core:model"))
    implementation(project(":core:data"))

    // AndroidX
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.fragment.ktx)
    implementation(libs.splash.screen)

    // Third Party
    implementation(libs.coil.core)
    implementation(libs.material)
    implementation(libs.androidx.appcompat)
}

ktlint {
    version = "0.49.1"
    android.set(true)
    debug.set(true)
    coloredOutput.set(true)
    verbose.set(true)
    outputToConsole.set(true)
}
