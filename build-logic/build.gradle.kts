plugins {
    `kotlin-dsl`
}

group = "com.teamwable.wable.buildlogic"

dependencies {
    compileOnly(libs.agp)
    compileOnly(libs.kotlin.gradleplugin)
    compileOnly(libs.compose.compiler.gradle.plugin)
}

gradlePlugin {
    plugins {
        create("android-application") {
            id = "com.teamwable.wable.application"
            implementationClass = "com.teamwable.wable.plugin.AndroidApplicationPlugin"
        }
        create("android-feature") {
            id = "com.teamwable.wable.feature"
            implementationClass = "com.teamwable.wable.plugin.AndroidFeaturePlugin"
        }
        create("android-kotlin") {
            id = "com.teamwable.wable.kotlin"
            implementationClass = "com.teamwable.wable.plugin.AndroidKotlinPlugin"
        }
        create("android-hilt") {
            id = "com.teamwable.wable.hilt"
            implementationClass = "com.teamwable.wable.plugin.AndroidHiltPlugin"
        }
        create("kotlin-serialization") {
            id = "com.teamwable.wable.serialization"
            implementationClass = "com.teamwable.wable.plugin.KotlinSerializationPlugin"
        }
        create("android-test") {
            id = "com.teamwable.wable.test"
            implementationClass = "com.teamwable.wable.plugin.AndroidTestPlugin"
        }
        create("android-retrofit") {
            id = "com.teamwable.wable.retrofit"
            implementationClass = "com.teamwable.wable.plugin.RetrofitPlugin"
        }
        create("android-network") {
            id = "com.teamwable.wable.network"
            implementationClass = "com.teamwable.wable.plugin.AndroidNetworkPlugin"
        }
        create("android-compose") {
            id = "com.teamwable.wable.compose"
            implementationClass = "com.teamwable.wable.plugin.AndroidComposePlugin"
        }
        create("android-compose-feature") {
            id = "com.teamwable.wable.compose.feature"
            implementationClass = "com.teamwable.wable.plugin.AndroidComposeFeaturePlugin"
        }
    }
}

tasks.named("compileTestKotlin") {
    dependsOn("clean")
}

tasks.named("processTestResources") {
    dependsOn("compileTestKotlin")
}

tasks.named("testClasses") {
    dependsOn("processTestResources")
}
