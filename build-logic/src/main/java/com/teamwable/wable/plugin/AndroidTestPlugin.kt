package com.teamwable.wable.plugin

import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidTestPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        configureJUnit()
        val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
        extensions.getByType<BaseExtension>().apply {
            defaultConfig {
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }

            testOptions {
                unitTests {
                    isIncludeAndroidResources = true
                }
            }

            packagingOptions {
                resources.excludes.add("META-INF/LICENSE*")
            }
        }

        dependencies {
            "testImplementation"(libs.findLibrary("junit").get())
            "testImplementation"(libs.findLibrary("junit.jupiter.api").get())
            "testImplementation"(libs.findLibrary("kotlinx.coroutines.test").get())
            "testRuntimeOnly"(libs.findLibrary("junit.jupiter.engine").get())
            "androidTestImplementation"(libs.findBundle("androidx.android.test").get())
        }
    }
}
