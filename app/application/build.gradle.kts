plugins {
    kotlin("jvm")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

dependencies {
    // The application module depends on the implementation of the domain modules
    implementation(project(":app:sample-domain:sample-domain-impl"))

    // Standard Spring Boot starter
    implementation("org.springframework.boot:spring-boot-starter-web")
}
