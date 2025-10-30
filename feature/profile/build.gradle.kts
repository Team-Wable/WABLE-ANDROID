@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.teamwable.wable.feature")
    id("com.teamwable.wable.compose.feature")
    id("com.teamwable.wable.test")
}
android {
    namespace = "com.teamwable.profile"
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:data"))
    implementation(project(":feature:onboarding"))

    // AndroidX
    implementation(libs.fragment.ktx)
    // TODO:이동 없으면 지워도 됨
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.paging)
    implementation(libs.swipe.refresh.layout)

    // Third Party
    implementation(libs.glide)
    implementation(libs.google.play.services)
    implementation(libs.firebase.messaging.ktx)
}
