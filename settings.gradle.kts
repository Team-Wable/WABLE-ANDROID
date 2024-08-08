pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Wable"
include(":app")
include(":feature:main")
include(":core:ui")
include(":core:designsystem")
include(":core:data")
include(":core:network")
include(":core:model")
include(":core:navigation")
include(":feature:home")
include(":feature:posting")
include(":feature:news")
include(":feature:notification")
include(":feature:profile")
include(":feature:auth")
include(":feature:main-compose")
