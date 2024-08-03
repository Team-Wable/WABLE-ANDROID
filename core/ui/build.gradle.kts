@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.teamwable.wable.feature")
}
android {
    namespace = "com.teamwable.main"
    buildFeatures {
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}