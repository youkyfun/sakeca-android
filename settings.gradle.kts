@file:Suppress("UnstableApiUsage")

include(":feature:shopping")


include(":feature:discovery")


include(":feature:details")


include(":feature")


include(":core:designsystem")


pluginManagement {
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

rootProject.name = "Recipe App"
include(":app")
include(":core")
include(":favorite")
