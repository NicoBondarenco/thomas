val libs = rootProject.libs

@Suppress("DSL_SCOPE_VIOLATION") // workaround for IntelliJ bug with Gradle Version Catalogs DSL in plugins
plugins {
    alias(libs.plugins.kotlin.lang)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.framework.boot)
    alias(libs.plugins.spring.dependency.management)
}

configurations {
    all {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
    }
}

dependencies {

    implementation(project(":core"))
    implementation(project(":infrastructure:authentication:authentication-base"))
    implementation(project(":infrastructure:authentication:authentication-jwt-auth0"))
    implementation(project(":infrastructure:database:mongo-base"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("jakarta.validation:jakarta.validation-api:3.0.2")

    implementation(libs.mongodb.driver.kotlin)

    implementation(libs.jackson.core)
    implementation(libs.jackson.annotations)
    implementation(libs.jackson.databind)
    implementation(libs.jackson.module.parameter)
    implementation(libs.jackson.module.kotlin)
    implementation(libs.jackson.module.jaxb)
    implementation(libs.jackson.datatype.jdk8)
    implementation(libs.jackson.datatype.jsr310)

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")

    testImplementation(libs.auth0.jwt)

    testImplementation(libs.testcontainers.generic)

    testImplementation(libs.awaitility.base)
    testImplementation(libs.awaitility.kotlin)
}
