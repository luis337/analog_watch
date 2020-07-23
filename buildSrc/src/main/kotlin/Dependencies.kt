const val kotlinVersion = "1.3.72"

object Build {
    object Versions {
        const val buildToolsVersion = "4.0.0"
        const val googleServicesVersion = "4.2.0"
    }

    const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.buildToolsVersion}"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
    const val googleServicesPlugin =
        "com.google.gms:google-services:${Versions.googleServicesVersion}"
}

object Plugins{
    const val androidApplication = "com.android.application"
    const val kotlinAndroid = "kotlin-android"
    const val kotlinAndroidExtensions = "kotlin-android-extensions"
    const val googleServices = "com.google.gms.google-services"
    const val library = "com.android.library"
}

object AndroidSdk {
    const val min = 21
    const val compile = 29
    const val target = compile
    const val buildToolsVersion = "29.0.2"
    const val versionCode = 1
    const val versionName = "1.0"
}

object Art {
    const val name = "AnalogWatch"
    const val group = "com.luis37.lib"
    const val artifact = "analogWatch"
    const val version = "1.0"
    const val desc = "An Android custom analog watch. It just works as a decoration."
    const val repo = "maven"
}

object Libraries {
    private object Versions {
        const val appCompat = "1.1.0"
        const val constraintLayout = "1.1.3"
        const val recyclerview = "1.0.0"
        const val cardView = "1.0.0"
        const val dagger = "2.24"
        const val ktx = "1.1.0"
        const val material = "1.0.0"
        const val firebaseFirestore = "21.1.1"
        const val koin = "2.0.1"
        const val lottie = "3.0.7"
        const val rxjava = "2.2.10"
        const val rxkotlin = "2.4.0"
        const val rxandroid = "2.1.1"
        const val junit = "4.12"
        const val androidxJunit = "1.1.1"
        const val expresso = "3.2.0"
    }

    const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion"
    const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    const val junit = "junit:junit:${Versions.junit}"
    const val androidxJunit = "androidx.test.ext:junit:${Versions.androidxJunit}"
    const val expresso = "androidx.test.espresso:espresso-core:${Versions.expresso}"
    const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val recyclerview = "androidx.recyclerview:recyclerview:${Versions.recyclerview}"
    const val cardview = "androidx.cardview:cardview:${Versions.cardView}"
    const val ktxCore = "androidx.core:core-ktx:${Versions.ktx}"
    const val material = "com.google.android.material:material:${Versions.material}"
    const val firebaseFirestore =
        "com.google.firebase:firebase-firestore:${Versions.firebaseFirestore}"
    const val koin = "org.koin:koin-androidx-viewmodel:${Versions.koin}"
    const val lottie = "com.airbnb.android:lottie:${Versions.lottie}"
    const val rxjava = "io.reactivex.rxjava2:rxjava:${Versions.rxjava}"
    const val rxkotlin = "io.reactivex.rxjava2:rxkotlin:${Versions.rxkotlin}"
    const val rxandroid = "io.reactivex.rxjava2:rxandroid:${Versions.rxandroid}"
    const val dagger2 = "com.google.dagger:dagger:${Versions.dagger}"
    const val daggerCompiler = "com.google.dagger:dagger-compiler:${Versions.dagger}"
}