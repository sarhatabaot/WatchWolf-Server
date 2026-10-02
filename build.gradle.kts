plugins {
    java
    `maven-publish`
    id("com.gradleup.shadow") version "9.4.3"
}

group = "dev.watchwolf"
version = "0.3.1"

val coreVersion = "0.3.3"

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.16.5-R0.1-SNAPSHOT")
    compileOnly("org.apache.logging.log4j:log4j-core:2.17.1")

    implementation("com.github.watch-wolf:WatchWolf-Core:$coreVersion")
    implementation("com.github.cryptomorin:XSeries:9.9.0")
}

tasks.compileJava {
    options.release.set(8)
    options.encoding = "UTF-8"
}

tasks.processResources {
    filesMatching("plugin.yml") {
        expand("version" to project.version)
    }
}

tasks.shadowJar {
    archiveFileName.set("WatchWolf-${project.version}-1.8-LATEST.jar")
    archiveClassifier.set("")
}

tasks.jar {
    enabled = false
}

tasks.assemble {
    dependsOn(tasks.shadowJar)
}

publishing {
    publications {
        create<MavenPublication>("plugin") {
            artifact(tasks.shadowJar)
            artifactId = "watchwolf-server"
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/miranda1000/watchwolf-server")
            credentials {
                username = providers.environmentVariable("GITHUB_ACTOR").orNull
                password = providers.environmentVariable("GITHUB_TOKEN").orNull
            }
        }
    }
}
