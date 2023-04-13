plugins {
    kotlin("jvm") version "1.8.10"
    id("org.jetbrains.dokka") version "1.7.20"
    `maven-publish`
}

group = "de.md5lukas"
version = "1.2.1"
description = "Helpers methods for SQLite to reduce boilerplate code"

repositories {
    mavenCentral()

    maven(url = "https://oss.sonatype.org/content/groups/public/")
}

dependencies {
    api(kotlin("stdlib-jdk8"))
    compileOnly("org.jetbrains:annotations:24.0.1")
}

kotlin {
    jvmToolchain(16)
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
