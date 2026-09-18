pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // Maptec Maven releases（公开制品）
        maven {
            url = uri("https://maven.maptec.cn/repository/maven-releases/")
        }
    }
}

rootProject.name = "navi-sdk-sample"

include(":navi-sdk-sample")
