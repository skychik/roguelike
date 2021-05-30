plugins {
    java
    application
    kotlin("jvm") version "1.4.21"
    kotlin("plugin.serialization") version "1.4.21"
}

group = "ru.ifmo.jb"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation("ch.qos.logback:logback-classic:1.2.3")

    implementation("io.ktor:ktor-server-core:1.5.2")
    implementation("io.ktor:ktor-server-netty:1.5.2")
    implementation("io.ktor:ktor-serialization:1.5.2")
    testImplementation("io.ktor:ktor-server-tests:1.5.2")
}

application {
    mainClassName = "ru.ifmo.sd.httpapi.ApplicationKt"
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

tasks.test {
    useJUnitPlatform()
}
