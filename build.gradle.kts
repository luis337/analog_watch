// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath (Build.androidGradlePlugin)
        classpath (Build.kotlinGradlePlugin)
    }
}


allprojects {
    repositories {
        google()
        jcenter()
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}