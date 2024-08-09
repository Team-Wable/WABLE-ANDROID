plugins {
    id("com.teamwable.wable.compose.feature")
    alias(libs.plugins.ktlint)
}

android {
    namespace = "com.teamwable.main_compose"
}

dependencies {
    implementation(project(":feature:auth"))
}
