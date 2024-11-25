@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.teamwable.wable.feature")
    id("com.teamwable.wable.test")
    id("com.teamwable.wable.compose.feature")
}
android {
    namespace = "com.teamwable.news"
}

dependencies {
    implementation(project(":core:ui"))

    // AndroidX
    implementation(libs.fragment.ktx)
    // TODO:이동 없으면 지워도 됨
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Third Party
    implementation(libs.coil.core)
    implementation(libs.lottie)
    implementation(libs.bundles.landscapist.glide)
    implementation(libs.paging)
    implementation(libs.androidx.paging.compose)
}
