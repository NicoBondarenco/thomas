plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "service"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        mavenLocal()
    }
    versionCatalogs {
        create("libs") {
            from("com.thomas:catalog:1.0.0.final")
        }
    }
}

include("core")

include("infrastructure:authentication:authentication-base")
findProject(":infrastructure:authentication:authentication-base")?.name = "authentication-base"

include("infrastructure:authentication:authentication-jwt-auth0")
findProject(":infrastructure:authentication:authentication-jwt-auth0")?.name = "authentication-jwt-auth0"

include("infrastructure:database:mongo-base")
findProject(":infrastructure:database:mongo-base")?.name = "mongo-base"

include("infrastructure:database:exposed-base")
findProject(":infrastructure:database:exposed-base")?.name = "exposed-base"

include("infrastructure:spring:spring-base")
findProject(":infrastructure:spring:spring-base")?.name = "spring-base"

include("infrastructure:storage:storage-base")
findProject(":infrastructure:storage:storage-base")?.name = "storage-base"

include("infrastructure:storage:storage-local")
findProject(":infrastructure:storage:storage-local")?.name = "storage-local"
