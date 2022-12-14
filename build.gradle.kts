import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    `build-scan`
    id("org.springframework.boot") version "2.1.7.RELEASE"
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
    id("org.jetbrains.dokka") version "0.9.17"
    kotlin("jvm") version "1.3.50"
    kotlin("plugin.spring") version "1.3.50"
    kotlin("plugin.jpa") version "1.3.50"
    id("org.jlleitschuh.gradle.ktlint") version "9.4.1"
    id("io.gitlab.arturbosch.detekt") version "1.1.1"
    id("org.jetbrains.kotlin.kapt") version "1.3.50"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.3.50"
    jacoco
}

group = "co.brainz"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

val developmentOnly by configurations.creating
configurations {
    runtimeClasspath {
        extendsFrom(developmentOnly)
    }

    "implementation" {
        exclude(module = "spring-boot-starter-logging")
    }
}

repositories {
    mavenCentral()
}

apply(plugin = "kotlin-kapt")

apply(plugin = "kotlin-allopen")

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-rest")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-logging")
    implementation("ch.qos.logback:logback-classic:1.2.9")
    implementation("ch.qos.logback:logback-core:1.2.9")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-devtools")
    implementation("org.springframework.social:spring-social-google:latest.integration")
    implementation("org.bgee.log4jdbc-log4j2:log4jdbc-log4j2-jdbc4.1:1.16")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.9.10")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("net.rakugakibox.util:yaml-resource-bundle:1.1")
    implementation("io.github.microutils:kotlin-logging:1.12.5")
    implementation("com.github.ulisesbocchio:jasypt-spring-boot-starter:2.1.2")
    implementation("commons-codec:commons-codec:1.13")
    implementation("org.apache.tika:tika-core:1.22")
    implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity5")
    implementation("nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect")
    implementation("org.apache.httpcomponents:httpcore:4.4.13")
    implementation("org.apache.httpcomponents:httpclient:4.5.11")
    implementation("com.google.code.gson:gson:2.8.2")
    implementation("org.apache.poi:poi:5.0.0")
    implementation("org.apache.poi:poi-ooxml:5.0.0")
    runtimeOnly("org.postgresql:postgresql")
    implementation("javax.servlet:jstl")
    implementation("org.apache.tomcat.embed:tomcat-embed-jasper")
    implementation("com.querydsl:querydsl-core:5.0.0")
    kapt(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    // Junit 5 ??????
    // 2021-03-10 Jung Hee Chan
    // Junit 4??? ???????????? ???????????? ??????. => vintage ??????
    // ?????? Spring Boot ??? Kotlin ?????? ??????????????? ?????? ??? ?????? ?????? ??????.
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude("junit") // SpringBoot??? ?????? ????????? Junit 4??? ??????.
    }

    // ?????? ???????????? "WARNING: TestEngine with ID 'junit-jupiter' failed to discover tests"??? ?????? ?????? ??????.
    // dependency??? ?????? 2????????? ????????? ??????.

    // junit-platform-commons ?????? ??????
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.5.2") { exclude("junit-platform-commons") }
    testImplementation("org.junit.platform:junit-platform-commons:1.5.1")
    // junit-jupiter-engine ?????? ??????
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.5.2") { exclude("junit-platform-engine") }
    testImplementation("org.junit.platform:junit-platform-engine:1.5.1")

    testImplementation("org.junit.vintage:junit-vintage-engine:5.5.2")

    testImplementation("org.springframework.security:spring-security-test")

    kapt("org.springframework.boot:spring-boot-configuration-processor")
    compile("com.querydsl:querydsl-jpa:5.0.0")
    kapt("com.querydsl:querydsl-apt:5.0.0:jpa")

    implementation("org.mapstruct:mapstruct:1.3.0.Final")
    kapt("org.mapstruct:mapstruct-processor:1.3.0.Final")
    kaptTest("org.mapstruct:mapstruct-processor:1.3.0.Final")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

// KTLINT
ktlint {
    version.set("0.35.0")
    debug.set(false)
    verbose.set(true)
    android.set(false)
    outputToConsole.set(true)
    outputColorName.set("BLUE")
    ignoreFailures.set(true)
    reporters {
        reporter(ReporterType.CHECKSTYLE)
    }
    kotlinScriptAdditionalPaths {
        include(fileTree("scripts/"))
    }
    filter {
        exclude("**/generated/**")
        include("**/kotlin/**")
    }
}

// JACOCO
jacoco {
    toolVersion = "0.8.5"
}

tasks.jacocoTestReport {
    reports {
        xml.isEnabled = false
        csv.isEnabled = false
        html.destination = file("$buildDir/jacocoHtml")
    }
}

// DETEKT
detekt {
    toolVersion = "1.1.1"
    input = files(
        "src/main/kotlin",
        "src/test/kotlin"
    )
    parallel = false
    buildUponDefaultConfig = false
    disableDefaultRuleSets = false
    debug = false
    ignoreFailures = true
    reports {
        xml {
            enabled = true
            destination = file("$buildDir/reports/detekt/detekt.xml")
        }
        html {
            enabled = true
            destination = file("$buildDir/reports/detekt/detekt.html")
        }
    }
}

// JAVADOC
tasks.dokka {
    outputFormat = "html"
    outputDirectory = "$buildDir/javadoc"
}

val dokkaJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles Kotlin docs with Dokka"
    classifier = "javadoc"
    from(tasks.dokka)
}

tasks.bootJar {
    exclude("**/sample/**", "**/FrameworkApplication*")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

buildScan {
    termsOfServiceUrl = "https://gradle.com/terms-of-service"
    termsOfServiceAgree = "yes"

    publishAlways()
}

tasks.test {
    systemProperty("spring.profiles.active", findProperty("profile") ?: "default")
}

allOpen {
    annotation("javax.persistence.Entity")
}
