plugins {
    id("com.github.johnrengelman.shadow") version "8.1.0"
    id("java")
}

group = "cloud.antony.mariogpt-client"
version = "1.0.0"

tasks {
    compileJava {
        sourceCompatibility = "1.8"
        targetCompatibility = "1.8"
    }

    jar {
        manifest {
            attributes["Main-Class"] = "cloud.antony.mariogptclient.PlayLevel"
        }
    }

    shadowJar {
        archiveBaseName.set("mariogpt-client")
        archiveClassifier.set("")
        archiveVersion.set("")
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind:2.14.0")
}
