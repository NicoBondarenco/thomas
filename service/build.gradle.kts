import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Suppress("DSL_SCOPE_VIOLATION") // workaround for IntelliJ bug with Gradle Version Catalogs DSL in plugins
plugins {
    alias(libs.plugins.kotlin.lang)
    alias(libs.plugins.flyway.plugin)
    id("io.gitlab.arturbosch.detekt") version ("1.23.3")
}

detekt {
    toolVersion = "1.23.3"
    config.setFrom(file("config/detekt/detekt.yml"))
    buildUponDefaultConfig = true
}

dependencies {
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.3")
}

val projectSource = file(projectDir)
val configFile = files("$rootDir/config/detekt/detekt.yml")
val kotlinFiles = "**/*.kt"
val resourceFiles = "**/resources/**"
val buildFiles = "**/build/**"

val detektAll by tasks.registering(Detekt::class) {
    description = "Custom DETEKT build for all modules"
    parallel = true
    ignoreFailures = false
    autoCorrect = true
    buildUponDefaultConfig = true
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
    description = "Custom DETEKT build to build baseline for all modules"
    parallel = true
    ignoreFailures = false
    buildUponDefaultConfig = true
    setSource(projectSource)
    config.setFrom(configFile)
    include(kotlinFiles)
    exclude(resourceFiles, buildFiles)
}

allprojects {

    val libs = rootProject.libs

    apply {
        plugin(libs.plugins.kotlin.lang.get().pluginId)
    }

    group = "com.thomas"
    version = "1.0.0.final"

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

        testImplementation(libs.junit.pioneer)
        testImplementation(libs.junit.jupiter.api)
        testImplementation(libs.junit.jupiter.engine)
        testImplementation(libs.junit.jupiter.params)

        testImplementation(libs.mockk)

        testImplementation(libs.system.stubs.core)
        testImplementation(libs.system.stubs.jupiter)

        testImplementation(libs.mockito.core)
        testImplementation(libs.mockito.inline)
        testImplementation(libs.mockito.junit)
        testImplementation(libs.mockito.kotlin)

        testImplementation(libs.skyscreamer.jsonassert)

    }

    tasks.test {
        useJUnitPlatform()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = libs.versions.jvm.get()
    }

}