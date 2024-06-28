// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) version "8.0.0" apply false
    alias(libs.plugins.jetbrains.kotlin.android) version "1.8.0" apply false
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}
