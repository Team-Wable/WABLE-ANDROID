@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.teamwable.wable.feature")
}
android {
    namespace = "com.teamwable.ui"
}
dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:domain"))

    // AndroidX
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.paging)
    implementation(libs.androidx.paging.compose)

    // Third Party
    implementation(libs.glide)
    implementation(libs.lottie)
}
