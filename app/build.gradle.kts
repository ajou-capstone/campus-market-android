import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.gms)
    alias(libs.plugins.firebase.crashlytics)
}

android {
    namespace = "kr.linkerbell.campusmarket.android"
    compileSdk = libs.versions.sdk.compile.get().toInt()

    defaultConfig {
        applicationId = "kr.linkerbell.campusmarket.android"
        minSdk = libs.versions.sdk.min.get().toInt()
        targetSdk = libs.versions.sdk.target.get().toInt()
        versionCode = libs.versions.app.versioncode.get().toInt()
        versionName = libs.versions.app.versionname.get()

        manifestPlaceholders["kakaoAppKey"] = getLocalProperty("KAKAO_APP_KEY")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            resValue("string", "key_kakao_app", getLocalProperty("KAKAO_APP_KEY"))
        }
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
            isDebuggable = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            resValue("string", "key_kakao_app", getLocalProperty("KAKAO_APP_KEY"))
        }
    }

    flavorDimensions += "server"
    productFlavors {
        create("development") {
            dimension = "server"
        }
        create("production") {
            dimension = "server"
        }
    }

    /**
     * Android 14 JDK 17 지원
     * url : https://developer.android.com/about/versions/14/behavior-changes-14?hl=ko#core-libraries
     */
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
    testFixtures {
        enable = true
    }
}

dependencies {
    implementation(project(":common"))
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":presentation"))

    implementation(libs.bundles.kotlin)
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    implementation(libs.bundles.network)

    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.kakao)
    implementation(libs.bundles.logging)
    debugImplementation(libs.leakcanary)

    testImplementation(testFixtures(project(":app")))
    testImplementation(testFixtures(project(":data")))
    testImplementation(testFixtures(project(":domain")))
    testImplementation(testFixtures(project(":presentation")))

    testImplementation(libs.bundles.test)
    kspTest(libs.hilt.android.compiler)

    testFixturesImplementation(libs.bundles.kotlin)
    testFixturesImplementation(libs.hilt.android)
    testFixturesImplementation(libs.bundles.test)
    // TODO : Ksp / Kapt Test Fixtures Update 대기중
    kspTestFixtures(libs.hilt.android.compiler)
}

fun getLocalProperty(propertyKey: String): String {
    return gradleLocalProperties(rootDir, providers).getProperty(propertyKey) ?: System.getenv(
        propertyKey
    )
}
