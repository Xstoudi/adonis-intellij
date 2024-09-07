package io.stouder.adonis.service

import com.intellij.openapi.project.Project

interface AdonisAppService {
    companion object {
        fun getInstance(project: Project): AdonisAppService {
            return project.getService(AdonisAppService::class.java)
        }
    }

    fun isAdonisProject(): Boolean

    fun getAdonisRoots(): List<String>
}