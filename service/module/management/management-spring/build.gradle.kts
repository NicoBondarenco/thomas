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

dependencyManagement {
    imports {
        mavenBom(libs.spring.cloud.dependencies.get().toString())
    }
}

dependencies {

    implementation(project(":core"))

    implementation(project(":module:management:management-data"))
    implementation(project(":module:management:management-data-exposed"))
    implementation(project(":module:management:management-domain"))

    implementation(project(":infrastructure:database:exposed-base"))
    implementation(project(":infrastructure:spring:spring-base"))
    implementation(project(":infrastructure:message:management-message"))

    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.cloud.stream.core.all)
    implementation(libs.bundles.spring.cloud.stream.rabbit.bundle)

    implementation(libs.exposed.core)

    implementation(libs.postgresql)
    implementation(libs.bundles.mongodb.sync.bundle)

    testImplementation(libs.spring.boot.test.starter.core)
    testImplementation(libs.spring.boot.test.container.testcontainers)

}