val libs = rootProject.libs

@Suppress("DSL_SCOPE_VIOLATION") // workaround for IntelliJ bug with Gradle Version Catalogs DSL in plugins
plugins {
    alias(libs.plugins.kotlin.lang)
}

dependencies {

    implementation(project(":core"))
    implementation(project(":module:management:management-data"))
    implementation(project(":infrastructure:database:exposed-base"))

    implementation(libs.exposed.core)
    implementation(libs.exposed.crypt)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.javatime)
    implementation(libs.exposed.json)

    testImplementation(libs.postgresql)
    testImplementation(libs.testcontainers.generic)

    testImplementation(libs.awaitility.base)
    testImplementation(libs.awaitility.kotlin)

    testImplementation(testFixtures(project(":core")))
}
