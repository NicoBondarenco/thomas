val libs = rootProject.libs

@Suppress("DSL_SCOPE_VIOLATION") // workaround for IntelliJ bug with Gradle Version Catalogs DSL in plugins
plugins {
    alias(libs.plugins.kotlin.lang)
}

dependencies {

    implementation(project(":core"))

    implementation(libs.reflections)

    implementation(libs.hikaricp)

    implementation(libs.exposed.core)
    implementation(libs.exposed.crypt)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.javatime)
    implementation(libs.exposed.json)

    implementation(libs.jackson.core)
    implementation(libs.jackson.annotations)
    implementation(libs.jackson.databind)
    implementation(libs.jackson.module.parameter)
    implementation(libs.jackson.module.kotlin)
    implementation(libs.jackson.module.jaxb)
    implementation(libs.jackson.datatype.jdk8)
    implementation(libs.jackson.datatype.jsr310)

    testImplementation(libs.h2)
    testImplementation(libs.postgresql)

    testImplementation(libs.awaitility.base)
    testImplementation(libs.awaitility.kotlin)

    testImplementation(libs.testcontainers.generic)

}
