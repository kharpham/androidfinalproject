pluginManagement {
    repositories {
        google()
        jcenter()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        jcenter()
        maven { url= uri( "https://jitpack.io") }
    }
}

rootProject.name = "Group12FinalProject"
include(":app")
 