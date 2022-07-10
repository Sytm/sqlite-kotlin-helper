import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    id("org.jetbrains.dokka") version "1.7.0"
    `maven-publish`
}

group = "de.md5lukas"
version = "1.1.0"
description = "Helpers methods for SQLite to reduce boilerplate code"

repositories {
    mavenCentral()

    maven(url = "https://oss.sonatype.org/content/groups/public/")
}

dependencies {
    api(kotlin("stdlib-jdk8"))
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
            name = "md5lukasReposilite"

            url = uri("https://repo.md5lukas.de/${if (version.toString().endsWith("-SNAPSHOT")) {
                "snapshots"
            } else {
                "releases"
            }}")

            credentials(PasswordCredentials::class)
            authentication {
                create<BasicAuthentication>("basic")
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