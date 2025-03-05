plugins {
    id("com.teamwable.wable.feature")
    id("com.teamwable.wable.test")
    id("com.teamwable.wable.compose.feature")
}

android {
    namespace = "com.teamwable.community"
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:data"))

    implementation(libs.fragment.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
}
