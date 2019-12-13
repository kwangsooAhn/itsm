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
    id("org.jlleitschuh.gradle.ktlint") version "9.1.0"
    id("io.gitlab.arturbosch.detekt").version("1.1.1")
    jacoco
}

group = "co.brainz"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

val developmentOnly: Configuration by configurations.creating
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

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-rest")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-log4j2")
    implementation("org.bgee.log4jdbc-log4j2:log4jdbc-log4j2-jdbc4.1:1.16")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.boot:spring-boot-devtools")
    implementation("net.rakugakibox.util:yaml-resource-bundle:1.1")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("org.apache.logging.log4j:log4j-web:2.7")
    implementation("com.github.ulisesbocchio:jasypt-spring-boot-starter:2.1.2")
    implementation("commons-codec:commons-codec:1.13")
    implementation("org.apache.tika:tika-core:1.22")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity5") 
}

// KTLINT
ktlint {
    version.set("0.35.0")
    debug.set(true)
    verbose.set(true)
    android.set(false)
    outputToConsole.set(true)
    outputColorName.set("RED")
    ignoreFailures.set(true)
    reporters {
        reporter(ReporterType.PLAIN)
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
        html.destination = file("${buildDir}/jacocoHtml")
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

// JAR
tasks.jar {
    enabled = true
    exclude("**/sample/**", "**/FrameworkApplication*")
    exclude("public/", "static/", "*.properties", "*.xml")
}

tasks.bootJar {
    classifier = "boot"
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