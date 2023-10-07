package io.stouder.adonis.edge.textmate;

import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManagerListener;
import io.stouder.adonis.AdonisAppService;
import io.stouder.adonis.cli.json.RouteDomain;

import java.util.List;

public class TextmateCheckListener implements ProjectManagerListener {

    private static final Logger LOG = Logger.getInstance(TextmateCheckListener.class);

    public TextmateService textmateService;

    public TextmateCheckListener(Project project) {
        this.textmateService = TextmateService.getInstance(project);
    }

    @Override
    public void projectOpened(Project project) {
        boolean isTextmatePluginPresent = PluginManager.getLoadedPlugins()
                .stream()
                .anyMatch(plugin -> plugin.getPluginId().getIdString().equals("org.jetbrains.plugins.textmate"));
        if (isTextmatePluginPresent) {
            this.textmateService.ensureEdgeIsInstalled();
        } else {
            LOG.warn("Textmate plugin is not present, Edge.js syntax highlighting will not work.");
        }
    }
}
