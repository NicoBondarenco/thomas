val libs = rootProject.libs

@Suppress("DSL_SCOPE_VIOLATION") // workaround for IntelliJ bug with Gradle Version Catalogs DSL in plugins
plugins {
    alias(libs.plugins.kotlin.lang)
}

dependencies {

    implementation(project(":core"))

    implementation(libs.mongodb.driver.sync)

    testImplementation("org.testcontainers:mongodb:1.19.7")
    testImplementation("org.testcontainers:testcontainers:1.19.7")
    testImplementation("org.awaitility:awaitility:4.2.1")
    testImplementation("org.awaitility:awaitility-kotlin:4.2.1")

}