package io.stouder.adonis.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import io.stouder.adonis.AdonisBundle;
import io.stouder.adonis.cli.json.routes.RouteDomain;
import io.stouder.adonis.notifier.AdonisRouteUpdateNotifier;
import io.stouder.adonis.service.AdonisAceService;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RefreshRoutesAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        assert project != null;

        e.getPresentation().setEnabled(false);
        AdonisRouteUpdateNotifier publisher = project.getMessageBus().syncPublisher(AdonisRouteUpdateNotifier.ADONIS_ROUTES_UPDATE_TOPIC);
        RouteDomain[] routeDomains = AdonisAceService.getInstance(project).runAceCommand(
                AdonisBundle.message("adonis.actions.refresh.routes"), List.of("list:routes", "--json"), RouteDomain[].class
        );
        publisher.routes(routeDomains);
        e.getPresentation().setEnabled(true);
    }
}
