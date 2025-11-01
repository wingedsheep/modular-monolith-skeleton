plugins {
    id("buildlogic.kotlin-conventions")
    id("org.springframework.boot")
}

dependencies {
    implementation(project(":ecoglobal:product-catalog:product-catalog-impl"))
    implementation("org.springframework.boot:spring-boot-starter-web")
}
