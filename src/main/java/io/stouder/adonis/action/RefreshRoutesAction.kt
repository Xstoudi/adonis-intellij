package io.stouder.adonis.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import io.stouder.adonis.AdonisBundle
import io.stouder.adonis.cli.json.routes.RouteDomain
import io.stouder.adonis.notifier.AdonisRouteUpdateNotifier
import io.stouder.adonis.service.AdonisAceService

class RefreshRoutesAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project
        assert(project != null)

        e.presentation.isEnabled = false
        val routeDomains = project?.let {
            AdonisAceService.getInstance(it).runAceGetCommandOnEveryRoots(
                AdonisBundle.message("adonis.actions.refresh.routes"),
                listOf("list:routes", "--json"),
                Array<RouteDomain>::class.java
            )
        }
        project
            ?.messageBus
            ?.syncPublisher(AdonisRouteUpdateNotifier.ADONIS_ROUTES_UPDATE_TOPIC)
            ?.routes(routeDomains)
        e.presentation.isEnabled = true
    }
}
