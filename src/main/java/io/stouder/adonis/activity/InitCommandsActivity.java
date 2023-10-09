package io.stouder.adonis.activity;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import io.stouder.adonis.notifier.AdonisRcUpdateNotifier;
import io.stouder.adonis.service.AdonisAceService;
import org.jetbrains.annotations.NotNull;

public class InitCommandsActivity implements StartupActivity {
    @Override
    public void runActivity(@NotNull Project project) {
        AdonisRcUpdateNotifier publisher = project.getMessageBus().syncPublisher(AdonisRcUpdateNotifier.ADONIS_RC_UPDATE_TOPIC);
        AdonisAceService.getInstance(project).fetchCommands(publisher::commands);
    }
}
