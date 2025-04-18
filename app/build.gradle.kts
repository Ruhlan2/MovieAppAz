import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Date

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

fun gitLatestCommitHash(): String {
    val stdout = ByteArrayOutputStream()
    exec {
        commandLine("git", "rev-parse", "--short", "HEAD")
        standardOutput = stdout
    }
    return stdout.toString().trim()
}

fun getCurrentFormattedDate(format: String = "yyyy-MM-dd"): String {
    val currentDate = Date()
    val formatter = SimpleDateFormat(format)
    return formatter.format(currentDate)
}
android {
    namespace = "com.isteam.movieappaz"
    compileSdk = 34


    defaultConfig {
        applicationId = "com.isteam.movieappaz"
        minSdk = 24
        targetSdk = 34
        versionCode = 9
        versionName = "1.0.4"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        applicationVariants.all {
            outputs.all {
                val outputFileName =
                    "CineMate_${buildType.name}_${getCurrentFormattedDate()}_${gitLatestCommitHash()}.apk"
                (this as BaseVariantOutputImpl).outputFileName =
                    outputFileName
            }
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    bundle {
        language {
            enableSplit = false
        }
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    val nav_version = "2.7.6"
    val lifecycle_version = "2.6.2"
    val room_version = "2.6.1"
    val paging_version = "3.2.1"
    val lottieVersion = "6.4.0"

    implementation("com.google.firebase:firebase-auth-ktx:22.3.1")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("com.google.firebase:firebase-messaging:23.4.1")


    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")

    //Lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycle_version")

    //Retrofit & Okhttp & Chucker
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.2")

    //Dagger-Hilt
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-android-compiler:2.47")

    //Room
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    kapt("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")

    //Coroutines
    implementation("androidx.room:room-ktx:$room_version")

    //Security Crypto
    implementation("androidx.security:security-crypto:1.1.0-alpha06")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.15.1")
    kapt("com.github.bumptech.glide:compiler:4.15.1")

    //dots
    implementation("com.tbuonomo:dotsindicator:5.0")

    //Motion Toast
    implementation("com.github.Spikeysanju:MotionToast:1.4")

    //Lottie
    implementation("com.airbnb.android:lottie:$lottieVersion")

    //imageSlider
    implementation("com.github.denzcoskun:ImageSlideshow:0.1.2")

    //YoutubePlayer
    implementation("com.pierfrancescosoffritti.androidyoutubeplayer:core:12.1.0")

    //Paging 3
    implementation("androidx.paging:paging-runtime-ktx:$paging_version")


    //firebase
    implementation(platform("com.google.firebase:firebase-bom:32.8.1"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-storage")
    implementation("com.google.android.gms:play-services-auth:21.1.0")
    implementation("com.google.firebase:firebase-crashlytics")
    implementation("com.google.firebase:firebase-analytics")


    //gemini-ai
    implementation("com.google.ai.client.generativeai:generativeai:0.1.2")

    //flexbox
    implementation("com.google.android.flexbox:flexbox:3.0.0")

    //In-app updates
    // This dependency is downloaded from the Google’s Maven repository.
    // So, make sure you also include that repository in your project's build.gradle file.
    implementation("com.google.android.play:app-update:2.1.0")

    // For Kotlin users also import the Kotlin extensions library fo Play In-App Update:
    implementation("com.google.android.play:app-update-ktx:2.1.0")

}