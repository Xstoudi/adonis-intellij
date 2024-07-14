package io.stouder.adonis.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import io.stouder.adonis.AdonisBundle
import io.stouder.adonis.cli.json.routes.RouteDomain
import io.stouder.adonis.notifier.AdonisRouteUpdateNotifier
import io.stouder.adonis.service.AdonisAceService
import java.util.*

class RefreshRoutesAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project
        assert(project != null)

        e.presentation.isEnabled = false

        ApplicationManager.getApplication().executeOnPooledThread {
            val routeDomainsFuture = project?.let {
                AdonisAceService.getInstance(it).runAceGetCommandOnEveryRoots(
                    AdonisBundle.message("adonis.actions.refresh.routes"),
                    listOf("list:routes", "--json"),
                    Array<RouteDomain>::class.java
                )
            }

            ApplicationManager.getApplication().invokeLater {
                val routeDomains = routeDomainsFuture
                project
                    ?.messageBus
                    ?.syncPublisher(AdonisRouteUpdateNotifier.ADONIS_ROUTES_UPDATE_TOPIC)
                    ?.routes(routeDomains)
                e.presentation.isEnabled = true
            }
        }
    }
}