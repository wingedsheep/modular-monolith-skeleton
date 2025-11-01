pluginManagement {
    includeBuild("build-logic")
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
    }
}

rootProject.name = "EcoGlobal"

includeBuild("common")

// Deployable with its domains
include(":app:application")
include(":app:sample-domain:sample-domain-api")
include(":app:sample-domain:sample-domain-impl")
include(":ecoglobal:application")
include(":ecoglobal:test:use-case")
