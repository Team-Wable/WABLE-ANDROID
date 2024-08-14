@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.teamwable.wable.feature")
    id("com.teamwable.wable.test")
}
android {
    namespace = "com.teamwable.main"
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:data"))
    implementation(project(":feature:home"))
    implementation(project(":feature:news"))
    implementation(project(":feature:notification"))
    implementation(project(":feature:posting"))
    implementation(project(":feature:profile"))

    // AndroidX
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.splash.screen)
}