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
include(":ecoglobal:application")
include(":ecoglobal:product-catalog:product-catalog-api")
include(":ecoglobal:product-catalog:product-catalog-impl")
include(":ecoglobal:product-catalog:product-catalog-worldview")
include(":ecoglobal:test:use-case")
