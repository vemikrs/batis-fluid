plugins {
    id("java-library")
    id("com.vanniktech.maven.publish")
}

group = "jp.vemi"
version = "0.0.2"

sourceSets {
    named("main") {
        java.srcDirs("src/main/java")
        resources.srcDirs("src/main/resources")
    }
    named("test") {
        java.srcDirs("src/test/java")
        resources.srcDirs("src/test/resources")
    }
}

repositories { mavenCentral() }

dependencies {
    api(project(":lib"))
    api("org.springframework.boot:spring-boot-autoconfigure:3.5.8")
    api("org.springframework:spring-context:6.2.12")
    api("org.springframework:spring-beans:6.2.12")
    api("org.mybatis:mybatis-spring:3.0.5")

    // Align Spring 6.x
    implementation("org.springframework:spring-tx:6.2.12")
    implementation("org.springframework:spring-jdbc:6.2.12")

    testImplementation("org.springframework.boot:spring-boot-starter-test:3.5.8")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.12.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.12.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("com.h2database:h2:2.4.240")
}

java {
    toolchain { languageVersion.set(JavaLanguageVersion.of(21)) }
    withSourcesJar()
    withJavadocJar()
}

tasks.named<org.gradle.jvm.tasks.Jar>("jar").configure {
    // Maven Central 上のアーティファクト名と揃えるため、JAR のベース名を明示します。
    archiveBaseName.set("batis-fluid-spring")
}

tasks.named<Test>("test").configure {
    useJUnitPlatform {
        val prop = System.getProperty("junitTags") ?: project.findProperty("junitTags")?.toString()
        if (!prop.isNullOrBlank()) {
            val tags = prop.split(',').map { it.trim() }.filter { it.isNotEmpty() }
            if (tags.isNotEmpty()) includeTags(*tags.toTypedArray())
        }
    }
    // Configure to not fail when no tests are discovered (Gradle 9+)
    failOnNoDiscoveredTests = false
    filter { isFailOnNoMatchingTests = false }
}

tasks.withType<Javadoc>().configureEach {
    val opts = options as? CoreJavadocOptions
    opts?.addStringOption("Xdoclint:none", "-quiet")
    opts?.addStringOption("Xmaxwarns", "1")
    opts?.addBooleanOption("allow-script-in-comments", true)
    isFailOnError = false
}

mavenPublishing {
    publishToMavenCentral(com.vanniktech.maven.publish.SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()
    coordinates("jp.vemi", "batis-fluid-spring", version.toString())
    pom {
        name.set("BatisFluid Spring Integration")
        description.set("Spring Framework integration module for BatisFluid")
        url.set("https://github.com/vemikrs/batis-fluid")
        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
        developers {
            developer {
                id.set("vemikrs")
                name.set("Hiroki Kurosawa")
                email.set("contact@vemi.jp")
            }
        }
        scm {
            connection.set("scm:git:git://github.com/vemikrs/batis-fluid.git")
            developerConnection.set("scm:git:ssh://git@github.com/vemikrs/batis-fluid.git")
            url.set("https://github.com/vemikrs/batis-fluid")
        }
    }
}

// Duplicate resources handling (Gradle 9 requires explicit strategy on duplicates)
tasks.named<org.gradle.language.jvm.tasks.ProcessResources>("processResources").configure {
    duplicatesStrategy = org.gradle.api.file.DuplicatesStrategy.EXCLUDE
}

// Configure sourcesJar task to handle duplicates
tasks.named<org.gradle.jvm.tasks.Jar>("sourcesJar").configure {
    duplicatesStrategy = org.gradle.api.file.DuplicatesStrategy.EXCLUDE
}

// Vanniktech Maven Publish プラグインが生成するプレーン Javadoc JAR と重複しないように、
// mavenPlainJavadocJar タスクを無効化して、単一の Javadoc JAR のみを公開します。
tasks.withType<org.gradle.jvm.tasks.Jar>().configureEach {
    if (name == "mavenPlainJavadocJar") {
        enabled = false
    }
}
