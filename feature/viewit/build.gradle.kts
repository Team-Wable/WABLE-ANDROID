@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.teamwable.wable.feature")
    id("com.teamwable.wable.test")
    id("com.teamwable.wable.compose.feature")
}
android {
    namespace = "com.teamwable.viewit"
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:data"))
    implementation(project(":core:domain"))

    // AndroidX
    implementation(libs.fragment.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.paging)
    implementation(libs.androidx.paging.compose)
    implementation(libs.swipe.refresh.layout)

    // Third Party
    implementation(libs.glide)
    implementation(libs.lottie)
    implementation(libs.bundles.landscapist.glide)
}
