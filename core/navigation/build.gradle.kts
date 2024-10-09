plugins {
    id("com.teamwable.wable.compose")
    id("com.teamwable.wable.serialization")
}

android {
    namespace = "com.teamwable.navigation"
}

dependencies {
    implementation(project(":core:model"))
    implementation(libs.androidx.compose.navigation)
}
