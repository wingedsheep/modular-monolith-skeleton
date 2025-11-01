import com.arcmutate.gradle.github.GithubTask
import de.undercouch.gradle.tasks.download.Download

plugins {
    id("com.autonomousapps.dependency-analysis")
    id("com.arcmutate.github")
    id("info.solidsoft.pitest.aggregator")
}

tasks.withType<GithubTask>().configureEach {
    notCompatibleWithConfigurationCache("because pitest does not support configuration cache")
}

pitestGithub {
    mutantEmoji = ":space_invader:"
    deleteOldSummaries = true
}

val versionCatalog: VersionCatalog = rootProject.extensions.getByType<VersionCatalogsExtension>().named("libs")

dependencies {
    pitestReport(versionCatalog.findLibrary("pitest-kotlin-plugin").get())
}

dependencyAnalysis {
    issues {
        all {
            onAny {
                severity("fail")
            }
            onUsedTransitiveDependencies {
                severity("ignore")
            }
        }
    }
}
