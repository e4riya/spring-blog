plugins {
	java
	id("org.springframework.boot") version "4.0.4"
	id("io.spring.dependency-management") version "1.1.7"
	jacoco
	id("org.sonarqube") version "7.2.3.7755"
}

group = "hexlet.code"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-devtools")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-validation")

	compileOnly("org.projectlombok:lombok:1.18.44")
	annotationProcessor("org.projectlombok:lombok:1.18.44")

	runtimeOnly("com.h2database:h2")

	implementation("net.datafaker:datafaker:1.9.0")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testImplementation("org.springframework.boot:spring-boot-webmvc-test")
	testImplementation("org.springframework.security:spring-security-test")
	//testImplementation("net.javacrumbs.json-unit:json-unit-assertj:3.2.2")
	testImplementation("org.instancio:instancio-junit:6.0.0-RC2")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.jacocoTestReport {
	dependsOn(tasks.test)
	reports {
		xml.required.set(true)
		html.required.set(true)
	}
}
tasks.check {
	dependsOn(tasks.jacocoTestReport)
}

sonar {
	properties {
		property("sonar.projectKey", "e4riya_spring-blog")
		property("sonar.organization", "e4riya")
		property("sonar.host.url", "https://sonarcloud.io")
	}
}
