val libs = rootProject.libs

@Suppress("DSL_SCOPE_VIOLATION") // workaround for IntelliJ bug with Gradle Version Catalogs DSL in plugins
plugins {
    alias(libs.plugins.kotlin.lang)
}

dependencies {

    implementation(project(":core"))
    implementation(project(":module:authentication:authentication-data"))
    implementation(project(":module:authentication:authentication-tokenizer"))

    implementation(libs.auth0.jwt)

    testImplementation(testFixtures(project(":core")))
    testImplementation(libs.bundles.jackson.all.bundle)
}
