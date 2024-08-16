plugins {
    alias(libs.plugins.android.library)
    id("com.teamwable.wable.kotlin")
    id("com.teamwable.wable.hilt")
}

android {
    namespace = "com.teamwable.datastore"
}

dependencies {
    implementation(libs.bundles.datastore)
}
