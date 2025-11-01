plugins {
    id("com.autonomousapps.dependency-analysis")
    kotlin("jvm")
    kotlin("plugin.spring")
    id("buildlogic.jacoco-conventions")
    id("buildlogic.pitest-conventions")
    id("buildlogic.errorprone")
    id("java-test-fixtures")
}

val versionCatalog = rootProject.extensions.getByType<VersionCatalogsExtension>().named("libs")

val mockitoAgent: Configuration by configurations.creating {
    isTransitive = false
}

kotlin {
    jvmToolchain(JavaLanguageVersion.of(versionCatalog.findVersion("java").get().requiredVersion).asInt())
    compilerOptions {
        javaParameters = true
        freeCompilerArgs.add("-Xjsr305=strict")
    }
}

dependencies {
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(versionCatalog.findLibrary("mockito").get())
    testImplementation(versionCatalog.findLibrary("mockito-kotlin").get())
    testImplementation(versionCatalog.findLibrary("mockk").get())

    testFixturesImplementation(versionCatalog.findLibrary("mockito-kotlin").get())

    mockitoAgent(versionCatalog.findLibrary("mockito").get()) {
        isTransitive = false
    }
}

ext["junit-jupiter.version"] = versionCatalog.findVersion("junit").get().requiredVersion

tasks.withType<Test> {
    useJUnitPlatform()
    jvmArgs("-javaagent:${mockitoAgent.asPath}")
}
