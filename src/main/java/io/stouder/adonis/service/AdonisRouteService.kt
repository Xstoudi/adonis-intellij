package io.stouder.adonis.service

import com.intellij.openapi.project.Project
import io.stouder.adonis.cli.json.routes.RouteDomain
import java.util.*

interface AdonisRouteService {
    companion object {
        fun getInstance(project: Project): AdonisRouteService {
            return project.getService(AdonisRouteService::class.java)
        }
    }

    fun fetchRoutes(callback: (Map<String, Optional<Array<RouteDomain>>>) -> Unit)
}