plugins {
    // Apply the Kotlin JVM plugin to all projects that need it.
    kotlin("jvm") version "1.9.22" apply false
    // Apply the Spring Boot plugin to the application project.
    id("org.springframework.boot") version "3.2.0" apply false
    // Apply the Spring Dependency Management plugin to manage dependency versions.
    id("io.spring.dependency-management") version "1.1.4" apply false
}
