plugins {
    id(Plugins.library)
    id(Plugins.kotlinAndroid)
    id(Plugins.kotlinAndroidExtensions)
    id("org.jetbrains.dokka") version "0.10.1"
    `maven-publish`
}

tasks {
    val dokkaJavadoc by creating(org.jetbrains.dokka.gradle.DokkaTask::class) {
        outputFormat = "javadoc"
        outputDirectory = "$buildDir/javadoc"
        configuration {
            sourceLink {
                path = "src/main/java"
                url = "https://github.com/luis337/analog_watch/tree/master/lib/src/main/java"
                lineSuffix = "#L"
            }
        }
    }

    register("androidJavadocJar", Jar::class) {
        archiveClassifier.set("javadoc")
        from("$buildDir/javadoc")
        dependsOn(dokkaJavadoc)
    }
    register("androidSourcesJar", Jar::class) {
        archiveClassifier.set("sources")
        from(android.sourceSets.getByName("main").java.srcDirs)
    }
}

val prop = org.jetbrains.kotlin.konan.properties.Properties()
prop.load(project.rootProject.file("local.properties").inputStream())

android {
    compileSdkVersion (AndroidSdk.compile)
    defaultConfig {

        minSdkVersion (AndroidSdk.min)
        targetSdkVersion (AndroidSdk.target)
        versionCode = AndroidSdk.versionCode
        versionName = AndroidSdk.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        named("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions.jvmTarget = "1.8"
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation (Libraries.kotlinStdLib)
    implementation (Libraries.ktxCore)
    implementation (Libraries.appCompat)
    implementation (Libraries.constraintLayout)
    testImplementation (Libraries.junit)
    androidTestImplementation (Libraries.androidxJunit)
    androidTestImplementation (Libraries.expresso)
}

afterEvaluate {
    publishing {
        publications {
            register<MavenPublication>("release") {
                groupId = Publish.Meta.group
                artifactId = Publish.Meta.artifact
                version = Publish.Meta.version
                artifact(tasks.getByName("bundleReleaseAar"))
                artifact(tasks.getByName("androidJavadocJar"))
                artifact(tasks.getByName("androidSourcesJar"))
                pom {
                    name.set(Publish.Pom.name)
                    description.set(Publish.Pom.desc)
                    url.set(Publish.Pom.url)

                    licenses {
                        license {
                            name.set(Publish.Lic.name)
                            url.set(Publish.Lic.url)
                        }
                    }
                    developers {
                        developer {
                            id.set(Publish.Dev.id)
                            name.set(Publish.Dev.name)
                            email.set(Publish.Dev.email)
                        }
                    }

                    scm {
                        connection.set(Publish.Scm.con)
                        developerConnection.set(Publish.Scm.devc)
                        url.set(Publish.Scm.url)
                    }

                    withXml {
                        fun groovy.util.Node.addDependency(
                            dependency: Dependency,
                            scope: String
                        ) {
                            appendNode("dependency").apply {
                                appendNode("groupId", dependency.group)
                                appendNode("artifactId", dependency.name)
                                appendNode("version", dependency.version)
                                appendNode("scope", scope)
                            }
                        }

                        asNode().appendNode("dependencies").let { dependencies ->
                            configurations.api.get().allDependencies.forEach {
                                dependencies.addDependency(it, "compile")
                            }
                            configurations.implementation.get().allDependencies.forEach {
                                dependencies.addDependency(it, "runtime")
                            }
                        }
                    }
                }
            }
        }
        repositories {
            maven {
                name = Publish.Repo.name
                credentials {
                    username = prop.getProperty("USER")
                    password = prop.getProperty("PASS")
                }
                url = uri(Publish.Repo.url)
            }
        }
    }
}

