plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka") version "1.8.10"
    `java-library`
    `maven-publish`
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    //implementation("mysql:mysql-connector-java:8.0.33")
}

tasks.test {
    useJUnitPlatform()
}

tasks.javadoc {
    dependsOn(tasks.dokkaJavadoc)
    doLast {
        println("javadoc task completed with Dokka output");
    }
}

tasks.dokkaJavadoc {
    outputDirectory.set(file("build/dokka/javadoc"))
}


publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"]) // If you're using the 'java' or 'kotlin' plugin

            groupId = "org.example"
            artifactId = "spec"
            version = "1.0.0"
        }
    }
}

kotlin {
    jvmToolchain(21)
}


