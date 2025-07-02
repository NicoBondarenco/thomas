val libs = rootProject.libs

@Suppress("DSL_SCOPE_VIOLATION") // workaround for IntelliJ bug with Gradle Version Catalogs DSL in plugins
plugins {
    alias(libs.plugins.kotlin.lang)
}

dependencies {

    implementation(project(":core"))

    implementation(project(":infrastructure:cache:cache-handler"))

    implementation("com.github.ben-manes.caffeine:caffeine:3.2.0")
    implementation("dev.hsbrysk:caffeine-coroutines:1.1.0")

    testImplementation(testFixtures(project(":core")))

}
