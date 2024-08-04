@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.teamwable.wable.feature")
    alias(libs.plugins.google.services)
}
android {
    namespace = "com.teamwable.network"
}

dependencies {
    implementation(project(":core:model"))

    //firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
}
