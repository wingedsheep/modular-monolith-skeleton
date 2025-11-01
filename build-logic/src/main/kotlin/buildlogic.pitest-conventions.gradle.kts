plugins {
    java
    id("info.solidsoft.pitest")
    id("de.undercouch.download")
}

val versionCatalog: VersionCatalog = rootProject.extensions.getByType<VersionCatalogsExtension>().named("libs")

dependencies {
    pitest(versionCatalog.findLibrary("pitest-accelerator-junit5").get())
    pitest(versionCatalog.findLibrary("pitest-arcmutate-spring").get())
    pitest(versionCatalog.findLibrary("pitest-git-plugin").get())
    pitest(versionCatalog.findLibrary("pitest-kotlin-plugin").get())
}

pitest {
    excludedTestClasses = listOf("*CucumberTestRunner")
    exportLineCoverage = true
    failWhenNoMutations = false
    historyInputLocation = layout.buildDirectory.file("pitest/pitest-history.txt")
    historyOutputLocation = layout.buildDirectory.file("pitest/pitest-history.txt")
    junit5PluginVersion = versionCatalog.findVersion("pitest-junit5-plugin").get().requiredVersion
    outputFormats = listOf("gitci", "html", "xml")
    pitestVersion = versionCatalog.findVersion("pitest").get().requiredVersion
    targetClasses = listOf("com.example.*")
    threads = Runtime.getRuntime().availableProcessors()
    timeoutConstInMillis = 30000
    timeoutFactor = BigDecimal.valueOf(2L)
    timestampedReports = false
    useClasspathFile = true
    reportAggregator {
        maxSurviving = 0
    }
}
