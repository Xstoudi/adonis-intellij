package io.stouder.adonis.tool_window.content

import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.ActionToolbar
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.util.text.StringUtil
import com.intellij.ui.components.JBTabbedPane
import io.stouder.adonis.cli.json.ace.Command
import io.stouder.adonis.notifier.AdonisRcUpdateNotifier
import io.stouder.adonis.tool_window.content.tabs.MakeCommandTab
import javax.swing.DefaultComboBoxModel
import javax.swing.JComponent
import javax.swing.JPanel
import java.awt.BorderLayout
import java.util.*
import java.util.stream.Collectors
import java.util.stream.Stream
import kotlin.collections.HashMap

class MakeToolWindowContent(private val project: Project) : AdonisRcUpdateNotifier {
    private val rootPanel = JPanel()
    private val comboBox = ComboBox<String>()
    private val tabbedPanes = HashMap<String, JBTabbedPane>()
    private var makeCommands: Map<String, List<Command>> = HashMap()
    private var selectedModule: String? = null

    init {

        rootPanel.layout = BorderLayout()
        comboBox.addActionListener {
            rootPanel.remove(tabbedPanes[selectedModule])
            selectedModule = comboBox.selectedItem as String
            updateUi()
        }

        updateUi()
    }

    fun getRootPanel(): JComponent {
        return rootPanel
    }

    private fun buildToolbar() {
        val actionGroup: ActionGroup = ActionManager.getInstance().getAction("io.stouder.adonis.MakeToolbar") as ActionGroup
        val toolbar: ActionToolbar = ActionManager.getInstance().createActionToolbar(
            "AdonisCommandsToolbar",
            actionGroup,
            true
        )

        comboBox.model = DefaultComboBoxModel(makeCommands.keys.toTypedArray())
        if (selectedModule != null) {
            comboBox.selectedItem = selectedModule
        }

        val toolbarAndComboPanel = JPanel(BorderLayout())
        toolbarAndComboPanel.add(toolbar.component, BorderLayout.WEST)
        toolbarAndComboPanel.add(comboBox, BorderLayout.CENTER)
        toolbar.targetComponent = rootPanel

        rootPanel.add(toolbarAndComboPanel, BorderLayout.NORTH)
    }

    private fun buildTabs() {
        for (module in makeCommands.keys) {
            var tabbedPane = tabbedPanes[module]
            if (tabbedPane == null) {
                tabbedPane = JBTabbedPane()
                tabbedPanes[module] = tabbedPane
            }

            while (tabbedPane.tabCount > 0) {
                tabbedPane.removeTabAt(0)
            }

            for (command in makeCommands[module]!!) {
                val commandNameWithoutNamespace = StringUtil.capitalize(
                    command.commandName.replace("make:", "")
                )
                tabbedPane.addTab(commandNameWithoutNamespace, MakeCommandTab(command, project, module))
            }
        }
    }

    private fun updateUi() {
        buildTabs()
        buildToolbar()

        val tabbedPane = tabbedPanes[selectedModule]
        if (tabbedPane != null) {
            rootPanel.add(tabbedPane, BorderLayout.CENTER)
        }
    }

    override fun commands(commands: Map<String, Optional<Array<Command>>>) {
        makeCommands = commands.entries.stream()
            .filter { it.value.isPresent }
            .collect(
                Collectors.toMap(
                    { it.key },
                    { it.value.get().filter { command -> "make" == command.namespace }.toList() }
                )
            )
        selectedModule = if (selectedModule == null) {
            makeCommands.keys.stream().findFirst().orElse(null)
        } else selectedModule

        ApplicationManager.getApplication().invokeLater { updateUi() }
    }
}