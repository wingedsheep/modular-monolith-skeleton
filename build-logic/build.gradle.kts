plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

fun convertToPluginDependency(plugin: Provider<PluginDependency>): String {
    val pluginId = plugin.get().pluginId
    val pluginVersion = plugin.get().version
    return "$pluginId:$pluginId.gradle.plugin:$pluginVersion"
}

dependencies {
    implementation(convertToPluginDependency(libs.plugins.arcmutate.github))
    implementation(convertToPluginDependency(libs.plugins.dependency.analysis))
    implementation(convertToPluginDependency(libs.plugins.download))
    implementation(convertToPluginDependency(libs.plugins.errorprone))
    implementation(convertToPluginDependency(libs.plugins.kotlin.jvm))
    implementation(convertToPluginDependency(libs.plugins.kotlin.spring))
    implementation(convertToPluginDependency(libs.plugins.pitest))
    implementation(libs.arcmutate.gradle.common)
}
