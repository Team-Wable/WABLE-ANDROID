@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.teamwable.wable.feature")
}
android {
    namespace = "com.teamwable.data"
}

dependencies {
    implementation(project(":core:network"))
    implementation(project(":core:model"))
}