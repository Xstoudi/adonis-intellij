package io.stouder.adonis.component;

import com.intellij.ui.components.JBList;
import io.stouder.adonis.AdonisBundle;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class JListEditor extends JPanel {
    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JBList<String> stringList = new JBList<>(listModel);
    private final JTextField textField;
    private final JButton addButton = new JButton(AdonisBundle.message("adonis.component.jlisteditor.add"));
    private final JButton removeButton = new JButton(AdonisBundle.message("adonis.component.jlisteditor.remove"));

    public JListEditor() {
        this(null);
    }

    public JListEditor(JFormattedTextField.AbstractFormatter formatter) {
        this.textField = formatter == null ? new JTextField(20) : new JFormattedTextField(formatter);
        this.buildUI();
        this.behavior();
    }

    public List<String> getValues() {
        return List.of((String[]) this.listModel.toArray());
    }

    public void reset() {
        this.listModel.clear();
    }

    private void buildUI() {
        this.stringList.disableEmptyText();
        this.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        GridBagConstraints inputGBC = new GridBagConstraints();
        inputGBC.fill = GridBagConstraints.HORIZONTAL;
        inputGBC.gridwidth = 2;
        inputGBC.gridx = 0;
        inputGBC.gridy = 0;

        GridBagConstraints addButtonGBC = new GridBagConstraints();
        addButtonGBC.fill = GridBagConstraints.HORIZONTAL;
        addButtonGBC.gridwidth = 1;
        addButtonGBC.weightx = 0.5;
        addButtonGBC.gridx = 0;
        addButtonGBC.gridy = 1;

        GridBagConstraints editButtonGBC = new GridBagConstraints();
        editButtonGBC.fill = GridBagConstraints.HORIZONTAL;
        editButtonGBC.gridwidth = 1;
        editButtonGBC.weightx = 0.5;
        editButtonGBC.gridx = 1;
        editButtonGBC.gridy = 1;

        inputPanel.add(this.textField, inputGBC);
        inputPanel.add(this.addButton, addButtonGBC);
        inputPanel.add(this.removeButton, editButtonGBC);

        this.add(this.stringList, BorderLayout.CENTER);
        this.add(inputPanel, BorderLayout.SOUTH);
    }

    private void behavior() {
        this.addButton.addActionListener(e -> {
            String input = this.textField.getText().trim();
            if (!input.isEmpty()) {
                this.listModel.addElement(input);
                this.textField.setText("");
            }
            this.stringList.setExpandableItemsEnabled(false);
        });

        this.removeButton.addActionListener(e ->
            IntStream.of(this.stringList.getSelectedIndices())
                    .boxed()
                    .sorted(Collections.reverseOrder())
                    .forEach(this.listModel::remove)
        );
    }
}