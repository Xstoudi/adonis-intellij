package io.stouder.adonis.tool_window.content;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import io.stouder.adonis.cli.json.routes.RouteDomain;
import io.stouder.adonis.model.RoutesTableModel;
import io.stouder.adonis.notifier.AdonisRouteUpdateNotifier;
import lombok.Getter;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class RoutesToolWindowContent implements AdonisRouteUpdateNotifier {

    @Getter
    private final JPanel rootPanel = new JPanel();
    private final JBScrollPane scrollPane = new JBScrollPane();
    private final JTable routesTable = new JBTable();

    public RoutesToolWindowContent(Project project) {
        this.buildUi();
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
        TableModel model = new RoutesTableModel(Map.of());

        this.routesTable.setModel(model);
        this.routesTable.setRowHeight(30);
        this.scrollPane.setViewportView(this.routesTable);

        this.rootPanel.add(this.scrollPane, BorderLayout.CENTER);
    }

    @Override
    public void routes(Map<String, Optional<RouteDomain[]>> routes) {
        if (routes == null) return;
        TableModel model = new RoutesTableModel(
                routes
                        .entrySet()
                        .stream()
                        .filter(entry -> entry.getValue().isPresent())
                        .collect(
                                Collectors.toMap(
                                        Map.Entry::getKey,
                                        entry -> List.of(entry.getValue().get())
                                )
                        )
        );
        this.routesTable.setModel(model);
    }
}