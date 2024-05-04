package io.stouder.adonis.listener

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileContentChangeEvent
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import io.stouder.adonis.notifier.AdonisRcUpdateNotifier
import io.stouder.adonis.notifier.AdonisRouteUpdateNotifier
import io.stouder.adonis.service.AdonisAceService
import io.stouder.adonis.service.AdonisRouteService

class AdonisBulkFileListener(private val project: Project) : BulkFileListener {

    private val adonisAceService: AdonisAceService = AdonisAceService.getInstance(project)
    private val adonisRouteService: AdonisRouteService = AdonisRouteService.getInstance(project)

    override fun after(events: MutableList<out VFileEvent>) {
        for (event in events) {
            if (event is VFileContentChangeEvent) {
                when (event.file.name) {
                    "adonisrc.ts" -> {
                        val publisher = project.messageBus.syncPublisher(AdonisRcUpdateNotifier.ADONIS_RC_UPDATE_TOPIC)
                        adonisAceService.fetchCommands(publisher::commands)
                    }
                    "routes.ts" -> {
                        val publisher = project.messageBus.syncPublisher(AdonisRouteUpdateNotifier.ADONIS_ROUTES_UPDATE_TOPIC)
                        adonisRouteService.fetchRoutes(publisher::routes)
                    }
                }
            }
        }
    }
}