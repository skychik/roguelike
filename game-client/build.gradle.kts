plugins {
    java
    kotlin("jvm") version "1.4.21"
}

group = "ru.ifmo.jb"
version = "1.0-SNAPSHOT"
val ktor_version = "1.5.2"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-client-gson:$ktor_version")
    implementation("io.ktor:ktor-client-jackson:$ktor_version")
    implementation("io.ktor:ktor-client-serialization:1.5.2")
    implementation(project(":game-server"))
}

tasks.test {
    useJUnitPlatform()
}
