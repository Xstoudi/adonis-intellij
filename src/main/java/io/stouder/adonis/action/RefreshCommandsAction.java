package io.stouder.adonis.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import io.stouder.adonis.AdonisBundle;
import io.stouder.adonis.cli.json.ace.Command;
import io.stouder.adonis.notifier.AdonisRcUpdateNotifier;
import io.stouder.adonis.service.AdonisAceService;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RefreshCommandsAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        assert project != null;

        e.getPresentation().setEnabled(false);
        AdonisRcUpdateNotifier publisher = project.getMessageBus().syncPublisher(AdonisRcUpdateNotifier.ADONIS_RC_UPDATE_TOPIC);
        Map<String, Optional<Command[]>> commands = AdonisAceService.getInstance(project).runAceGetCommandOnEveryRoots(
                AdonisBundle.message("adonis.actions.refresh.commands"),
                List.of("list", "--json"),
                Command[].class
        );
        publisher.commands(commands);
        e.getPresentation().setEnabled(true);
    }
}
