val libs = rootProject.libs

@Suppress("DSL_SCOPE_VIOLATION") // workaround for IntelliJ bug with Gradle Version Catalogs DSL in plugins
plugins {
    alias(libs.plugins.kotlin.lang)
    alias(libs.plugins.ksp.plugin)
    alias(libs.plugins.flyway.plugin)
}

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.flywaydb:flyway-database-postgresql:11.10.0")
    }
}


dependencies {

    implementation(project(":core"))
    implementation(project(":infrastructure:database:komapper"))

    platform("org.komapper:komapper-platform:5.4.0").let {
        implementation(it)
        ksp(it)
    }
    implementation("org.komapper:komapper-starter-r2dbc")
    ksp("org.komapper:komapper-processor")

    implementation(project(":module:locality:locality-data"))

    testImplementation(testFixtures(project(":core")))

    testImplementation("org.komapper:komapper-dialect-postgresql-r2dbc")
    testImplementation("org.postgresql:r2dbc-postgresql:1.0.7.RELEASE")
    testImplementation("io.r2dbc:r2dbc-pool:1.0.2.RELEASE")

    implementation("org.flywaydb:flyway-core:11.10.0")
    implementation("org.flywaydb:flyway-database-postgresql:11.10.0")
    implementation("org.postgresql:postgresql:42.7.7")

    testImplementation("org.testcontainers:testcontainers:1.21.2")

    testImplementation("org.awaitility:awaitility:4.3.0")
    testImplementation("org.awaitility:awaitility-kotlin:4.3.0")

}

flyway {
    url = System.getenv("LOCALITY_FL_URL")
    driver = System.getenv("LOCALITY_FL_DRIVER")
    user = System.getenv("LOCALITY_FL_USER")
    password = System.getenv("LOCALITY_FL_PASSWORD")
    locations = arrayOf("classpath:locality/migration")
    schemas = arrayOf(System.getenv("LOCALITY_FL_SCHEMAS"))
    baselineOnMigrate = System.getenv("LOCALITY_FL_BASELINE_ON_MIGRATE").toBoolean()
    validateOnMigrate = System.getenv("LOCALITY_FL_VALIDATE_ON_MIGRATE").toBoolean()
    cleanOnValidationError = System.getenv("LOCALITY_FL_CLEAN_ON_VALIDATION_ERROR").toBoolean()
    outOfOrder = System.getenv("LOCALITY_FL_OUT_OF_ORDER").toBoolean()
    defaultSchema = System.getenv("LOCALITY_FL_DEFAULT_SCHEMA")
    failOnMissingLocations = System.getenv("LOCALITY_FL_FAIL_ON_MISSING_LOCATIONS").toBoolean()
}
