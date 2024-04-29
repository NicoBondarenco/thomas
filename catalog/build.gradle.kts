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

        version("mongodb", "5.0.1")

        version("auth0", "4.4.0")

        version("jackson", "2.17.0")

        version("awaitility", "4.2.1")

        version("testcontainers", "1.19.7")

        //endregion VERSIONS

        //region PLUGINS

        plugin("kotlin-lang", "org.jetbrains.kotlin.jvm").versionRef("kotlin")
        plugin("kotlinx-serialization", "org.jetbrains.kotlin.plugin.serialization").versionRef("kotlin")
        plugin("ksp-plugin", "com.google.devtools.ksp").versionRef("ksp")
        plugin("flyway-plugin", "org.flywaydb.flyway").versionRef("flywayPlugin")

        //endregion PLUGINS

        //region DEPENDENCIES

        //region DEFAULTS

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
        library("junit-jupiter-params", "org.junit.jupiter", "junit-jupiter-params").versionRef("junit")

        library("mockito-core", "org.mockito", "mockito-core").versionRef("mockito")
        library("mockito-inline", "org.mockito", "mockito-inline").versionRef("mockito-inline")
        library("mockito-junit", "org.mockito", "mockito-junit-jupiter").versionRef("mockito")
        library("mockito-kotlin", "org.mockito.kotlin", "mockito-kotlin").versionRef("mockitoKotlin")

        library("mockk", "io.mockk", "mockk").versionRef("mockk")

        library("system-stubs-core", "uk.org.webcompere", "system-stubs-core").versionRef("stubs")
        library("system-stubs-jupiter", "uk.org.webcompere", "system-stubs-jupiter").versionRef("stubs")

        //endregion DEFAULTS

        //region DATABASES

        library("mongodb-driver-kotlin", "org.mongodb", "mongodb-driver-kotlin-coroutine").versionRef("mongodb")
        library("mongodb-bson-kotlinx", "org.mongodb", "bson-kotlinx").versionRef("mongodb")
        library("mongodb-bson-kotlin", "org.mongodb", "bson-kotlin").versionRef("mongodb")

        //endregion DATABASES

        //region JWT

        library("auth0-jwt", "com.auth0", "java-jwt").versionRef("auth0")

        //endregion JWT

        //region SERIALIZATION

        library("jackson-core", "com.fasterxml.jackson.core", "jackson-core").versionRef("jackson")
        library("jackson-annotations", "com.fasterxml.jackson.core", "jackson-annotations").versionRef("jackson")
        library("jackson-databind", "com.fasterxml.jackson.core", "jackson-databind").versionRef("jackson")
        library("jackson-module-parameter", "com.fasterxml.jackson.module", "jackson-module-parameter-names").versionRef("jackson")
        library("jackson-module-kotlin", "com.fasterxml.jackson.module", "jackson-module-kotlin").versionRef("jackson")
        library("jackson-module-jaxb", "com.fasterxml.jackson.module", "jackson-module-jaxb-annotations").versionRef("jackson")
        library("jackson-datatype-jdk8", "com.fasterxml.jackson.datatype", "jackson-datatype-jdk8").versionRef("jackson")
        library("jackson-datatype-jsr310", "com.fasterxml.jackson.datatype", "jackson-datatype-jsr310").versionRef("jackson")
        library("jackson-dataformat-yaml", "com.fasterxml.jackson.dataformat", "jackson-dataformat-yaml").versionRef("jackson")
        library("jackson-dataformat-xml", "com.fasterxml.jackson.dataformat", "jackson-dataformat-xml").versionRef("jackson")

        //endregion SERIALIZATION

        //region TESTS

        library("awaitility-base", "org.awaitility", "awaitility").versionRef("awaitility")
        library("awaitility-kotlin", "org.awaitility", "awaitility-kotlin").versionRef("awaitility")

        library("testcontainers-generic", "org.testcontainers", "testcontainers").versionRef("testcontainers")

        //endregion TESTS

        //endregion DEPENDENCIES

    }
}