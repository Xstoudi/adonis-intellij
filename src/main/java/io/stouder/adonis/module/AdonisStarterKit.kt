package io.stouder.adonis.module

import io.stouder.adonis.AdonisBundle

enum class AdonisStarterKit(
    val kitName: String,
    val hint: String,
    val repositoryUrl: String
) {
    SLIM("Slim", AdonisBundle.message("adonis.project.generator.template.slim.description"), "github:adonisjs/slim-starter-kit"),
    WEB("Web", AdonisBundle.message("adonis.project.generator.template.web.description"), "github:adonisjs/web-starter-kit"),
    API("API", AdonisBundle.message("adonis.project.generator.template.api.description"), "github:adonisjs/api-starter-kit");

    companion object {
        fun fromName(name: String): AdonisStarterKit? {
            return entries.firstOrNull { it.kitName == name }
        }
    }
}
