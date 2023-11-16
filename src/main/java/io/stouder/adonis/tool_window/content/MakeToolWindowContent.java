package io.stouder.adonis.tool_window.content;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.components.JBTabbedPane;
import io.stouder.adonis.cli.json.ace.Command;
import io.stouder.adonis.notifier.AdonisRcUpdateNotifier;
import io.stouder.adonis.tool_window.content.tabs.MakeCommandTab;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MakeToolWindowContent implements AdonisRcUpdateNotifier {
    private final JPanel rootPanel = new JPanel();
    private final ComboBox<String> comboBox = new ComboBox<>();
    private final Map<String, JBTabbedPane> tabbedPane = new HashMap<>();
    private Map<String, List<Command>> makeCommands = new HashMap<>();
    private String selectedModule;
    private final Project project;


    public MakeToolWindowContent(Project project) {
        this.project = project;

        this.rootPanel.setLayout(new BorderLayout());
        this.comboBox.addActionListener(e -> {
            this.rootPanel.remove(this.tabbedPane.get(this.selectedModule));
            this.selectedModule = (String) comboBox.getSelectedItem();
            this.updateUi();
        });

        this.updateUi();
    }

    public JComponent getRootPanel() {
        return this.rootPanel;
    }

    private void buildTabs() {
        for (String module : this.makeCommands.keySet()) {
            JBTabbedPane tabbedPane = this.tabbedPane.get(module);
            if(tabbedPane == null) {
                tabbedPane = new JBTabbedPane();
                this.tabbedPane.put(module, tabbedPane);
            }

            while(tabbedPane.getTabCount() > 0) {
                tabbedPane.removeTabAt(0);
            }

            for(Command command : this.makeCommands.get(module)) {
                String commandNameWithoutNamespace = StringUtil.capitalize(
                        command.getCommandName().replace("make:", "")
                );
                tabbedPane.addTab(commandNameWithoutNamespace, new MakeCommandTab(command, project));
            }
        }
    }

    private void updateUi() {
        this.buildTabs();

        this.comboBox.setModel(
                new DefaultComboBoxModel<>(this.makeCommands.keySet().toArray(new String[0]))
        );
        if(this.selectedModule != null) {
            comboBox.setSelectedItem(this.selectedModule);
        }
        this.rootPanel.remove(comboBox);
        this.rootPanel.add(comboBox, BorderLayout.NORTH);

        JBTabbedPane tabbedPane = this.tabbedPane.get(this.selectedModule);
        if(tabbedPane != null) {
            this.rootPanel.add(tabbedPane, BorderLayout.CENTER);
        }
    }

    @Override
    public void commands(Map<String, Optional<Command[]>> commands) {
        if (commands == null) return;
        this.makeCommands = commands.entrySet()
                .stream()
                .filter(entry -> entry.getValue().isPresent())
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> Stream.of(entry.getValue().get())
                                        .filter(command -> "make".equals(command.getNamespace()))
                                        .toList()
                        )
                );
        this.selectedModule = this.selectedModule == null
                ? this.makeCommands.keySet().stream().findFirst().orElse(null)
                : this.selectedModule;
        ApplicationManager.getApplication().invokeLater(this::updateUi);
    }
}

