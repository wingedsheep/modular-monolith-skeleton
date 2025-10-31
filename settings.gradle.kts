dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
    }
}

rootProject.name = "EcoGlobal"

includeBuild("build-logic")
includeBuild("common")

// Deployable with its domains
include(":app:application")
include(":app:sample-domain:sample-domain-api")
include(":app:sample-domain:sample-domain-impl")
