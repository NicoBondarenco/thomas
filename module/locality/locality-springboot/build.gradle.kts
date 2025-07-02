import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

val libs = rootProject.libs

@Suppress("DSL_SCOPE_VIOLATION") // workaround for IntelliJ bug with Gradle Version Catalogs DSL in plugins
plugins {
    alias(libs.plugins.kotlin.lang)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.framework.boot)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.ksp.plugin)
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

    implementation(project(":infrastructure:cache:cache-handler"))
    implementation(project(":infrastructure:cache:cache-handler-caffeine"))

    implementation(project(":module:locality:locality-data"))
    implementation(project(":module:locality:locality-data-komapper"))
    implementation(project(":module:locality:locality-domain"))
    implementation(project(":module:locality:locality-port-address"))
    implementation(project(":module:locality:locality-port-address-viacep"))

    implementation(project(":infrastructure:contract:contract-messaging"))

    implementation(libs.spring.boot.starter.web) { removeJackson() }
    implementation(libs.spring.boot.starter.webflux) { removeJackson() }
    implementation(libs.spring.boot.starter.security)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.spring.boot.starter.aop)
    implementation(libs.spring.boot.starter.actuator)

    implementation(libs.springdoc.openapi.starter.webmvc.ui) { removeJackson() }
    implementation(libs.springdoc.openapi.starter.common.ui)

    platform("org.komapper:komapper-platform:5.4.0").let {
        implementation(it)
        ksp(it)
    }
    implementation("org.komapper:komapper-starter-r2dbc")
    ksp("org.komapper:komapper-processor")
    implementation(project(":infrastructure:database:komapper"))

    implementation("org.komapper:komapper-dialect-postgresql-r2dbc")
    implementation("org.postgresql:r2dbc-postgresql:1.0.7.RELEASE")
    implementation("io.r2dbc:r2dbc-pool:1.0.2.RELEASE")

    implementation("io.micrometer:micrometer-registry-prometheus:1.15.0")

    implementation(libs.auth0.jwt) { removeJackson() }

    implementation("com.github.loki4j:loki-logback-appender:1.6.0")

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
