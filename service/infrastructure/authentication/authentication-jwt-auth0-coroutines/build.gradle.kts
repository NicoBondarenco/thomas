val libs = rootProject.libs

@Suppress("DSL_SCOPE_VIOLATION") // workaround for IntelliJ bug with Gradle Version Catalogs DSL in plugins
plugins {
    alias(libs.plugins.kotlin.lang)
}

dependencies {

    implementation(project(":core"))
    implementation(project(":infrastructure:authentication:authentication-coroutines"))
    implementation(project(":infrastructure:database:mongodb:mongodb-base"))
    implementation(project(":infrastructure:database:mongodb:mongodb-sync"))

    implementation(libs.auth0.jwt) { removeJackson() }

    implementation(libs.bundles.jackson.all.bundle)

    implementation(libs.mongodb.driver.sync)

    testImplementation(libs.testcontainers.generic) { removeJackson() }

    testImplementation(libs.awaitility.base)
    testImplementation(libs.awaitility.kotlin)

}

fun ExternalModuleDependency.removeJackson() {
    libs.bundles.jackson.all.bundle.get().forEach {
        exclude(group = it.module.group, module = it.module.name)
    }
}
