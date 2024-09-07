package io.stouder.adonis.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import io.stouder.adonis.AdonisBundle
import io.stouder.adonis.cli.json.ace.Command
import io.stouder.adonis.notifier.AdonisRcUpdateNotifier
import io.stouder.adonis.service.AdonisAceService
import org.jetbrains.annotations.NotNull
import java.util.*

class RefreshCommandsAction : AnAction() {

    override fun actionPerformed(@NotNull e: AnActionEvent) {
        val project = e.project ?: return

        e.presentation.isEnabled = false
        val publisher = project.messageBus.syncPublisher(AdonisRcUpdateNotifier.ADONIS_RC_UPDATE_TOPIC)

        ApplicationManager.getApplication().executeOnPooledThread {
            val commands = AdonisAceService.getInstance(project).runAceGetCommandOnEveryRoots(
                AdonisBundle.message("adonis.actions.refresh.commands"),
                listOf("list", "--json"),
                Array<Command>::class.java
            )

            val safeCommands = commands as? Map<String, Optional<Array<Command>>>
            safeCommands?.let { publisher.commands(it) }
        }
    }
}