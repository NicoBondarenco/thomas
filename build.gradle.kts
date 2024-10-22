import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.lang)
    id(libs.plugins.jacoco.plugin.get().pluginId)
}

allprojects {

    val libs = rootProject.libs

    apply {
        plugin(libs.plugins.kotlin.lang.get().pluginId)
        plugin(libs.plugins.jacoco.plugin.get().pluginId)
    }

    group = "com.thomas"
    version = properties["version"].toString()

    java.sourceCompatibility = JavaVersion.valueOf(libs.versions.target.get())
    java.targetCompatibility = JavaVersion.valueOf(libs.versions.target.get())

    repositories {
        mavenCentral()
        mavenLocal()
        maven(url = "https://repo.typedb.com/public/public-release/maven/")
    }

    dependencies {
        implementation(libs.bundles.kotlin.standard.bundle)

        implementation(libs.bundles.log4j.kotlin.bundle)

        testImplementation(libs.bundles.test.standard.bundle)
    }

    jacoco {
        toolVersion = libs.versions.jacoco.get()
    }

    tasks.jacocoTestReport {
        reports {
            xml.required = true
            html.required = true
        }
    }

    tasks.test {
        useJUnitPlatform()
        finalizedBy("jacocoTestReport")
    }

    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions.jvmTarget.set(JvmTarget.valueOf(libs.versions.jvm.get()))
    }

}
