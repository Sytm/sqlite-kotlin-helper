import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.21"
    id("org.jetbrains.dokka") version "1.4.20"
    `maven-publish`
}

group = "de.md5lukas"
version = "1.0.0-SNAPSHOT"
description = "PainVentories"

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()

    maven(url = "https://repo.sytm.de/repository/maven-hosted/")
    maven(url = "https://oss.sonatype.org/content/groups/public/")
}

dependencies {
    api(kotlin("stdlib-jdk8"))
    compileOnly("org.jetbrains:annotations:20.1.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

val javadocJar by tasks.creating(Jar::class) {
    dependsOn(tasks.dokkaJavadoc)
    archiveClassifier.set("javadoc")
    from(tasks.dokkaJavadoc)
}

publishing {
    repositories {
        maven {
            val releasesRepoUrl = "https://repo.sytm.de/repository/maven-releases/"
            val snapshotsRepoUrl = "https://repo.sytm.de/repository/maven-snapshots/"
            url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)
            credentials {
                if (project.hasProperty("mavenUsername")) {
                    username = project.properties["mavenUsername"] as String
                }
                if (project.hasProperty("mavenPassword")) {
                    password = project.properties["mavenPassword"] as String
                }
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            from(components["kotlin"])
            artifact(sourcesJar)
            artifact(javadocJar)
        }
    }
}