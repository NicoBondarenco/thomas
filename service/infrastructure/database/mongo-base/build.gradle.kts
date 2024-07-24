val libs = rootProject.libs

@Suppress("DSL_SCOPE_VIOLATION") // workaround for IntelliJ bug with Gradle Version Catalogs DSL in plugins
plugins {
    alias(libs.plugins.kotlin.lang)
}

dependencies {

    implementation(project(":core"))

    implementation(libs.bundles.mongodb.sync.bundle)

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
