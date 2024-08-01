rootProject.name = "service"

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

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

include("infrastructure:database:exposed-base")
findProject(":infrastructure:database:exposed-base")?.name = "exposed-base"

include("infrastructure:database:mongo-base")
findProject(":infrastructure:database:mongo-base")?.name = "mongo-base"

include("infrastructure:hash:hash-base")
findProject(":infrastructure:hash:hash-base")?.name = "hash-base"

include("infrastructure:hash:hash-bouncy-castle")
findProject(":infrastructure:hash:hash-bouncy-castle")?.name = "hash-bouncy-castle"

include("infrastructure:message:management-message")
findProject(":infrastructure:message:management-message")?.name = "management-message"

include("infrastructure:message:notification-message")
findProject(":infrastructure:message:notification-message")?.name = "notification-message"

include("infrastructure:spring:spring-base")
findProject(":infrastructure:spring:spring-base")?.name = "spring-base"

include("infrastructure:storage:storage-base")
findProject(":infrastructure:storage:storage-base")?.name = "storage-base"

include("infrastructure:storage:storage-local")
findProject(":infrastructure:storage:storage-local")?.name = "storage-local"

include("module:management:management-data")
findProject(":module:management:management-data")?.name = "management-data"

include("module:management:management-data-exposed")
findProject(":module:management:management-data-exposed")?.name = "management-data-exposed"

include("module:management:management-domain")
findProject(":module:management:management-domain")?.name = "management-domain"

include("module:management:management-spring")
findProject(":module:management:management-spring")?.name = "management-spring"

include("module:authentication:authentication-data")
findProject(":module:authentication:authentication-data")?.name = "authentication-data"

include("module:authentication:authentication-domain")
findProject(":module:authentication:authentication-domain")?.name = "authentication-domain"

include("module:authentication:authentication-tokenizer")
findProject(":module:authentication:authentication-tokenizer")?.name = "authentication-tokenizer"

include("module:authentication:authentication-tokenizer-auth0")
findProject(":module:authentication:authentication-tokenizer-auth0")?.name = "authentication-tokenizer-auth0"
