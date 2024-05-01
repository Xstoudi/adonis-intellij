package io.stouder.adonis.activity;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.ProjectActivity;
import io.stouder.adonis.notifier.AdonisRcUpdateNotifier;
import io.stouder.adonis.service.AdonisAceService;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import org.jetbrains.annotations.NotNull;

public class InitCommandsActivity implements ProjectActivity {
    @Override
    public Object execute(@NotNull Project project, @NotNull Continuation<? super Unit> continuation) {
        AdonisRcUpdateNotifier publisher = project.getMessageBus().syncPublisher(AdonisRcUpdateNotifier.ADONIS_RC_UPDATE_TOPIC);
        AdonisAceService.getInstance(project).fetchCommands(publisher::commands);
        return Unit.INSTANCE;
    }
}
