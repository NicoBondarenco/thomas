val libs = rootProject.libs

@Suppress("DSL_SCOPE_VIOLATION") // workaround for IntelliJ bug with Gradle Version Catalogs DSL in plugins
plugins {
    alias(libs.plugins.kotlin.lang)
}

dependencies {

    implementation(project(":core"))
    implementation(project(":infrastructure:hash:hash-base"))
    implementation(project(":infrastructure:message:management-message"))
    implementation(project(":module:authentication:authentication-data"))
    implementation(project(":module:authentication:authentication-tokenizer"))

}
