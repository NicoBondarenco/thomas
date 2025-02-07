val libs = rootProject.libs

@Suppress("DSL_SCOPE_VIOLATION") // workaround for IntelliJ bug with Gradle Version Catalogs DSL in plugins
plugins {
    alias(libs.plugins.kotlin.lang)
}

dependencies {

    implementation(project(":core"))

    implementation(project(":infrastructure:contract:contract-messaging"))
    implementation(project(":infrastructure:hasher:hasher"))

    implementation(project(":module:management:management-data"))

    testImplementation(testFixtures(project(":core")))
    testImplementation(testFixtures(project(":module:management:management-data")))

}
