package com.teamwable.wable.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

class AndroidComposePlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        plugins.apply("com.android.library")
        apply<AndroidKotlinPlugin>()
        configureComposeAndroid()
    }
}
