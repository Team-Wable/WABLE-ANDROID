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
    implementation(project(":core:datastore"))
    implementation(project(":core:common"))

    implementation(libs.paging)
    implementation(libs.okhttp)
    implementation(libs.androidx.exifinterface)
    implementation(libs.jsoup)

    // androidx work manager
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.hilt.work)
    ksp(libs.androidx.hilt.compiler)

    implementation(libs.timber)
}
