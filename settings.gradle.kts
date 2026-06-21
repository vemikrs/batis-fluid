// Share plugin versions across subprojects (must be the first block)
pluginManagement {
    plugins {
        id("com.vanniktech.maven.publish") version "0.37.0"
    }
}

rootProject.name = "batis-fluid"
include("lib")
include("spring")
