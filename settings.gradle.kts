enableFeaturePreview("VERSION_CATALOGS")

pluginManagement {
    repositories {
        google()
        jcenter()
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "KMM_App_Test"
include(":androidKMMAppTest")
include(":shared")

dependencyResolutionManagement {
    versionCatalogs {
        create("appVersions") {
            from(files("gradle/app.versions.toml"))
        }
    }
}