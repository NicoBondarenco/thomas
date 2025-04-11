val libs = rootProject.libs

@Suppress("DSL_SCOPE_VIOLATION") // workaround for IntelliJ bug with Gradle Version Catalogs DSL in plugins
plugins {
    alias(libs.plugins.kotlin.lang)
    kotlin("plugin.noarg") version "2.1.10"
}

noArg {
    annotation("com.thomas.database.neo4j.node.NoArgsConstructor")
}

dependencies {

    implementation(project(":core"))
    implementation(project(":module:management:management-data"))
    implementation(project(":infrastructure:database:neo4j"))

    implementation(libs.neo4j.ogm.core)
    implementation(libs.neo4j.ogm.bolt)

    testImplementation(libs.bundles.jackson.all.bundle)

    testImplementation(testFixtures(project(":core")))
    testImplementation(testFixtures(project(":module:management:management-data")))
    testImplementation(testFixtures(project(":infrastructure:database:neo4j")))

}
