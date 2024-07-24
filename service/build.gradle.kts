import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.parsing.parseBoolean

@Suppress("DSL_SCOPE_VIOLATION") // workaround for IntelliJ bug with Gradle Version Catalogs DSL in plugins
plugins {
    alias(libs.plugins.kotlin.lang)
    alias(libs.plugins.flyway.plugin)
    alias(libs.plugins.detekt.plugin)
    alias(libs.plugins.sonarqube.plugin)
    id(libs.plugins.jacoco.plugin.get().pluginId)
}

detekt {
    toolVersion = libs.versions.detekt.get()
    config.setFrom(file(properties["detekt.configuration.configurationFilePath"].toString()))
    buildUponDefaultConfig = parseBoolean(properties["detekt.configuration.buildUponDefault"].toString())
}

sonar {

    val projectKey = properties["sonar.projectKey"].toString()
    val projectName = properties["sonar.projectName"].toString()
    val hostUrl = properties["sonar.host.url"].toString()
    val coveragePlugin = properties["sonar.java.coveragePlugin"].toString()
    val reportPath = properties["sonar.coverage.jacoco.xmlReportPath"].toString()
    val isVerbose = properties["sonar.verbose"].toString().toBoolean()

    properties {
        property("sonar.projectKey", projectKey)
        property("sonar.projectName", projectName)
        property("sonar.host.url", hostUrl)
        property("sonar.java.coveragePlugin", coveragePlugin)
        property("sonar.coverage.jacoco.xmlReportPath", reportPath)
        property("sonar.verbose", isVerbose)
    }
}

tasks.named("sonarqube").configure {
    dependsOn("jacocoTestReport")
}

dependencies {
    detektPlugins(libs.detekt.formatting)
}

val projectSource = file(projectDir)
val configFile = files("$rootDir/${properties["detekt.configuration.configurationFilePath"]}")
val kotlinFiles = properties["detekt.configuration.kotlinFiles"].toString()
val resourceFiles = properties["detekt.configuration.resourceFiles"].toString()
val buildFiles = properties["detekt.configuration.buildFiles"].toString()

val detektAll by tasks.registering(Detekt::class) {
    description = properties["detetk.all.description"].toString()
    parallel = parseBoolean(properties["detetk.all.parallel"].toString())
    ignoreFailures = parseBoolean(properties["detetk.all.ignoreFailures"].toString())
    autoCorrect = parseBoolean(properties["detetk.all.autoCorrect"].toString())
    buildUponDefaultConfig = parseBoolean(properties["detetk.all.buildUponDefaultConfig"].toString())
    setSource(projectSource)
    config.setFrom(configFile)
    include(kotlinFiles)
    exclude(resourceFiles, buildFiles)
    reports {
        html.required.set(true)
        xml.required.set(false)
        txt.required.set(false)
    }
}

val detektGenerateBaseline = tasks.registering(DetektCreateBaselineTask::class) {
    description = properties["detetk.baseline.description"].toString()
    parallel = parseBoolean(properties["detetk.baseline.parallel"].toString())
    ignoreFailures = parseBoolean(properties["detetk.baseline.ignoreFailures"].toString())
    buildUponDefaultConfig = parseBoolean(properties["detetk.baseline.autoCorrect"].toString())
    setSource(projectSource)
    config.setFrom(configFile)
    include(kotlinFiles)
    exclude(resourceFiles, buildFiles)
}

allprojects {

    val libs = rootProject.libs

    apply {
        plugin(libs.plugins.kotlin.lang.get().pluginId)
        plugin(libs.plugins.jacoco.plugin.get().pluginId)
        plugin(libs.plugins.sonarqube.plugin.get().pluginId)
    }

    group = "com.thomas"
    version = properties["version"].toString()

    java.sourceCompatibility = JavaVersion.valueOf(libs.versions.target.get())
    java.targetCompatibility = JavaVersion.valueOf(libs.versions.target.get())

    repositories {
        mavenCentral()
        mavenLocal()
    }

    dependencies {

        implementation(libs.kotlin.stdlib.common)
        implementation(libs.kotlin.reflect)

        implementation(libs.log4j.core)
        implementation(libs.log4j.api)
        implementation(libs.log4j.slf4j2)
        implementation(libs.log4j.kotlin)

        testImplementation(libs.bundles.tests.base.dependencies.bundle)
    }

    jacoco {
        toolVersion = libs.versions.jacoco.get()
    }

    tasks.jacocoTestReport {
        reports {
            xml.required = true
            html.required = true
        }
    }

    tasks.test {
        useJUnitPlatform()
        finalizedBy("jacocoTestReport")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = libs.versions.jvm.get()
    }

}

fun ExternalModuleDependency.removeJackson() {
    libs.bundles.jackson.all.bundle.get().forEach {
        exclude(group = it.module.group, module = it.module.name)
    }
}
