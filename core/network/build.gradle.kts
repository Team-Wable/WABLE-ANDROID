import java.util.Properties

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.teamwable.wable.data")
}

val properties = Properties().apply {
    load(rootProject.file("local.properties").inputStream())
}

android {
    namespace = "com.teamwable.network"

    buildTypes {
        getByName("debug") {
            val wableDevBaseUrl = properties["wable.dev.base.url"] as? String ?: ""
            buildConfigField("String", "WABLE_BASE_URL", "\"${wableDevBaseUrl}\"")

            //TODO:테스트용 나중에 지우기
            val testToken = properties["test.token"] as? String ?: ""
            buildConfigField("String", "TEST_TOKEN", "\"${testToken}\"")
        }

        getByName("release") {
            val wableRelBaseUrl = properties["wable.rel.base.url"] as? String ?: ""
            buildConfigField("String", "WABLE_BASE_URL", "\"${wableRelBaseUrl}\"")
        }
    }

    buildFeatures.apply {
        buildConfig = true
    }

}

dependencies {
    implementation(project(":core:model"))
}
