pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven {
            url = uri("https://storage.googleapis.com/download.flutter.io")
        }
        maven {
            url = uri("/Users/macmini/Downloads/repo")
        }

    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
    repositories {
        google()
        maven { url = uri("https://maven.google.com") }
        mavenCentral()
        maven { url =uri("https://jitpack.io" )}
        maven { url =uri("https://api.mapbox.com/downloads/v2/releases/maven") }
        maven { url =uri("https://raw.githubusercontent.com/rafaelsetragni/awesome_notifications/master/android/m2repository") }
        maven {
            url = uri("https://storage.googleapis.com/download.flutter.io")
        }
        maven {
            url = uri("/Users/macmini/Downloads/repo")
        }


    }
}

rootProject.name = "moudleAndroid"
include(":app")
 