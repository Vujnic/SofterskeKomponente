plugins {
    kotlin("jvm")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation(project(":spec"))
    implementation(project(":kalkulacije"))
    runtimeOnly(project(":prvaImpl"))
    runtimeOnly(project(":drugaImplm"))
    runtimeOnly(project(":trecaImpl"))
    runtimeOnly(project(":cetvrtaImpl"))
    implementation("mysql:mysql-connector-java:8.0.33")
    implementation("com.google.code.gson:gson:2.10.1")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}