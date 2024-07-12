package io.stouder.adonis.component

import com.intellij.ui.components.JBList
import io.stouder.adonis.AdonisBundle
import java.awt.BorderLayout
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.util.Collections
import java.util.stream.IntStream
import javax.swing.*

class JListEditor : JPanel {
    private val listModel = DefaultListModel<String>()
    private val stringList = JBList(listModel)
    private val textField: JTextField
    private val addButton = JButton(AdonisBundle.message("adonis.component.jlisteditor.add"))
    private val removeButton = JButton(AdonisBundle.message("adonis.component.jlisteditor.remove"))

    constructor() : this(null)

    constructor(formatter: JFormattedTextField.AbstractFormatter?) {
        textField = if (formatter == null) JTextField(20) else JFormattedTextField(formatter)
        buildUI()
        behavior()
    }

    val values: List<String>
        get() = listModel.toArray().map { it as String }

    fun reset() {
        listModel.clear()
    }

    private fun buildUI() {
        stringList.disableEmptyText()
        layout = BorderLayout()

        val inputPanel = JPanel()
        inputPanel.layout = GridBagLayout()
        val inputGBC = GridBagConstraints()
        inputGBC.fill = GridBagConstraints.HORIZONTAL
        inputGBC.gridwidth = 2
        inputGBC.gridx = 0
        inputGBC.gridy = 0

        val addButtonGBC = GridBagConstraints()
        addButtonGBC.fill = GridBagConstraints.HORIZONTAL
        addButtonGBC.gridwidth = 1
        addButtonGBC.weightx = 0.5
        addButtonGBC.gridx = 0
        addButtonGBC.gridy = 1

        val editButtonGBC = GridBagConstraints()
        editButtonGBC.fill = GridBagConstraints.HORIZONTAL
        editButtonGBC.gridwidth = 1
        editButtonGBC.weightx = 0.5
        editButtonGBC.gridx = 1
        editButtonGBC.gridy = 1

        inputPanel.add(textField, inputGBC)
        inputPanel.add(addButton, addButtonGBC)
        inputPanel.add(removeButton, editButtonGBC)

        add(stringList, BorderLayout.CENTER)
        add(inputPanel, BorderLayout.SOUTH)
    }

    private fun behavior() {
        addButton.addActionListener {
            val input = textField.text.trim()
            if (input.isNotEmpty()) {
                listModel.addElement(input)
                textField.text = ""
            }
            stringList.setExpandableItemsEnabled(false)
        }

        removeButton.addActionListener {
            IntStream.of(*stringList.selectedIndices)
                .boxed()
                .sorted(Collections.reverseOrder())
                .forEach { listModel.remove(it) }
        }
    }
}