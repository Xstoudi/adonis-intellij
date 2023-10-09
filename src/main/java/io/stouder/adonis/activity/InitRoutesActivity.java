package io.stouder.adonis.activity;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import io.stouder.adonis.notifier.AdonisRouteUpdateNotifier;
import io.stouder.adonis.service.AdonisRouteService;
import org.jetbrains.annotations.NotNull;

public class InitRoutesActivity implements StartupActivity {
    @Override
    public void runActivity(@NotNull Project project) {
        AdonisRouteUpdateNotifier publisher = project.getMessageBus().syncPublisher(AdonisRouteUpdateNotifier.ADONIS_ROUTES_UPDATE_TOPIC);
        AdonisRouteService.getInstance(project).fetchRoutes(publisher::routes);
    }
}
