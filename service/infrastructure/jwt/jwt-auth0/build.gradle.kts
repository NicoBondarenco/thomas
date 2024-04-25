val libs = rootProject.libs

@Suppress("DSL_SCOPE_VIOLATION") // workaround for IntelliJ bug with Gradle Version Catalogs DSL in plugins
plugins {
    alias(libs.plugins.kotlin.lang)
}

dependencies {

    implementation(project(":core"))
    implementation(project(":infrastructure:jwt:jwt-base"))
    implementation(project(":infrastructure:database:mongo-base"))

    implementation(libs.auth0.jwt)

    implementation(libs.mongodb.driver.kotlin)

}