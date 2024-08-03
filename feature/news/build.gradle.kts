@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.teamwable.wable.feature")
    id("com.teamwable.wable.test")
}
android {
    namespace = "com.teamwable.news"
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