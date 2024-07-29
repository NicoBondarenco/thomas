val libs = rootProject.libs

@Suppress("DSL_SCOPE_VIOLATION") // workaround for IntelliJ bug with Gradle Version Catalogs DSL in plugins
plugins {
    alias(libs.plugins.kotlin.lang)
}

dependencies {

    implementation(project(":core"))
    implementation(project(":infrastructure:hash:hash-base"))
    implementation("org.bouncycastle:bcpkix-jdk18on:1.78.1")


}
