import com.github.jengelman.gradle.plugins.shadow.tasks.*
import com.google.protobuf.gradle.*
import org.jetbrains.kotlin.gradle.tasks.*

val logbackVersion = "1.4.5"

val grpcVersion = "1.57.2"
val grpcKotlinVersion = "1.3.1"
val protobufVersion = "3.23.4"
val platformVersion = "1.8.8"
val opentelemetryVersion = "1.22.0"
val testcontainersVersion = "1.19.1"
val ktorVersion = "2.2.2"

plugins {
    application
    idea
    kotlin("jvm") version "1.8.0"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("org.jmailen.kotlinter") version "3.2.0"
    id("io.spring.dependency-management") version "1.0.7.RELEASE"
    id("com.google.protobuf") version "0.8.19"
}

application {
    mainClass.set("com.buildingblock.BuildingBlockApplicationKt")
    group = "buildingblock"
}

sourceSets {
    main {
        java.srcDirs("src_java")
        kotlin.srcDirs("src")
        resources.srcDirs("resources")

        proto {
            srcDir("protos")

            val includeDirs = listOf(
                "partstech/common",
                "partstech/processing",
            )
            exclude {
                val path = it.file.path

                !path.endsWith("partstech") && includeDirs.none { dir -> path.contains(dir) }
            }
        }
    }
    test {
        kotlin.srcDirs("test")
        resources.srcDirs("testresources")
    }
}

idea {
    module {
        generatedSourceDirs = setOf(
            file("build/extracted-include-protos/main"),
            file("build/extracted-protos/main"),
            file("build/generated/source/proto/main/grpckt"),
            file("build/generated/source/proto/main/kotlin"),
        )
        isDownloadSources = true
    }
}

