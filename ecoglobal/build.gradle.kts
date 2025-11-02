plugins {
    jacoco
    alias(libs.plugins.spring.boot) apply false
    alias(libs.plugins.spring.dependency.management)
    id("buildlogic.project-root")
}

group = "com.example"
version = project.findProperty("version") ?: "0.0.1-SNAPSHOT"

extra["kotlin.version"] = libs.versions.kotlin.get()

subprojects {
    apply(plugin = "io.spring.dependency-management")

    dependencyManagement {
        imports {
            mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
        }
    }
}
