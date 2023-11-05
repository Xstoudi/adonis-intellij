package io.stouder.adonis.tool_window.content;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.components.JBTabbedPane;
import io.stouder.adonis.cli.json.ace.Command;
import io.stouder.adonis.notifier.AdonisRcUpdateNotifier;
import io.stouder.adonis.tool_window.content.tabs.MakeCommandTab;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class MakeToolWindowContent implements AdonisRcUpdateNotifier {
    private final JPanel rootPanel = new JPanel();
    private final JBTabbedPane tabbedPane = new JBTabbedPane();
    private List<Command> makeCommands = new ArrayList<>();
    private final Project project;


    public MakeToolWindowContent(Project project) {
        this.project = project;
        this.buildUi();
        this.buildTabs();
    }

    public JComponent getRootPanel() {
        return this.rootPanel;
    }

    private void buildTabs() {
        while(this.tabbedPane.getTabCount() > 0) {
            this.tabbedPane.removeTabAt(0);
        }
        this.makeCommands.forEach(command -> {
            String commandNameWithoutNamespace = StringUtil.capitalize(
                    command.getCommandName().replace("make:", "")
            );
            this.tabbedPane.addTab(commandNameWithoutNamespace, new MakeCommandTab(command, project));
        });
    }

    private void buildUi() {
        this.rootPanel.setLayout(new BorderLayout());
        this.rootPanel.add(this.tabbedPane);
    }

    @Override
    public void commands(Command[] commands) {
        if (commands == null) return;
        this.makeCommands = Stream.of(commands)
                .filter(command -> "make".equals(command.getNamespace()))
                .toList();
        ApplicationManager.getApplication().invokeLater(this::buildTabs);
    }
}

