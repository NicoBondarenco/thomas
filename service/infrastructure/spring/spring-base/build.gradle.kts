import org.springframework.boot.gradle.tasks.bundling.BootJar

val libs = rootProject.libs

@Suppress("DSL_SCOPE_VIOLATION") // workaround for IntelliJ bug with Gradle Version Catalogs DSL in plugins
plugins {
    alias(libs.plugins.kotlin.lang)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.framework.boot)
    alias(libs.plugins.spring.dependency.management)
}

configurations {
    all {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
    }
}

val jar: Jar by tasks
val bootJar: BootJar by tasks

bootJar.enabled = false
jar.enabled = true

dependencies {

    implementation(project(":core"))
    implementation(project(":infrastructure:authentication:authentication-base"))
    implementation(project(":infrastructure:authentication:authentication-jwt-auth0")) {
        exclude(group = "com.fasterxml.jackson.core", module = "jackson-annotations")
        exclude(group = "com.fasterxml.jackson.dataformat", module = "jackson-dataformat-yaml")
    }
    implementation(project(":infrastructure:database:mongo-base"))

    implementation(libs.spring.boot.starter.web) {
        exclude(group = "com.fasterxml.jackson.core", module = "jackson-annotations")
        exclude(group = "com.fasterxml.jackson.core", module = "jackson-core")
        exclude(group = "com.fasterxml.jackson.core", module = "jackson-databind")
        exclude(group = "com.fasterxml.jackson.dataformat", module = "jackson-dataformat-yaml")
        exclude(group = "com.fasterxml.jackson.datatype", module = "jackson-datatype-jdk8")
        exclude(group = "com.fasterxml.jackson.datatype", module = "jackson-datatype-jsr310")
        exclude(group = "com.fasterxml.jackson.module", module = "jackson-module-jaxb-annotations")
        exclude(group = "com.fasterxml.jackson.module", module = "jackson-module-kotlin")
        exclude(group = "com.fasterxml.jackson.module", module = "jackson-module-parameter-names")
    }
    implementation(libs.spring.boot.starter.security)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.spring.boot.starter.aop)
    implementation(libs.spring.boot.starter.actuator)

    implementation(libs.springdoc.openapi.starter.webmvc.ui){
        exclude(group = "com.fasterxml.jackson.core", module = "jackson-annotations")
        exclude(group = "com.fasterxml.jackson.core", module = "jackson-core")
        exclude(group = "com.fasterxml.jackson.core", module = "jackson-databind")
        exclude(group = "com.fasterxml.jackson.dataformat", module = "jackson-dataformat-yaml")
        exclude(group = "com.fasterxml.jackson.datatype", module = "jackson-datatype-jdk8")
        exclude(group = "com.fasterxml.jackson.datatype", module = "jackson-datatype-jsr310")
        exclude(group = "com.fasterxml.jackson.module", module = "jackson-module-jaxb-annotations")
        exclude(group = "com.fasterxml.jackson.module", module = "jackson-module-kotlin")
        exclude(group = "com.fasterxml.jackson.module", module = "jackson-module-parameter-names")
    }
    implementation(libs.springdoc.openapi.starter.common.ui)

    implementation(libs.bundles.jackson.all.bundle)

    implementation(libs.bundles.mongodb.sync.bundle)

    testImplementation(libs.spring.boot.test.starter.core)

    testImplementation(libs.auth0.jwt)

    testImplementation(libs.testcontainers.generic) {
        exclude(group = "com.fasterxml.jackson.core", module = "jackson-annotations")
    }

    testImplementation(libs.awaitility.base)
    testImplementation(libs.awaitility.kotlin)

    testImplementation(testFixtures(project(":core")))
}

