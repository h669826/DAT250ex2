import org.gradle.kotlin.dsl.annotationProcessor
import org.gradle.kotlin.dsl.compileOnly

plugins {
    java
    id("org.springframework.boot") version "3.5.4"
    id("io.spring.dependency-management") version "1.1.6"
    id("io.freefair.lombok") version "8.10.2"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.core:jackson-databind")

    implementation("org.springframework.boot:spring-boot-starter-amqp")

    implementation("jakarta.persistence:jakarta.persistence-api:3.2.0")
    testImplementation("org.hibernate.orm:hibernate-core:7.1.1.Final")

    testRuntimeOnly("com.h2database:h2:2.3.232")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.3")

    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("redis.clients:jedis:6.2.0")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("com.h2database:h2:2.3.232")
}
configurations.all {
    exclude(group = "com.vaadin.external.google", module = "android-json")
}
tasks.test {
    useJUnitPlatform()
}


