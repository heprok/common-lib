import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version Versions.SPRING_BOOT_VERSION
    id("io.spring.dependency-management") version Versions.SPRING_DEPENDENCY_MANAGEMENT

    kotlin("jvm") version Versions.KOTLIN
    kotlin("kapt") version Versions.KOTLIN
    kotlin("plugin.spring") version Versions.KOTLIN

    `java-library`
    `maven-publish`
}

group = "com.briolink.lib"
version = "0.0.36-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    compileOnly("javax.servlet:javax.servlet-api:4.0.1")
    annotationProcessor("org.springframework.boot:spring-boot-autoconfigure-processor")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    implementation("org.springframework.security:spring-security-core:${Versions.SPRING_SECURITY}")
    implementation("org.springframework.security:spring-security-oauth2-jose:${Versions.SPRING_SECURITY}")
    implementation("org.springframework.security:spring-security-oauth2-resource-server:${Versions.SPRING_SECURITY}")
    implementation("org.aspectj:aspectjrt:1.7.3")
    kapt("org.springframework.boot:spring-boot-autoconfigure-processor:${Versions.SPRING_BOOT_VERSION}")
    kapt("org.springframework.boot:spring-boot-configuration-processor:${Versions.SPRING_BOOT_VERSION}")

    // FasterXML
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // Netflix DGS
    implementation("com.netflix.graphql.dgs:graphql-dgs-extended-scalars:${Versions.GRAPHQL_DGS}")
    implementation("com.netflix.graphql.dgs:graphql-error-types:${Versions.GRAPHQL_DGS}")

    implementation("joda-time:joda-time:2.10.14")
    kapt("jakarta.annotation:jakarta.annotation-api")

    // Kotlin
    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib-jdk8"))

    // kotlin-logging
    implementation("io.github.microutils:kotlin-logging-jvm:2.0.11")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11

    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }

    repositories {
        mavenLocal()
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<GenerateMavenPom>().all {
    doLast {
        val file = File("$buildDir/publications/maven/pom-default.xml")
        var text = file.readText()
        val regex =
            "(?s)(<dependencyManagement>.+?<dependencies>)(.+?)(</dependencies>.+?</dependencyManagement>)".toRegex()
        val matcher = regex.find(text)
        if (matcher != null) {
            text = regex.replaceFirst(text, "")
            val firstDeps = matcher.groups[2]!!.value
            text = regex.replaceFirst(text, "$1$2$firstDeps$3")
        }
        file.writeText(text)
    }
}
