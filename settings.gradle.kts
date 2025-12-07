// Share plugin versions across subprojects (must be the first block)
pluginManagement {
    plugins {
        id("com.vanniktech.maven.publish") version "0.30.0"
    }
}

rootProject.name = "batis-fluid"
include("lib")
include("spring")
