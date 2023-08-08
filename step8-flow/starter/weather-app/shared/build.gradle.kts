import org.jetbrains.compose.internal.utils.getLocalProperty
import java.util.*

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("org.jetbrains.compose")
    kotlin("plugin.serialization") version "1.9.0"
    id("com.codingfeline.buildkonfig") version "0.13.3"
    id("app.cash.sqldelight") version "2.0.0-alpha05"
}

kotlin {
    androidTarget()

    iosX64()
    iosArm64()
    iosSimulatorArm64() {
        // TODO: remove after 1.5 release
        binaries.forEach {
            it.freeCompilerArgs += listOf(
                "-linker-option", "-framework", "-linker-option", "Metal",
                "-linker-option", "-framework", "-linker-option", "CoreText",
                "-linker-option", "-framework", "-linker-option", "CoreGraphics",
            )
        }
    }


    cocoapods {
        version = "1.0.0"
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
            isStatic = true
        }
        extraSpecAttributes["resources"] =
            "['src/commonMain/resources/**', 'src/iosMain/resources/**']"
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)
                api("io.github.qdsfdhvh:image-loader:1.6.3")
                implementation("cafe.adriel.voyager:voyager-navigator:1.0.0-rc05")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
                implementation("io.ktor:ktor-client-core:2.3.3")
                implementation("io.ktor:ktor-client-content-negotiation:2.3.3")
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.3")
                implementation("app.cash.sqldelight:runtime:2.0.0-alpha05")
                implementation("app.cash.sqldelight:primitive-adapters:2.0.0-alpha05")
                implementation("io.github.xxfast:kstore:0.6.0")
                implementation("io.github.xxfast:kstore-file:0.6.0")
                api("com.rickclephas.kmm:kmm-viewmodel-core:1.0.0-ALPHA-12")
                //implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting {
            dependencies {
                api("androidx.activity:activity-compose:1.7.2")
                api("androidx.appcompat:appcompat:1.6.1")
                api("androidx.core:core-ktx:1.10.1")
                implementation("io.ktor:ktor-client-android:2.3.3")
                implementation("app.cash.sqldelight:android-driver:2.0.0-alpha05")
                implementation("app.cash.sqldelight:primitive-adapters-jvm:2.0.0-alpha05")
                implementation("androidx.startup:startup-runtime:1.2.0-alpha02")
            }
        }
        val androidUnitTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13.2")
            }
        }
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                implementation("io.ktor:ktor-client-darwin:2.3.3")
                implementation("app.cash.sqldelight:native-driver:2.0.0-alpha05")
                implementation("app.cash.sqldelight:primitive-adapters:2.0.0-alpha05")
            }
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    namespace = "com.myapplication.common"

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        minSdk = (findProperty("android.minSdk") as String).toInt()
        targetSdk = (findProperty("android.targetSdk") as String).toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        jvmToolchain(11)
    }
}

buildkonfig {
    packageName = "com.myapplication"
    objectName = "ApiKeyConfig"
    exposeObjectWithName = "Config"

    defaultConfigs {
        buildConfigField(
            com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING,
            "WeatherApiKey",
            getLocalProperty("WEATHER_API_KEY").toString()
        )
    }
}
