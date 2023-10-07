package io.stouder.adonis.ui;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.table.JBTable;
import io.stouder.adonis.AdonisAppService;
import io.stouder.adonis.actions.RefreshRoutesAction;
import io.stouder.adonis.edge.textmate.TextmateService;
import io.stouder.adonis.ui.models.RoutesTableModel;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import java.awt.*;

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

    @Override
    public boolean isDoNotActivateOnStart() {
        return ToolWindowFactory.super.isDoNotActivateOnStart();
    }

    @Override
    public @Nullable ToolWindowAnchor getAnchor() {
        return ToolWindowFactory.super.getAnchor();
    }

    @Override
    public @Nullable Icon getIcon() {
        return ToolWindowFactory.super.getIcon();
    }

    private static class AdonisRoutesToolWindowContent {

        private final JPanel rootPanel = new JPanel();
        private final JTable routesTable = new JBTable();
        private ActionToolbar toolbar;

        private final Project project;

        private AdonisRoutesToolWindowContent(ToolWindow toolWindow, Project project) {
            this.project = project;

            this.build();
        }

        private void build() {
            this.buildTable();
        }

        private void buildToolbar() {
            this.toolbar.setTargetComponent(rootPanel);
        }

        private void buildTable() {
            TableModel model = new RoutesTableModel(
                    AdonisAppService.getInstance(this.project).getRoutes()
            );
            this.routesTable.setModel(model);
            this.routesTable.setRowHeight(30);
            this.rootPanel.add(new JLabel("Routes"));
            this.rootPanel.add(this.routesTable);
        }
    }
}
