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
    implementation(project(":infrastructure:authentication:authentication-jwt-auth0"))
    implementation(project(":infrastructure:database:mongodb:mongodb-base"))
    implementation(project(":infrastructure:database:mongodb:mongodb-sync"))

    implementation(libs.spring.boot.starter.web) { removeJackson() }
    implementation(libs.spring.boot.starter.security)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.spring.boot.starter.aop)
    implementation(libs.spring.boot.starter.actuator)

    implementation(libs.springdoc.openapi.starter.webmvc.ui) { removeJackson() }
    implementation(libs.springdoc.openapi.starter.common.ui)

    implementation(libs.bundles.jackson.all.bundle)

    implementation(libs.bundles.mongodb.sync.bundle)

    testImplementation(libs.spring.boot.test.starter.core)

    testImplementation(libs.auth0.jwt) { removeJackson() }

    testImplementation(libs.testcontainers.generic) { removeJackson() }

    testImplementation(libs.awaitility.base)
    testImplementation(libs.awaitility.kotlin)

    testImplementation(testFixtures(project(":core")))
}

fun ExternalModuleDependency.removeJackson() {
    libs.bundles.jackson.all.bundle.get().forEach {
        exclude(group = it.module.group, module = it.module.name)
    }
}
