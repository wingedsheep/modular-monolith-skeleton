import net.ltgt.gradle.errorprone.errorprone

plugins {
    java
    id("net.ltgt.errorprone")
}

val versionCatalog: VersionCatalog = rootProject.extensions.getByType<VersionCatalogsExtension>().named("libs")

dependencies {
    errorprone(versionCatalog.findLibrary("error-prone").get())
}

tasks.withType<JavaCompile>().configureEach {
    options.errorprone.disableWarningsInGeneratedCode.set(true)
}
