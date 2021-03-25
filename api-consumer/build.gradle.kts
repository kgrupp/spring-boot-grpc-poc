import com.google.protobuf.gradle.generateProtoTasks
import com.google.protobuf.gradle.id
import com.google.protobuf.gradle.ofSourceSet
import com.google.protobuf.gradle.plugins
import com.google.protobuf.gradle.protobuf
import com.google.protobuf.gradle.protoc
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        mavenCentral()
    }
}

plugins {
    idea
    kotlin("jvm").version("1.4.31")
    kotlin("plugin.spring").version("1.4.31")

    id("org.springframework.boot").version("2.4.3")
    id("io.spring.dependency-management").version("1.0.11.RELEASE")

    id("com.google.protobuf") version "0.8.15"
}

version = "0.0.1-SNAPSHOT"

tasks.withType<KotlinCompile> {
    kotlinOptions {
        sourceCompatibility = "11"
        jvmTarget = "11"
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.4.31")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.4.31")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")

    implementation("net.logstash.logback:logstash-logback-encoder:6.6")

    implementation("net.devh:grpc-client-spring-boot-starter:2.11.0.RELEASE")
    implementation("com.google.protobuf:protobuf-java-util:3.15.6")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
    testImplementation("org.springframework.boot:spring-boot-starter-webflux")
    testImplementation("io.grpc:grpc-testing:1.36.0") {
        exclude(group = "junit", module = "junit")
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.15.6"
    }

    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.36.0"
        }
    }

    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins {
                id("grpc") {
                    outputSubDir = "java"
                }
            }
        }
    }
}