import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.3.21"
}

group = "ca.marshallwalker"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

fun ktor(feature: String) = "io.ktor:$feature:1.2.1"

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.9")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.9.9")

    implementation(ktor("ktor-client-core"))
    implementation(ktor("ktor-client-apache"))
    implementation(ktor("ktor-client-json"))
    implementation(ktor("ktor-client-jackson"))

    implementation("ch.qos.logback:logback-classic:1.3.0-alpha4")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}