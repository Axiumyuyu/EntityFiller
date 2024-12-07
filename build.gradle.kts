plugins {
    kotlin("jvm") version "2.0.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "me.axiumyu"
version = "2.1.2"

repositories {
    mavenLocal()
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc-repo"
    }
    maven("https://oss.sonatype.org/content/groups/public/") {
        name = "sonatype"
    }
    maven("https://repo.codemc.io/repository/maven-public/") {
        name = "CodeMC"
    }
    maven("https://maven.enginehub.org/repo/")
    maven ("https://repo.papermc.io/repository/maven-public/")
    mavenCentral()
}

dependencies {
    implementation("io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")

    implementation("de.tr7zw:item-nbt-api-plugin:2.14.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.sk89q.worldedit:worldedit-core:7.2.13")
    implementation("com.sk89q.worldedit:worldedit-bukkit:7.2.13")

    testImplementation("io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")
    testImplementation("de.tr7zw:item-nbt-api-plugin:2.14.0")
    testImplementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("com.sk89q.worldedit:worldedit-core:7.2.13")
    testImplementation("com.sk89q.worldedit:worldedit-bukkit:7.2.13")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.8.2")
    testImplementation(kotlin("test"))
    testImplementation("org.mockbukkit.mockbukkit:mockbukkit-v1.21:4.8.0")
}

tasks.test{
    useJUnitPlatform()
}

val targetJavaVersion = 21
kotlin {
    jvmToolchain(targetJavaVersion)
}

tasks.build {
    dependsOn("shadowJar")
}

tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand(props)
    }
}
