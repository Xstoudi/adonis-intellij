package io.stouder.adonis.activity;

import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import io.stouder.adonis.service.TextmateService;
import org.jetbrains.annotations.NotNull;

public class InitTextmateActivity implements StartupActivity {
    private static final Logger LOG = Logger.getInstance(InitTextmateActivity.class);

    @Override
    public void runActivity(@NotNull Project project) {
        TextmateService textmateService = TextmateService.getInstance(project);

        boolean isTextmatePluginPresent = PluginManager.getLoadedPlugins()
                .stream()
                .anyMatch(plugin -> plugin.getPluginId().getIdString().equals("org.jetbrains.plugins.textmate"));
        if (isTextmatePluginPresent) {
            textmateService.ensureEdgeIsInstalled();
        } else {
            LOG.warn("Textmate plugin is not present, Edge.js syntax highlighting will not work.");
        }
    }
}
