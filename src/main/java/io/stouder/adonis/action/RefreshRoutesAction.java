package io.stouder.adonis.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import io.stouder.adonis.cli.json.routes.RouteDomain;
import io.stouder.adonis.notifier.AdonisRcUpdateNotifier;
import io.stouder.adonis.notifier.AdonisRouteUpdateNotifier;
import io.stouder.adonis.service.AdonisAceService;
import io.stouder.adonis.service.AdonisRouteService;
import org.jetbrains.annotations.NotNull;

public class RefreshRoutesAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        assert project != null;

        e.getPresentation().setEnabled(false);
        AdonisRouteUpdateNotifier publisher = project.getMessageBus().syncPublisher(AdonisRouteUpdateNotifier.ADONIS_ROUTES_UPDATE_TOPIC);
        RouteDomain[] routeDomains = AdonisAceService.getInstance(project).runAceCommand(
                RouteDomain[].class,
                "Refresh Adonis routes",
                "list:routes", "--json"
        );
        publisher.routes(routeDomains);
        e.getPresentation().setEnabled(true);
    }
}
