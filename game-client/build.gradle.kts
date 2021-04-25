plugins {
    java
    kotlin("jvm") version "1.4.21"
}

group = "ru.ifmo.jb"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
}

tasks.test {
    useJUnitPlatform()
}
