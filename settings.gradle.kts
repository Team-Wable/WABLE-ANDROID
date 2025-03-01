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
        maven { setUrl("https://devrepo.kakao.com/nexus/content/groups/public/") }
    }
}

rootProject.name = "Wable"
include(":app")
include(":feature:main")
include(":core:ui")
include(":core:common")
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
include(":core:designsystem")
include(":core:datastore")
include(":feature:onboarding")
include(":feature:community")
