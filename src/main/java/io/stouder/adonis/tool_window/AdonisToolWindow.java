package io.stouder.adonis.tool_window;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.table.JBTable;
import io.stouder.adonis.cli.json.routes.RouteDomain;
import io.stouder.adonis.notifier.AdonisRouteUpdateNotifier;
import io.stouder.adonis.service.AdonisAppService;
import io.stouder.adonis.model.RoutesTableModel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.List;

public class AdonisToolWindow implements ToolWindowFactory {

    @Override
    public boolean isApplicable(@NotNull Project project) {
        return AdonisAppService.getInstance(project).isAdonisProject();
    }

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        AdonisRoutesToolWindowContent toolWindowContent = new AdonisRoutesToolWindowContent(toolWindow, project);
        Content content = ContentFactory.getInstance().createContent(toolWindowContent.rootPanel, "Routes", false);
        toolWindow.getContentManager().addContent(content);
    }

    @Override
    public void init(@NotNull ToolWindow toolWindow) {
        ToolWindowFactory.super.init(toolWindow);
    }

    @Override
    public boolean shouldBeAvailable(@NotNull Project project) {
        return ToolWindowFactory.super.shouldBeAvailable(project);
    }

    private static class AdonisRoutesToolWindowContent implements AdonisRouteUpdateNotifier {

        private final JPanel rootPanel;
        private final JBScrollPane scrollPane;
        private final JTable routesTable;

        private final GridLayout gridLayout = new GridLayout(1, 1);

        private AdonisRoutesToolWindowContent(ToolWindow toolWindow, Project project) {
            this.rootPanel = new JPanel();
            this.routesTable = new JBTable();
            this.scrollPane = new JBScrollPane();

            this.buildUi();

            project.getMessageBus().connect().subscribe(AdonisRouteUpdateNotifier.ADONIS_ROUTES_UPDATE_TOPIC, this);
        }


        private void buildUi() {
            this.rootPanel.setLayout(new BorderLayout());

            this.buildToolbar();
            this.buildTable();
        }

        private void buildToolbar() {
            ActionGroup actionGroup = (ActionGroup) ActionManager.getInstance().getAction("io.stouder.adonis.RoutesToolbar");
            ActionToolbar toolbar = ActionManager.getInstance().createActionToolbar(
                    "AdonisRoutesToolbar",
                    actionGroup,
                    true
            );

            toolbar.setTargetComponent(this.rootPanel);
            this.rootPanel.add(toolbar.getComponent(), BorderLayout.NORTH);
        }

        private void buildTable() {
            TableModel model = new RoutesTableModel(List.of());

            this.routesTable.setModel(model);
            this.routesTable.setRowHeight(30);
            this.scrollPane.setViewportView(this.routesTable);

            this.rootPanel.add(this.scrollPane, BorderLayout.CENTER);
        }

        @Override
        public void routes(RouteDomain[] routes) {
            if (routes == null) return;
            TableModel model = new RoutesTableModel(List.of(routes));
            this.routesTable.setModel(model);
        }
    }
}
