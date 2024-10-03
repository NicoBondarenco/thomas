val libs = rootProject.libs

@Suppress("DSL_SCOPE_VIOLATION") // workaround for IntelliJ bug with Gradle Version Catalogs DSL in plugins
plugins {
    alias(libs.plugins.kotlin.lang)
}

dependencies {

    implementation(project(":core"))
    implementation(project(":core-coroutines"))

    implementation(project(":infrastructure:hash:hash-base"))

    implementation(project(":module:auma:auma-data"))

    implementation(project(":infrastructure:message:management-message"))

    implementation(libs.bundles.kotlinx.coroutines.bundle)

    testImplementation(testFixtures(project(":core")))
}