plugins {
    kotlin("jvm") version "2.0.0"
    kotlin("plugin.serialization") version "2.0.0"
    application
}

group = "com.aymenhta"
version = "1.0.0"

repositories {
    mavenCentral()
}

val exposed_version = "0.52.0"
dependencies {
    // CLI TOOL
    implementation("com.github.ajalt.clikt:clikt:4.4.0")
    // DB ACCESS
    implementation("org.jetbrains.exposed", "exposed-core", exposed_version)
    implementation("org.jetbrains.exposed", "exposed-dao", exposed_version)
    implementation("org.jetbrains.exposed", "exposed-jdbc", exposed_version)
    // For the times datatypes
    implementation("org.jetbrains.exposed", "exposed-java-time", exposed_version)
    implementation("com.mysql:mysql-connector-j:9.0.0")
    implementation("com.zaxxer:HikariCP:4.0.3")
    implementation("org.slf4j:slf4j-nop:2.0.13")
    // JSON SERIALIZATION
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.1")

    // TEST
    testImplementation(kotlin("test"))
}


application {
    mainClass.set("com.aymenhta.MainKt")
}

tasks {
    val fatJar = register<Jar>("fatJar") {
        dependsOn.addAll(listOf("compileJava", "compileKotlin", "processResources")) // We need this for Gradle optimization to work
        archiveClassifier.set("standalone") // Naming the jar
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        manifest { attributes(mapOf("Main-Class" to application.mainClass)) } // Provided we set it up in the application plugin configuration
        val sourcesMain = sourceSets.main.get()
        val contents = configurations.runtimeClasspath.get()
            .map { if (it.isDirectory) it else zipTree(it) } +
                sourcesMain.output
        from(contents)
    }
    build {
        dependsOn(fatJar) // Trigger fat jar creation during build
    }
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}