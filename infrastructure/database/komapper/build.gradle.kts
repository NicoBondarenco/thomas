val libs = rootProject.libs

@Suppress("DSL_SCOPE_VIOLATION") // workaround for IntelliJ bug with Gradle Version Catalogs DSL in plugins
plugins {
    alias(libs.plugins.kotlin.lang)
    alias(libs.plugins.ksp.plugin)
}

dependencies {

    implementation(project(":core"))

    platform("org.komapper:komapper-platform:5.4.0").let {
        implementation(it)
        ksp(it)
    }
    implementation("org.komapper:komapper-starter-r2dbc")
    ksp("org.komapper:komapper-processor")

    testImplementation(testFixtures(project(":core")))
    testImplementation("org.komapper:komapper-dialect-h2-r2dbc")

}
