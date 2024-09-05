import java.util.Properties

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.teamwable.wable.network")
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
    implementation(project(":core:datastore"))
    implementation(libs.androidx.exifinterface)
    implementation(libs.paging)
    implementation(libs.process.phoenix)
}
