package io.stouder.adonis.tool_window.content.tabs;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.ui.JBUI;
import io.stouder.adonis.AdonisBundle;
import io.stouder.adonis.cli.json.ace.Command;
import io.stouder.adonis.cli.json.ace.CommandArgument;
import io.stouder.adonis.cli.json.ace.CommandFlag;
import io.stouder.adonis.component.JListEditor;
import io.stouder.adonis.service.AdonisAceService;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MakeCommandTab extends JPanel {
    private final LinkedHashMap<CommandArgument, JTextField> arguments;
    private final LinkedHashMap<CommandFlag<?>, JComponent> flags;

    private final JButton createButton = new JButton(AdonisBundle.message("adonis.tool_window.content.tabs.make.create"));
    private final Project project;
    private final Command command;
    private final String selectedModule;

    public MakeCommandTab(Command command, Project project, String selectedModule) {
        this.command = command;
        this.project = project;
        this.selectedModule = selectedModule;
        this.arguments = command.getArgs()
                .stream()
                .map(arg -> new AbstractMap.SimpleEntry<>(
                        arg,
                        new JTextField()
                ))
                .collect(LinkedHashMap::new, (m, v) -> m.put(v.getKey(), v.getValue()), LinkedHashMap::putAll);
        this.flags = command.getFlags()
                .stream()
                .map(flag -> {
                    JComponent inputComponent = switch (flag.getType()) {
                        case "boolean" -> new JCheckBox();
                        case "number" -> new JFormattedTextField(this.getNumberFormatter());
                        case "array" -> new JListEditor();
                        case "numArray" -> new JListEditor(this.getNumberFormatter());
                        default -> new JTextField();
                    };

                    return new AbstractMap.SimpleEntry<>(
                            flag,
                            inputComponent
                    );
                })
                .collect(LinkedHashMap::new, (m, v) -> m.put(v.getKey(), v.getValue()), LinkedHashMap::putAll);

        this.buildUi();
        this.registerListeners();
    }

    private void buildUi() {
        this.setLayout(new GridBagLayout());

        int row = 0;
        // collect to sorted map
        LinkedHashMap<?, JComponent> entries = Stream.concat(
                this.arguments.entrySet().stream(),
                this.flags.entrySet().stream()
        )
        .collect(LinkedHashMap::new, (m, v) -> m.put(v.getKey(), v.getValue()), LinkedHashMap::putAll);

        for (Map.Entry<?, JComponent> entry : entries.entrySet()) {
            String name;
            if (entry.getKey() instanceof CommandArgument) {
                name = ((CommandArgument) entry.getKey()).getName();
            } else if (entry.getKey() instanceof CommandFlag<?>) {
                name = ((CommandFlag<?>) entry.getKey()).getName();
            } else {
                throw new RuntimeException("Unknown type");
            }

            JLabel label = new JLabel(StringUtil.capitalize(name) + ": \t");
            label.setLabelFor(entry.getValue());

            this.add(label, new GridBagConstraints(
                    0,
                    row,
                    1,
                    1,
                    0.3,
                    0.0,
                    GridBagConstraints.WEST,
                    GridBagConstraints.NONE,
                    JBUI.emptyInsets(),
                    0,
                    0
            ));
            this.add(entry.getValue(), new GridBagConstraints(
                    1,
                    row,
                    1,
                    1,
                    1.0,
                    0.0,
                    GridBagConstraints.WEST,
                    GridBagConstraints.HORIZONTAL,
                    JBUI.emptyInsets(),
                    0,
                    0
            ));

            row++;
        }

        this.add(this.createButton, new GridBagConstraints(
                0,
                row + 1,
                2,
                1,
                1.0,
                1.0,
                GridBagConstraints.NORTHEAST,
                GridBagConstraints.NONE,
                JBUI.emptyInsets(),
                0,
                0
        ));
    }

    private void registerListeners() {
        this.createButton.addActionListener(this::create);
    }

    private void create(ActionEvent actionEvent) {
        this.createButton.setEnabled(false);

        boolean result = AdonisAceService.getInstance(this.project).execAceCommand(
                "Creating " + this.command.getCommandName(),
                this.buildCommand(),
                selectedModule
        );
        this.createButton.setEnabled(true);
    }

    private List<String> buildCommand() {
        return Stream.concat(
                Stream.of(command.getCommandName()),
                Stream.concat(
                        this.arguments.values()
                                .stream()
                                .map(JTextComponent::getText),
                        this.flags.entrySet()
                                .stream()
                                .flatMap(entry -> {
                                    JComponent component = entry.getValue();
                                    String flagName = entry.getKey().getFlagName();
                                    return switch (entry.getKey().getType()) {
                                        case "boolean" ->
                                                Stream.of(((JCheckBox) component).isSelected() ? flagName : "no-" + flagName);
                                        case "string", "number" ->
                                                Stream.of(flagName + " " + this.escapeString(((JTextField) component).getText()));
                                        case "array", "numArray" -> ((JListEditor) component).getValues()
                                                .stream()
                                                .map(value -> flagName + "=" + this.escapeString(value));
                                        default -> Stream.of();
                                    };
                                })
                                .map(value -> "--" + value)
                )

        )

                .collect(Collectors.toList());
    }

    private String escapeString(String str) {
        return str.contains(" ") ? "\"" + str + "\"" : str;
    }

    private void resetFields() {
        this.arguments.values().forEach(field -> field.setText(""));
        this.flags.values().forEach(component -> {
            if (component instanceof JCheckBox) {
                ((JCheckBox) component).setSelected(false);
            } else if (component instanceof JListEditor) {
                ((JListEditor) component).reset();
            } else if (component instanceof JTextField) {
                ((JTextField) component).setText("");
            } else {
                throw new RuntimeException("Unknown type");
            }
        });
    }

    private NumberFormatter getNumberFormatter() {
        NumberFormat format = NumberFormat.getInstance();
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Double.class);
        formatter.setMinimum(0);
        formatter.setMaximum(Double.MAX_VALUE);
        formatter.setAllowsInvalid(false);
        formatter.setCommitsOnValidEdit(true);
        return formatter;
    }


}
