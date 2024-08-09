package com.teamwable.wable.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidComposeFeaturePlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        apply<AndroidComposePlugin>()
        apply<AndroidHiltPlugin>()

        val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
        dependencies {
            "implementation"(libs.findLibrary("androidx.lifecycle.runtimeCompose").get())
            "implementation"(libs.findLibrary("androidx.lifecycle.viewModelCompose").get())
            "implementation"(libs.findLibrary("androidx.activity.compose").get())
            "implementation"(libs.findLibrary("androidx.hilt.navigation.compose").get())
            "implementation"(libs.findLibrary("androidx.compose.navigation").get())
            "implementation"(libs.findLibrary("kotlinx.collections.immutable").get())
            "testImplementation"(libs.findLibrary("androidx.compose.navigation.test").get())

            // Adding module dependencies
            "implementation"(project(":core:navigation"))
            "implementation"(project(":core:model"))
            "implementation"(project(":core:data"))
            "implementation"(project(":core:designsystem-compose"))
            "implementation"(project(":core:designsystem"))
        }
    }
}
