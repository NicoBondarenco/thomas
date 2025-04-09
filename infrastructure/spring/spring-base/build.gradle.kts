val libs = rootProject.libs

@Suppress("DSL_SCOPE_VIOLATION") // workaround for IntelliJ bug with Gradle Version Catalogs DSL in plugins
plugins {
    alias(libs.plugins.kotlin.lang)
}

configurations {
    all {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
    }
}

dependencies {

    implementation(project(":core"))

    implementation(libs.spring.boot.starter.versioned.web) { removeJackson() }
    implementation(libs.spring.boot.starter.versioned.webflux) { removeJackson() }
    implementation(libs.spring.boot.starter.versioned.security)
    implementation(libs.spring.boot.starter.versioned.validation)
    implementation(libs.spring.boot.starter.versioned.aop)
    implementation(libs.spring.boot.starter.versioned.actuator)

    implementation(libs.springdoc.openapi.starter.webmvc.ui) { removeJackson() }
    implementation(libs.springdoc.openapi.starter.common.ui)

    implementation(libs.bundles.jackson.all.bundle)

    implementation(libs.auth0.jwt) { removeJackson() }

    testImplementation(testFixtures(project(":core")))
    testImplementation(libs.spring.boot.test.starter.versioned.core)
}

fun ExternalModuleDependency.removeJackson() {
    libs.bundles.jackson.all.bundle.get().forEach {
        exclude(group = it.module.group, module = it.module.name)
    }
}
