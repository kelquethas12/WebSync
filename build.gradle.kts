plugins {
    id("java")
    kotlin("jvm") version "2.0.20"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    kotlin("plugin.serialization") version "2.0.20"
}

group = "ru.freestyle.telegrambot"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://kotlin.bintray.com/kotlinx")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
    implementation("com.rabbitmq:amqp-client:5.22.0")
    api(fileTree("libraries"))
    implementation(kotlin("stdlib-jdk8"))
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

tasks.shadowJar {
    archiveBaseName.set("WebSync")
    archiveClassifier.set("")
    mergeServiceFiles()
    exclude("**/mcpc-plus.jar")
}

kotlin {
    jvmToolchain(8)
}