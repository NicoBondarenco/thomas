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

        //region PLUGINS

        version("kotlin", "1.9.23")
        version("kotlinx-coroutines", "1.8.0")
        version("jvm", "21")
        version("target", "VERSION_21")

        version("ksp", "1.9.23-1.0.19")

        version("flywayPlugin", "10.11.0")

        version("springFrameworkBoot", "3.2.5")
        version("springDependencyManagement", "1.1.4")

        version("detekt", "1.23.3")

        version("jacoco", "0.8.12")

        version("sonarqube", "5.1.0.4882")

        //endregion PLUGINS

        //region DEPENDENCIES

        version("log4j", "2.23.1")
        version("log4jKotlin", "1.4.0")

        version("junit", "5.10.2")
        version("junitPioneer", "2.2.0")

        version("mockito", "5.11.0")
        version("mockito-inline", "5.2.0")
        version("mockitoKotlin", "5.2.1")

        version("mockk", "1.13.10")

        version("stubs", "2.1.6")

        version("jsonassert", "1.5.1")

        version("awaitility", "4.2.1")

        version("testcontainers", "1.19.7")

        version("reflections", "0.10.2")

        version("postgresql", "42.7.3")

        version("h2", "2.2.224")

        version("hikari", "5.1.0")

        version("mongodb", "5.1.2")

        version("exposed", "0.49.0")

        version("auth0", "4.4.0")

        version("jackson", "2.17.0")

        version("springCloud", "2023.0.1")

        version("detektFormat", "1.23.3")

        //endregion DEPENDENCIES

        //endregion VERSIONS

        //region PLUGINS

        plugin("kotlin-lang", "org.jetbrains.kotlin.jvm").versionRef("kotlin")
        plugin("kotlin-spring", "org.jetbrains.kotlin.plugin.spring").versionRef("kotlin")

        plugin("kotlinx-serialization", "org.jetbrains.kotlin.plugin.serialization").versionRef("kotlin")

        plugin("ksp-plugin", "com.google.devtools.ksp").versionRef("ksp")

        plugin("flyway-plugin", "org.flywaydb.flyway").versionRef("flywayPlugin")

        plugin("spring-framework-boot", "org.springframework.boot").versionRef("springFrameworkBoot")
        plugin("spring-dependency-management", "io.spring.dependency-management").versionRef("springDependencyManagement")

        plugin("detekt-plugin", "io.gitlab.arturbosch.detekt").versionRef("detekt")

        plugin("jacoco-plugin", "jacoco").versionRef("jacoco")

        plugin("sonarqube-plugin", "org.sonarqube").versionRef("sonarqube")

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

        library("skyscreamer-jsonassert", "org.skyscreamer", "jsonassert").versionRef("jsonassert")

        //endregion DEFAULTS

        //region UTILS

        library("reflections", "org.reflections", "reflections").versionRef("reflections")

        //endregion UTILS

        //region DATABASES

        library("mongodb-driver-core", "org.mongodb", "mongodb-driver-core").versionRef("mongodb")
        library("mongodb-driver-sync", "org.mongodb", "mongodb-driver-sync").versionRef("mongodb")
        library("mongodb-bson-core", "org.mongodb", "bson").versionRef("mongodb")
        library("mongodb-bson-kotlin", "org.mongodb", "bson-kotlin").versionRef("mongodb")

        library("exposed-core", "org.jetbrains.exposed", "exposed-core").versionRef("exposed")
        library("exposed-crypt", "org.jetbrains.exposed", "exposed-crypt").versionRef("exposed")
        library("exposed-dao", "org.jetbrains.exposed", "exposed-dao").versionRef("exposed")
        library("exposed-jdbc", "org.jetbrains.exposed", "exposed-jdbc").versionRef("exposed")
        library("exposed-javatime", "org.jetbrains.exposed", "exposed-java-time").versionRef("exposed")
        library("exposed-json", "org.jetbrains.exposed", "exposed-json").versionRef("exposed")

        library("postgresql", "org.postgresql", "postgresql").versionRef("postgresql")

        library("h2", "com.h2database", "h2").versionRef("h2")

        library("hikaricp", "com.zaxxer", "HikariCP").versionRef("hikari")

        bundle(
            "mongodb-sync-bundle",
            listOf(
                "mongodb-driver-core",
                "mongodb-driver-sync",
                "mongodb-bson-core",
                "mongodb-bson-kotlin",
            ),
        )

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

        //region SPRING

        library("spring-boot-starter-web", "org.springframework.boot", "spring-boot-starter-web").withoutVersion()
        library("spring-boot-starter-security", "org.springframework.boot", "spring-boot-starter-security").withoutVersion()
        library("spring-boot-starter-validation", "org.springframework.boot", "spring-boot-starter-validation").withoutVersion()

        library("spring-cloud-dependencies", "org.springframework.cloud", "spring-cloud-dependencies").versionRef("springCloud")

        library("spring-cloud-stream-core-all", "org.springframework.cloud", "spring-cloud-stream").withoutVersion()

        library("spring-cloud-stream-binder-rabbit", "org.springframework.cloud", "spring-cloud-stream-binder-rabbit").withoutVersion()
        library("spring-cloud-stream-starter-rabbit", "org.springframework.cloud", "spring-cloud-starter-stream-rabbit").withoutVersion()

        library("spring-boot-test-starter-core", "org.springframework.boot", "spring-boot-starter-test").withoutVersion()
        library("spring-boot-test-container-testcontainers", "org.springframework.boot", "spring-boot-testcontainers").withoutVersion()
        library("spring-cloud-test-stream-binder", "org.springframework.cloud", "spring-cloud-stream-test-binder").withoutVersion()

        bundle(
            "spring-cloud-stream-rabbit-bundle",
            listOf(
                "spring-cloud-stream-binder-rabbit",
                "spring-cloud-stream-starter-rabbit",
            ),
        )

        //endregion SPRING

        //region DETEKT

        library("detekt-formatting", "io.gitlab.arturbosch.detekt", "detekt-formatting").versionRef("detektFormat")

        //endregion DETEKT

        //region TESTS

        library("awaitility-base", "org.awaitility", "awaitility").versionRef("awaitility")
        library("awaitility-kotlin", "org.awaitility", "awaitility-kotlin").versionRef("awaitility")

        library("testcontainers-generic", "org.testcontainers", "testcontainers").versionRef("testcontainers")
        library("testcontainers-junit", "org.testcontainers", "testcontainers").versionRef("testcontainers")

        //endregion TESTS

        //endregion DEPENDENCIES

    }
}