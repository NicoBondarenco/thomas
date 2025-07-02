val libs = rootProject.libs

@Suppress("DSL_SCOPE_VIOLATION") // workaround for IntelliJ bug with Gradle Version Catalogs DSL in plugins
plugins {
    alias(libs.plugins.kotlin.lang)
}

dependencies {

    implementation(project(":core"))
    implementation(project(":module:locality:locality-port-address"))

    implementation(libs.bundles.jackson.all.bundle)
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    testImplementation(testFixtures(project(":core")))
    testImplementation("org.wiremock:wiremock:3.13.1")

}
