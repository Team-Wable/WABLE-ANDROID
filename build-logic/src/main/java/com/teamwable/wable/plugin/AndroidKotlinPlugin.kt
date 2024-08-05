package com.teamwable.wable.plugin

import com.android.build.gradle.BaseExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

class AndroidKotlinPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(plugins) {
            apply("kotlin-android")
        }

        val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
        extensions.getByType<BaseExtension>().apply {
            setCompileSdkVersion(libs.findVersion("compileSdk").get().requiredVersion.toInt())

            defaultConfig {
                minSdk = libs.findVersion("minSdk").get().requiredVersion.toInt()
                targetSdk = libs.findVersion("targetSdk").get().requiredVersion.toInt()
            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_17
                targetCompatibility = JavaVersion.VERSION_17
            }

            tasks.withType<KotlinJvmCompile>().configureEach {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_17)
                    freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
                }
            }
        }

        dependencies {
            "implementation"(libs.findLibrary("kotlin").get())
            "implementation"(libs.findLibrary("kotlin.coroutines").get())
        }
    }
}