plugins {
    id("com.teamwable.wable.compose")
}

android {
    namespace = "com.teamwable.designsystem_compose"
}

dependencies {
    implementation(project(":core:designsystem"))
}
