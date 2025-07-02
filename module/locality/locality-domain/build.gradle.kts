val libs = rootProject.libs

@Suppress("DSL_SCOPE_VIOLATION") // workaround for IntelliJ bug with Gradle Version Catalogs DSL in plugins
plugins {
    alias(libs.plugins.kotlin.lang)
    id("java-test-fixtures")
}

dependencies {

    implementation(project(":core"))
    implementation(project(":infrastructure:cache:cache-handler"))

    implementation(project(":module:locality:locality-data"))
    implementation(project(":module:locality:locality-port-address"))

    testImplementation(testFixtures(project(":core")))

    testFixturesImplementation(testFixtures(project(":core")))

}
