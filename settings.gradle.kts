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

rootProject.name = "Homework"
include(":app")
include(":domain")
include(":data:datasource")
include(":data:network")
include(":data:persistence")
include(":data:repository")
include(":features:core")
include(":features:feed")
include(":features:favorites")
include(":features:detail")
