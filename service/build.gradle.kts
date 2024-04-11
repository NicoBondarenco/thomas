import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Suppress("DSL_SCOPE_VIOLATION") // workaround for IntelliJ bug with Gradle Version Catalogs DSL in plugins
plugins {
    alias(libs.plugins.kotlin.lang)
    alias(libs.plugins.flyway.plugin)
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

        testImplementation(libs.mockk)

        testImplementation(libs.system.stubs.core)
        testImplementation(libs.system.stubs.jupiter)

        testImplementation(libs.mockito.core)
        testImplementation(libs.mockito.inline)
        testImplementation(libs.mockito.junit)
        testImplementation(libs.mockito.kotlin)

    }

    tasks.test {
        useJUnitPlatform()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = libs.versions.jvm.get()
    }

}