@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.teamwable.wable.feature")
    id("com.teamwable.wable.test")
    alias(libs.plugins.kotlin.android)
}
android {
    namespace = "com.teamwable.main"
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:designsystem:xml"))
    implementation(project(":core:model"))
    implementation(project(":core:data"))
    implementation(project(":feature:home"))
    implementation(project(":feature:news"))
    implementation(project(":feature:notification"))
    implementation(project(":feature:posting"))
    implementation(project(":feature:profile"))

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