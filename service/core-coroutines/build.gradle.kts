val libs = rootProject.libs

@Suppress("DSL_SCOPE_VIOLATION") // workaround for IntelliJ bug with Gradle Version Catalogs DSL in plugins
plugins {
    alias(libs.plugins.kotlin.lang)
}

dependencies {
    implementation(project(":core"))

    implementation(libs.bundles.kotlinx.coroutines.bundle)

    testImplementation(testFixtures(project(":core")))

    testImplementation(libs.kotlinx.coroutines.test)
}
