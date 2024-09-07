package io.stouder.adonis.activity

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import io.stouder.adonis.notifier.AdonisRcUpdateNotifier
import io.stouder.adonis.service.AdonisAceService

class InitCommandsActivity : ProjectActivity {
    override suspend fun execute(project: Project) {
        val publisher = project.messageBus.syncPublisher(AdonisRcUpdateNotifier.ADONIS_RC_UPDATE_TOPIC)
        AdonisAceService.getInstance(project).fetchCommands(publisher::commands)
    }
}
