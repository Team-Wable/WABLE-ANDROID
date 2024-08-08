@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.android.library")
    id("com.teamwable.wable.kotlin")
    id("com.teamwable.wable.hilt")
}
android {
    namespace = "com.teamwable.data"
}

dependencies {
    implementation(project(":core:network"))
    implementation(project(":core:model"))
}