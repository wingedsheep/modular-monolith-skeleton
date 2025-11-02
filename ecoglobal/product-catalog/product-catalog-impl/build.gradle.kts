plugins {
    id("buildlogic.kotlin-conventions")
}

dependencies {
    implementation(project(":product-catalog:product-catalog-api"))
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
}
