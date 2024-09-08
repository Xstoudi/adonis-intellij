import org.jetbrains.changelog.Changelog
import org.jetbrains.changelog.markdownToHTML
import org.jetbrains.intellij.platform.gradle.TestFrameworkType

fun environment(key: String) = providers.environmentVariable(key)

plugins {
  id("java")
  alias(libs.plugins.kotlin)
  alias(libs.plugins.intelliJPlatform)
  alias(libs.plugins.changelog)
  alias(libs.plugins.grammarkit)
  alias(libs.plugins.lombok)
}

group = providers.gradleProperty("pluginGroup").get()
version = providers.gradleProperty("pluginVersion").get()

kotlin {
  jvmToolchain(21)
}

repositories {
  mavenCentral()

  intellijPlatform {
    defaultRepositories()
  }
}

sourceSets["main"].java.srcDirs("src/main/gen")

dependencies {
  compileOnly("org.projectlombok:lombok:1.18.30")
  annotationProcessor("org.projectlombok:lombok:1.18.30")

  intellijPlatform {
    create(providers.gradleProperty("platformType"), providers.gradleProperty("platformVersion"))

    bundledPlugins(providers.gradleProperty("platformBundledPlugins").map { it.split(',') })

    plugins(providers.gradleProperty("platformPlugins").map { it.split(',') })

    instrumentationTools()
    pluginVerifier()
    zipSigner()
    testFramework(TestFrameworkType.Platform)
  }
}

intellijPlatform {
  pluginConfiguration {
    version = providers.gradleProperty("pluginVersion")

    description = providers.fileContents(layout.projectDirectory.file("README.md")).asText.map {
      val start = "<!-- Plugin description -->"
      val end = "<!-- Plugin description end -->"

      with(it.lines()) {
        if (!containsAll(listOf(start, end))) {
          throw GradleException("Plugin description section not found in README.md:\n$start ... $end")
        }
        subList(indexOf(start) + 1, indexOf(end)).joinToString("\n").let(::markdownToHTML)
      }
    }

    val changelog = project.changelog // local variable for configuration cache compatibility
    // Get the latest available change notes from the changelog file
    changeNotes = providers.gradleProperty("pluginVersion").map { pluginVersion ->
      with(changelog) {
        renderItem(
          (getOrNull(pluginVersion) ?: getUnreleased())
            .withHeader(false)
            .withEmptySections(false),
          Changelog.OutputType.HTML,
        )
      }
    }

    ideaVersion {
      sinceBuild = providers.gradleProperty("pluginSinceBuild")
      untilBuild = providers.gradleProperty("pluginUntilBuild")
    }
  }

  signing {
    certificateChain = providers.environmentVariable("CERTIFICATE_CHAIN")
    privateKey = providers.environmentVariable("PRIVATE_KEY")
    password = providers.environmentVariable("PRIVATE_KEY_PASSWORD")
  }

  publishing {
    token = providers.environmentVariable("PUBLISH_TOKEN")
    channels = providers.gradleProperty("pluginVersion").map { listOf(it.substringAfter('-', "").substringBefore('.').ifEmpty { "default" }) }
  }

  pluginVerification {
    ides {
      recommended()
    }
  }
}

changelog {
  groups.empty()
  repositoryUrl = providers.gradleProperty("pluginRepositoryUrl")
}


tasks {
  wrapper {
    gradleVersion = providers.gradleProperty("gradleVersion").get()
  }

  named("compileKotlin") {
    dependsOn(":generateLexer")
  }

  withType<JavaCompile> {
    dependsOn(generateLexer)
  }

  generateLexer {
    sourceFile.set(file("src/main/java/io/stouder/adonis/edge/parsing/edge.flex"))
    targetOutputDir.set(file("src/main/gen/io/stouder/adonis/edge/parsing"))
    purgeOldFiles.set(true)
  }

  publishPlugin {
    dependsOn("patchChangelog")
  }
}

tasks.withType<Jar>() {
  duplicatesStrategy = DuplicatesStrategy.WARN
}
