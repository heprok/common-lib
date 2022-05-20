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
version = "0.0.40-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-webflux:${Versions.SPRING_BOOT_VERSION}")
    implementation("org.springframework.boot:spring-boot-starter-validation:${Versions.SPRING_BOOT_VERSION}")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:${Versions.SPRING_BOOT_VERSION}")
    compileOnly("javax.servlet:javax.servlet-api:${Versions.JAVA_SERVLET}")
    annotationProcessor("org.springframework.boot:spring-boot-autoconfigure-processor")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    implementation("org.springframework.security:spring-security-core:${Versions.SPRING_SECURITY}")
    implementation("org.springframework.security:spring-security-oauth2-jose:${Versions.SPRING_SECURITY}")
    implementation("org.springframework.security:spring-security-oauth2-resource-server:${Versions.SPRING_SECURITY}")

    kapt("org.springframework.boot:spring-boot-autoconfigure-processor:${Versions.SPRING_BOOT_VERSION}")
    kapt("org.springframework.boot:spring-boot-configuration-processor:${Versions.SPRING_BOOT_VERSION}")

    // Hibernate Types 55
    api("com.vladmihalcea:hibernate-types-55:${Versions.HIBERNATE_TYPES_55}")

    // Netflix DGS
    implementation("com.netflix.graphql.dgs:graphql-dgs-extended-scalars:${Versions.GRAPHQL_DGS}")
    implementation("com.netflix.graphql.dgs:graphql-error-types:${Versions.GRAPHQL_DGS}")

    implementation("joda-time:joda-time:${Versions.JODA_TIME}")
    kapt("jakarta.annotation:jakarta.annotation-api")

    // Kotlin
    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib-jdk8"))
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
        maven {
            name = "GitLab"
            url = uri("https://gitlab.com/api/v4/projects/36319712/packages/maven")

            authentication {
                create<HttpHeaderAuthentication>("header")
            }

            credentials(HttpHeaderCredentials::class) {
                name = "Deploy-Token"
                value = System.getenv("GITLAB_DEPLOY_TOKEN")
            }
        }
        mavenLocal()
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}
