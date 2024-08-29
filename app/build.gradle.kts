plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.activetytest"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.activetytest"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

android {
    defaultConfig {
        ndk {
            // 设置支持的SO库架构（开发者可以根据需要，选择一个或多个平台的so）
            abiFilters.add("armeabi")
            abiFilters.add("armeabi-v7a")
            abiFilters.add("arm64-v8a")
            abiFilters.add("x86")
            abiFilters.add("x86_64")
        }
    }
}


dependencies {
    implementation ("com.baidu.lbsyun","BaiduMapSDK_Map","7.6.2")
    implementation("com.baidu.lbsyun","BaiduMapSDK_Location","9.6.4")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("com.squareup.okhttp3","okhttp","3.4.1")
    implementation("com.google.code.gson","gson","2.7")
}