@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.teamwable.wable.data")
}
android {
    namespace = "com.teamwable.data"
}

dependencies {
    implementation(project(":core:network"))
    implementation(project(":core:model"))
}