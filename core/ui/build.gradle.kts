@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.teamwable.wable.feature")
}
android {
    namespace = "com.teamwable.ui"
}
dependencies {
    implementation(project(":core:common"))

    // AndroidX
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Third Party
    implementation(libs.glide)
}
