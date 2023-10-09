package io.stouder.adonis.listener;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.newvfs.BulkFileListener;
import com.intellij.openapi.vfs.newvfs.events.VFileContentChangeEvent;
import com.intellij.openapi.vfs.newvfs.events.VFileEvent;
import io.stouder.adonis.notifier.AdonisRcUpdateNotifier;
import io.stouder.adonis.notifier.AdonisRouteUpdateNotifier;
import io.stouder.adonis.service.AdonisAceService;
import io.stouder.adonis.service.AdonisRouteService;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdonisBulkFileListener implements BulkFileListener {

    private final Project project;
    private final AdonisAceService adonisAceService;
    private final AdonisRouteService adonisRouteService;

    public AdonisBulkFileListener(Project project) {
        this.project = project;
        this.adonisAceService = AdonisAceService.getInstance(project);
        this.adonisRouteService = AdonisRouteService.getInstance(project);
    }

    @Override
    public void after(@NotNull List<? extends @NotNull VFileEvent> events) {
        for (VFileEvent event : events) {
            if(event instanceof VFileContentChangeEvent changeEvent) {
                switch(changeEvent.getFile().getName()) {
                    case "adonisrc.ts":
                        {
                            AdonisRcUpdateNotifier publisher = this.project.getMessageBus().syncPublisher(AdonisRcUpdateNotifier.ADONIS_RC_UPDATE_TOPIC);
                            this.adonisAceService.fetchCommands(publisher::commands);
                        }
                        return;
                    case "routes.ts":
                        {
                            AdonisRouteUpdateNotifier publisher = this.project.getMessageBus().syncPublisher(AdonisRouteUpdateNotifier.ADONIS_ROUTES_UPDATE_TOPIC);
                            this.adonisRouteService.fetchRoutes(publisher::routes);
                        }
                        return;
                }

            }

        }
    }
}
