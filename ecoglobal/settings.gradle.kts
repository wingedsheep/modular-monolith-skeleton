pluginManagement {
    includeBuild("../build-logic")
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
    }
}

rootProject.name = "ecoglobal"

includeBuild("../common")

// Deployable with its domains
include(":application")
include(":product-catalog:product-catalog-api")
include(":product-catalog:product-catalog-impl")
include(":product-catalog:product-catalog-worldview")
include(":test:use-case")
