plugins {
    id("com.teamwable.wable.compose")
}

android {
    namespace = "com.teamwable.designsystem"
}
dependencies {
    implementation(project(":core:common"))
}

