package io.stouder.adonis.service.impl

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.util.io.FileUtil
import io.stouder.adonis.service.AdonisAppService
import java.io.File

class AdonisAppServiceImpl(private val project: Project) : AdonisAppService {
    override fun isAdonisProject(): Boolean {
        return getAdonisRoots().any { isAdonisModule(it) }
    }

    override fun getAdonisRoots(): List<String> {
        val modulesRoots = ProjectRootManager.getInstance(project).contentRoots
        return modulesRoots.mapNotNull { it.canonicalPath }.filter { isAdonisModule(it) }
    }

    private fun isAdonisModule(basePath: String): Boolean {
        val adonisrcFilePath = FileUtil.toSystemIndependentName("$basePath/adonisrc.ts")
        val adonisrcFile = File(adonisrcFilePath)
        return adonisrcFile.exists()
    }
}