plugins {
    id("com.teamwable.wable.compose")
}

android {
    namespace = "com.teamwable.designsystem"
}
dependencies {
    implementation(project(":core:common"))

    implementation(libs.androidx.paging.compose)
    implementation(libs.bundles.landscapist.glide)
}
