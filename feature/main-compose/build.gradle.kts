plugins {
    id("com.teamwable.wable.compose.feature")
}

android {
    namespace = "com.teamwable.main_compose"
}

dependencies {
    implementation(project(":feature:auth"))
}
