package io.stouder.adonis.service.impl

import com.intellij.openapi.project.Project
import io.stouder.adonis.cli.json.routes.RouteDomain
import io.stouder.adonis.service.AdonisAceService
import io.stouder.adonis.service.AdonisRouteService
import java.util.*

class AdonisRouteServiceImpl(private val project: Project) : AdonisRouteService {

    override fun fetchRoutes(callback: (Map<String, Optional<Array<RouteDomain>>>) -> Unit) {
        val adonisAceService = AdonisAceService.getInstance(project)
        adonisAceService.runAceGetCommandAsyncOnEveryRoots(callback, listOf("list:routes", "--json"), Array<RouteDomain>::class.java)
    }
}