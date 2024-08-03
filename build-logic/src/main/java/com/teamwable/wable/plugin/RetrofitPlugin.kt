package com.teamwable.wable.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class RetrofitPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {

        val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
        dependencies {
            "implementation"(platform(libs.findLibrary("okhttp.bom").get()))
            "implementation"(libs.findBundle("retrofit").get())
        }
    }
}