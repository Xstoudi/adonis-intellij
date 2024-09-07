package io.stouder.adonis.module

import com.intellij.lang.javascript.boilerplate.NpmPackageProjectGenerator
import com.intellij.openapi.util.Key

object AdonisSettings {
    val STARTER_KIT: Key<String> = Key.create("ADONIS_STARTER_KIT")
    val INITIALIZE_GIT: Key<Boolean> = Key.create("ADONIS_INITIALIZE_GIT")

    fun extractStarterKit(settings: NpmPackageProjectGenerator.Settings): String {
        val starterKit = settings.getUserData(STARTER_KIT)
        return starterKit ?: AdonisStarterKit.SLIM.repositoryUrl
    }

    fun extractInitializeGit(settings: NpmPackageProjectGenerator.Settings): Boolean {
        val initializeGit = settings.getUserData(INITIALIZE_GIT)
        return initializeGit ?: true
    }
}
