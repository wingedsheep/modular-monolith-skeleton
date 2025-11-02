plugins {
    id("buildlogic.kotlin-conventions")
    id("buildlogic.cucumber-conventions")
}

dependencies {
    // implementation(project(":app"))
    // implementation(project(":ecoglobal:inventory:inventory-worldview"))
    // implementation(project(":ecoglobal:order-fulfillment:order-fulfillment-worldview"))
    implementation(project(":product-catalog:product-catalog-worldview"))
}
