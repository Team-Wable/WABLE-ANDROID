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
    implementation(project(":core:designsystem"))
    implementation(project(":core:model"))
    implementation(project(":core:data"))

    // AndroidX
    implementation(libs.fragment.ktx)
    // TODO:이동 없으면 지워도 됨
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Third Party
    implementation(libs.coil.core)
}

ktlint {
    version = "0.49.1"
    android.set(true)
    debug.set(true)
    coloredOutput.set(true)
    verbose.set(true)
    outputToConsole.set(true)
}
