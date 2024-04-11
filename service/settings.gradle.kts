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
