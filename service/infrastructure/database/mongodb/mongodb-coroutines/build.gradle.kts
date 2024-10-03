val libs = rootProject.libs

@Suppress("DSL_SCOPE_VIOLATION") // workaround for IntelliJ bug with Gradle Version Catalogs DSL in plugins
plugins {
    alias(libs.plugins.kotlin.lang)
}

dependencies {

    implementation(project(":core"))
    implementation(project(":core-coroutines"))
    implementation(project(":infrastructure:database:mongodb:mongodb-base"))

    implementation(libs.bundles.kotlinx.coroutines.bundle)

    implementation("org.mongodb:mongodb-driver-kotlin-coroutine:5.1.3")
    implementation("org.mongodb:bson-kotlinx:5.1.3")

    testImplementation(libs.kotlinx.coroutines.test)

    testImplementation(libs.testcontainers.generic) { removeJackson() }

    testImplementation(libs.bundles.jackson.all.bundle)

    testImplementation(libs.awaitility.base)
    testImplementation(libs.awaitility.kotlin)

}

fun ExternalModuleDependency.removeJackson() {
    libs.bundles.jackson.all.bundle.get().forEach {
        exclude(group = it.module.group, module = it.module.name)
    }
}