repositories {
    mavenCentral()
    maven("https://repository.aspose.com/repo/")
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/partstech/platform")
        credentials {
            username = project.findProperty("gpr.user")?.toString() ?: System.getenv("GITHUB_ACTOR")
            password = project.findProperty("gpr.key")?.toString() ?: System.getenv("GITHUB_TOKEN")
        }
    }
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation("com.partstech:platform-grpc:$platformVersion")
    implementation("io.grpc:grpc-protobuf:$grpcVersion")
    implementation("io.grpc:grpc-netty:$grpcVersion")
    implementation("io.grpc:grpc-stub:$grpcVersion")
    implementation("io.grpc:grpc-services:$grpcVersion")
    implementation("io.grpc:grpc-kotlin-stub:$grpcKotlinVersion")
    implementation("com.google.protobuf:protobuf-java-util:$protobufVersion")
    implementation("com.google.protobuf:protobuf-kotlin:$protobufVersion")
    api("io.opentelemetry:opentelemetry-sdk:$opentelemetryVersion")
    api("io.opentelemetry:opentelemetry-exporter-otlp:$opentelemetryVersion")
    implementation(kotlin("reflect"))
    implementation("org.springframework:spring-web:5.0.6.RELEASE")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("net.logstash.logback:logstash-logback-encoder:6.6")
    implementation("io.sentry:sentry-logback:1.7.5")
    implementation("org.litote.kmongo:kmongo:4.7.1")
    implementation("org.litote.kmongo:kmongo-coroutine:4.7.1")
    implementation("io.lettuce:lettuce-core:6.2.2.RELEASE")
    implementation("org.jdbi:jdbi3-kotlin-sqlobject:3.36.0")
    implementation("com.zaxxer:HikariCP:3.3.1")
    implementation("com.rabbitmq:amqp-client:5.16.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.0")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-smile:2.15.2")
    implementation("com.typesafe:config:1.4.2")
    implementation("commons-io:commons-io:2.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("com.opencsv:opencsv:4.5")
    implementation("com.aspose:aspose-cells:19.2")
    implementation("software.amazon.awssdk:sts:2.19.17")
    implementation("software.amazon.awssdk:s3:2.19.17")
    implementation("software.amazon.awssdk.crt:aws-crt:0.21.1")
    implementation("software.amazon.awssdk:s3-transfer-manager:2.19.17")
    implementation("javax.xml.bind:jaxb-api:2.3.1")
    implementation("co.elastic.clients:elasticsearch-java:7.17.10")
    implementation("org.eclipse.persistence:org.eclipse.persistence.moxy:2.7.4")
    implementation("net.gpedro.integrations.slack:slack-webhook:1.4.0")
    implementation("net.sf.sevenzipjbinding:sevenzipjbinding:16.02-2.01")
    implementation("net.sf.sevenzipjbinding:sevenzipjbinding-all-platforms:16.02-2.01")
    implementation("org.apache.poi:poi-ooxml:4.1.2")
    implementation("com.monitorjbl:xlsx-streamer:2.2.0")
    implementation("org.im4java:im4java:1.4.0")
    implementation("org.apache.tika:tika-core:2.6.0")
    implementation("org.kodein.di:kodein-di:7.20.1")
    implementation("commons-net:commons-net:3.9.0")
    implementation("org.apache.commons:commons-compress:1.22")
    implementation("org.apache.commons:commons-collections4:4.4")
    implementation("org.apache.xmlgraphics:fop:2.9")
    implementation("io.ktor:ktor-server-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-server-metrics-micrometer:$ktorVersion")
    implementation("io.ktor:ktor-client-plugins:$ktorVersion")
    implementation("io.micrometer:micrometer-registry-prometheus:1.12.5")
    implementation("com.beust:jcommander:1.82")
    implementation("io.seruco.encoding:base62:0.1.3")
    implementation("net.snowflake:snowflake-jdbc:3.13.3")
    runtimeOnly("mysql:mysql-connector-java:8.0.31")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.2")
    testImplementation(platform("org.junit:junit-bom:5.9.2"))
    testImplementation("io.ktor:ktor-client-mock:$ktorVersion")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher") {
        because("Only needed to run tests in a version of IntelliJ IDEA that bundles older versions")
    }
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine")
    implementation("com.github.ben-manes.caffeine:caffeine:3.1.2")
    testImplementation("org.testcontainers:testcontainers:$testcontainersVersion")
    testImplementation("org.testcontainers:junit-jupiter:$testcontainersVersion")
    testImplementation("org.testcontainers:mariadb:$testcontainersVersion")
    testImplementation("org.testcontainers:rabbitmq:$testcontainersVersion")
    testImplementation("org.testcontainers:mongodb:$testcontainersVersion")
    testImplementation("org.assertj:assertj-core:3.24.2")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0")
    testImplementation("com.h2database:h2:2.0.206")
    testImplementation("org.jdbi:jdbi3-testing:3.36.0")
    testImplementation("org.flywaydb:flyway-core:9.11.0")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:$protobufVersion"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:$grpcVersion"
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:$grpcKotlinVersion:jdk8@jar"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                id("grpc")
                id("grpckt")
            }
            it.builtins {
                id("kotlin")
            }
        }
    }
}
tasks {
    formatKotlinMain {
        exclude { it.file.path.contains("generated/") }
    }

    lintKotlinMain {
        exclude { it.file.path.contains("generated/") }
    }
    withType<KotlinCompile>().all {
        kotlinOptions.jvmTarget = "17"
        kotlinOptions.freeCompilerArgs += "-Xjsr305=strict"
        kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
        kotlinOptions.freeCompilerArgs += "-opt-in=kotlinx.coroutines.FlowPreview"
    }
    withType<ShadowJar> {
        archiveBaseName.set("buildingblock")
        archiveClassifier.set("")
        archiveVersion.set("")
        isZip64 = true
    }
    test {
        jvmArgs = listOf("-Dkotlinx.coroutines.debug=off")
        useJUnitPlatform {
            excludeTags("testcontainer")
        }
        testLogging {
            events("passed", "skipped", "failed")
        }
    }
    val testContainer by registering(Test::class) {
        jvmArgs = listOf("-Dkotlinx.coroutines.debug=off")
        useJUnitPlatform {
            includeTags("testcontainer")
        }
        testLogging {
            events("passed", "skipped", "failed")
        }
        maxParallelForks = 2
    }
    named("check") {
        dependsOn(testContainer)
    }
}

kotlinter {
    ignoreFailures = false
    indentSize = 4
    reporters = arrayOf("checkstyle", "plain")
    experimentalRules = false
    disabledRules = arrayOf("no-wildcard-imports", "indent", "final-newline")
}
