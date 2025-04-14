import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

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

    implementation(project(":infrastructure:spring:spring-base"))

    implementation(project(":module:management:management-data"))
    implementation(project(":module:management:management-data-neo4j"))
    implementation(project(":module:management:management-domain"))

    implementation(project(":infrastructure:contract:contract-messaging"))

    implementation(libs.spring.boot.starter.web) { removeJackson() }
    implementation(libs.spring.boot.starter.webflux) { removeJackson() }
    implementation(libs.spring.boot.starter.security)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.spring.boot.starter.aop)
    implementation(libs.spring.boot.starter.actuator)

    implementation(libs.springdoc.openapi.starter.webmvc.ui) { removeJackson() }
    implementation(libs.springdoc.openapi.starter.common.ui)

    implementation(libs.spring.cloud.stream.core.all)

    implementation(libs.bundles.spring.cloud.stream.rabbit.bundle)

    implementation(libs.neo4j.ogm.core)
    implementation(libs.neo4j.ogm.bolt)

    implementation(libs.auth0.jwt) { removeJackson() }

    implementation("org.bouncycastle:bcpkix-jdk18on:1.80")

    implementation(libs.bundles.jackson.all.bundle)

    testImplementation(testFixtures(project(":core")))

}

fun ExternalModuleDependency.removeJackson() {
    libs.bundles.jackson.all.bundle.get().forEach {
        exclude(group = it.module.group, module = it.module.name)
    }
}

tasks.withType<KotlinJvmCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
        freeCompilerArgs.add("-Xjsr305=strict")
    }
}
