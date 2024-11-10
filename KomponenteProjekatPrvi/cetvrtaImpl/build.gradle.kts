plugins {
    kotlin("jvm")
    `java-library`
    `maven-publish`
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation("org.apache.poi:poi:5.2.3") // For .xls format
    implementation("org.apache.poi:poi-ooxml:5.2.3") // For .xlsx format
    implementation(project(":spec"))
    implementation("org.apache.logging.log4j:log4j-core:2.20.0") // Zameni sa verzijom, npr., 2.17.2
    implementation("org.apache.logging.log4j:log4j-api:2.20.0")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"]) // If you're using the 'java' or 'kotlin' plugin

            groupId = "org.example"
            artifactId = "cetvrtaImpl"
            version = "1.0.0"
        }
    }
}



kotlin {
    jvmToolchain(21)
}