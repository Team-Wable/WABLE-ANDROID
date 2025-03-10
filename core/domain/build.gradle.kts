plugins {
    alias(libs.plugins.android.library)
    id("com.teamwable.wable.kotlin")
    id("com.teamwable.wable.test")
}

android {
    namespace = "com.teamwable.domain"
}

dependencies {
    implementation(libs.inject)
}
