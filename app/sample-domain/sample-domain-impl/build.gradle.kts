plugins {
    kotlin("jvm")
}

dependencies {
    // Implementation modules depend on their own API module
    api(project(":app:sample-domain:sample-domain-api"))

    // They can also depend on common modules
    implementation("com.ecoglobal:common-ean")
    implementation("com.ecoglobal:common-time")
}
