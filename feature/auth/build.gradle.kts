plugins {
    id("com.teamwable.wable.compose.feature")
}

android {
    namespace = "com.teamwable.auth"
}

dependencies {
    implementation(libs.kakao.login)
}
