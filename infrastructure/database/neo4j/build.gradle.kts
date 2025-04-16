val libs = rootProject.libs

@Suppress("DSL_SCOPE_VIOLATION") // workaround for IntelliJ bug with Gradle Version Catalogs DSL in plugins
plugins {
    alias(libs.plugins.kotlin.lang)
    id("java-test-fixtures")
    kotlin("plugin.noarg") version "2.1.10"
}

noArg {
    annotation("com.thomas.database.neo4j.node.NoArgsConstructor")
}

dependencies {

    implementation(project(":core"))

    implementation("org.neo4j:neo4j-ogm-core:4.0.15")
    implementation("org.neo4j:neo4j-ogm-bolt-driver:4.0.15")

    testImplementation(kotlin("test"))
    testImplementation(testFixtures(project(":core")))

    testFixturesImplementation(testFixtures(project(":core")))

    testFixturesImplementation("com.thomas:thomas-neo4j-plugin:1.0.0")
    testFixturesImplementation(libs.bundles.test.standard.bundle)
    testFixturesImplementation(libs.bundles.neo4j.test.bundle)

    testFixturesImplementation("org.neo4j:neo4j-ogm-core:4.0.15")
    testFixturesImplementation("org.neo4j:neo4j-ogm-bolt-driver:4.0.15")
    testFixturesImplementation("io.kotest:kotest-assertions-core:5.9.1")
    testFixturesImplementation("io.kotest:kotest-runner-junit5:5.9.1")
    testFixturesImplementation("io.kotest:kotest-property:5.9.1")
    testFixturesImplementation("io.kotest:kotest-framework-datatest:5.9.1")
}
