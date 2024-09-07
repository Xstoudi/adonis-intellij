package io.stouder.adonis.tool_window.content.tabs

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.text.StringUtil
import com.intellij.util.ui.JBUI
import io.stouder.adonis.AdonisBundle
import io.stouder.adonis.cli.json.ace.Command
import io.stouder.adonis.cli.json.ace.CommandArgument
import io.stouder.adonis.cli.json.ace.CommandFlag
import io.stouder.adonis.component.JListEditor
import io.stouder.adonis.service.AdonisAceService
import java.awt.Component
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.event.ActionEvent
import java.text.NumberFormat
import java.util.*
import java.util.stream.Collectors
import java.util.stream.Stream
import javax.swing.JButton
import javax.swing.JCheckBox
import javax.swing.JComponent
import javax.swing.JFormattedTextField
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField
import javax.swing.text.NumberFormatter

class MakeCommandTab(private val command: Command, private val project: Project, private val selectedModule: String) : JPanel() {
    private val arguments: LinkedHashMap<CommandArgument, JTextField> = LinkedHashMap(command.args.associate { arg -> arg to JTextField() })
    private val flags: LinkedHashMap<CommandFlag<*>, JComponent>
    private val createButton = JButton(AdonisBundle.message("adonis.tool_window.content.tabs.make.create"))

    init {
        this.flags = LinkedHashMap(command.flags.associateWith { flag ->
            val inputComponent = when (flag.type) {
                "boolean" -> JCheckBox()
                "number" -> JFormattedTextField(getNumberFormatter())
                "array" -> JListEditor()
                "numArray" -> JListEditor(getNumberFormatter())
                else -> JTextField()
            }
            inputComponent
        })

        buildUi()
        registerListeners()
    }

    private fun buildUi() {
        layout = GridBagLayout()

        var row = 0
        val entries = Stream.concat(
            arguments.entries.stream(),
            flags.entries.stream()
        ).collect(Collectors.toList())

        for ((key, value) in entries) {
            val name = if (key is CommandArgument) {
                key.name
            } else if (key is CommandFlag<*>) {
                key.name
            } else {
                throw RuntimeException("Unknown type")
            }

            val label = JLabel(StringUtil.capitalize(name) + ": \t")
            label.labelFor = value as Component?

            add(label, GridBagConstraints(
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
            ))
            add(value, GridBagConstraints(
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
            ))

            row++
        }

        add(createButton, GridBagConstraints(
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
        ))
    }

    private fun registerListeners() {
        createButton.addActionListener {
            create(it)

            resetFields()
        }
    }

    private fun create(actionEvent: ActionEvent) {
        createButton.isEnabled = false

        val result = AdonisAceService.getInstance(project).execAceCommand(
            "Creating " + command.commandName,
            buildCommand(),
            selectedModule
        )

        createButton.isEnabled = true
    }

    private fun buildCommand(): List<String> {
        return Stream.concat(
            Stream.of(command.commandName),
            Stream.concat(
                arguments.values.stream().map { it.text },
                flags.entries.stream()
                    .flatMap { entry ->
                        val component = entry.value
                        val flagName = entry.key.flagName
                        when (entry.key.type) {
                            "boolean" -> Stream.of<String>(if (component is JCheckBox && component.isSelected) flagName else "no-$flagName")
                            "string", "number" -> Stream.of<String>("$flagName ${escapeString((component as JTextField).text)}")
                            "array", "numArray" -> (component as JListEditor).values.stream().map { "$flagName=${escapeString(it)}" }
                            else -> Stream.empty<String>()
                        }
                    }
                    .map { value -> "--$value" }
            )
        ).collect(Collectors.toList())
    }

    private fun escapeString(str: String): String {
        return if (str.contains(" ")) "\"" + str + "\"" else str
    }

    private fun resetFields() {
        arguments.values.forEach { it.text = "" }
        flags.values.forEach { component ->
            when (component) {
                is JCheckBox -> component.isSelected = false
                is JListEditor -> component.reset()
                is JTextField -> component.text = ""
                else -> throw RuntimeException("Unknown type")
            }
        }
    }

    private fun getNumberFormatter(): NumberFormatter {
        val format = NumberFormat.getInstance()
        val formatter = NumberFormatter(format)
        formatter.valueClass = Double::class.java
        formatter.minimum = 0
        formatter.maximum = Double.MAX_VALUE
        formatter.allowsInvalid = false
        formatter.commitsOnValidEdit = true
        return formatter
    }
}
