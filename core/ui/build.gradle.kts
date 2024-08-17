@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.teamwable.wable.feature")
}
android {
    namespace = "com.teamwable.ui"
}
dependencies {
    implementation(project(":core:common"))
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
}
