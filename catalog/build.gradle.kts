plugins {
    `version-catalog`
    `maven-publish`
}

group = "com.thomas"
version = "1.0.0.final"

repositories {
    mavenCentral()
    mavenLocal()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["versionCatalog"])
        }
    }
}

catalog {
    versionCatalog {
        //region VERSIONS

        version("kotlin", "1.9.23")
        version("kotlinx-coroutines", "1.8.0")
        version("jvm", "21")
        version("target", "VERSION_21")

        version("ksp", "1.9.23-1.0.19")

        version("flywayPlugin", "10.11.0")

        version("log4j", "2.23.1")
        version("log4jKotlin", "1.4.0")

        version("junit", "5.10.2")
        version("junitPioneer", "2.2.0")

        version("mockito", "5.11.0")
        version("mockito-inline", "5.2.0")
        version("mockitoKotlin", "5.2.1")

        version("mockk", "1.13.10")

        version("stubs", "2.1.6")

        //endregion VERSIONS

        //region PLUGINS

        plugin("kotlin-lang", "org.jetbrains.kotlin.jvm").versionRef("kotlin")
        plugin("kotlinx-serialization", "org.jetbrains.kotlin.plugin.serialization").versionRef("kotlin")
        plugin("ksp-plugin", "com.google.devtools.ksp").versionRef("ksp")
        plugin("flyway-plugin", "org.flywaydb.flyway").versionRef("flywayPlugin")

        //endregion PLUGINS

        //region DEPENDENCIES

        library("kotlin-stdlib-jdk8", "org.jetbrains.kotlin", "kotlin-stdlib-jdk8").versionRef("kotlin")
        library("kotlin-stdlib-common", "org.jetbrains.kotlin", "kotlin-stdlib").versionRef("kotlin")
        library("kotlin-reflect", "org.jetbrains.kotlin", "kotlin-reflect").versionRef("kotlin")

        library("kotlinx-coroutines-core", "org.jetbrains.kotlinx", "kotlinx-coroutines-core").versionRef("kotlinx-coroutines")
        library("kotlinx-coroutines-reactive", "org.jetbrains.kotlinx", "kotlinx-coroutines-reactive").versionRef("kotlinx-coroutines")

        library("log4j-api", "org.apache.logging.log4j", "log4j-api").versionRef("log4j")
        library("log4j-core", "org.apache.logging.log4j", "log4j-core").versionRef("log4j")
        library("log4j-slf4j2", "org.apache.logging.log4j", "log4j-slf4j2-impl").versionRef("log4j")
        library("log4j-kotlin", "org.apache.logging.log4j", "log4j-api-kotlin").versionRef("log4jKotlin")

        library("junit-pioneer", "org.junit-pioneer", "junit-pioneer").versionRef("junitPioneer")
        library("junit-jupiter-api", "org.junit.jupiter", "junit-jupiter-api").versionRef("junit")
        library("junit-jupiter-engine", "org.junit.jupiter", "junit-jupiter-engine").versionRef("junit")

        library("mockito-core", "org.mockito", "mockito-core").versionRef("mockito")
        library("mockito-inline", "org.mockito", "mockito-inline").versionRef("mockito-inline")
        library("mockito-junit", "org.mockito", "mockito-junit-jupiter").versionRef("mockito")
        library("mockito-kotlin", "org.mockito.kotlin", "mockito-kotlin").versionRef("mockitoKotlin")

        library("mockk", "io.mockk", "mockk").versionRef("mockk")

        library("system-stubs-core", "uk.org.webcompere", "system-stubs-core").versionRef("stubs")
        library("system-stubs-jupiter", "uk.org.webcompere", "system-stubs-jupiter").versionRef("stubs")

        //endregion DEPENDENCIES

    }
}