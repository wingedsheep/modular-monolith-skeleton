plugins {
    id("buildlogic.kotlin-conventions")
}

val libs = project.extensions.getByType<VersionCatalogsExtension>().named("libs")

dependencies {
    testImplementation(libs.findLibrary("cucumber.java").get())
    testImplementation(libs.findLibrary("cucumber.junit.platform.engine").get())
    testImplementation(libs.findLibrary("cucumber.spring").get())
    testImplementation("org.junit.platform:junit-platform-suite")
}
