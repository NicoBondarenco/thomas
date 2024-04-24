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

include("infrastructure:jwt:jwt-base")
findProject(":infrastructure:jwt:jwt-base")?.name = "jwt-base"

include("infrastructure:jwt:jwt-auth0")
findProject(":infrastructure:jwt:jwt-auth0")?.name = "jwt-auth0"

include("infrastructure:database:mongo-base")
findProject(":infrastructure:database:mongo-base")?.name = "mongo-base"
