plugins {
    kotlin("jvm")
}

dependencies {
    // API modules can depend on common modules
    implementation("com.ecoglobal:common-ean")
    implementation("com.ecoglobal:common-time")
}
