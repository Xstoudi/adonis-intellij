package io.stouder.adonis.activity

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import io.stouder.adonis.notifier.AdonisRouteUpdateNotifier
import io.stouder.adonis.service.AdonisRouteService

class InitRoutesActivity : ProjectActivity {
    override suspend fun execute(project: Project) {
        val publisher = project.messageBus.syncPublisher(AdonisRouteUpdateNotifier.ADONIS_ROUTES_UPDATE_TOPIC)
        AdonisRouteService.getInstance(project).fetchRoutes(publisher::routes)
    }
}