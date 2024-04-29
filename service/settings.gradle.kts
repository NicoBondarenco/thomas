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
