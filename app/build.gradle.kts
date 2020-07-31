plugins {
    id(Plugins.androidApplication)
    id(Plugins.kotlinAndroid)
    id(Plugins.kotlinAndroidExtensions)
}

android {
    compileSdkVersion (AndroidSdk.compile)
    defaultConfig {

        minSdkVersion (AndroidSdk.min)
        targetSdkVersion (AndroidSdk.target)
        versionCode =AndroidSdk.versionCode
        versionName =AndroidSdk.versionName
        applicationId = "com.luis.analogwatch"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        named("release") {
            isMinifyEnabled =false
            proguardFiles (getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation (project(":lib"))
    implementation (Libraries.kotlinStdLib)
    implementation (Libraries.ktxCore)
    implementation (Libraries.appCompat)
    implementation (Libraries.constraintLayout)
    testImplementation (Libraries.junit)
    androidTestImplementation (Libraries.androidxJunit)
    androidTestImplementation (Libraries.expresso)
}