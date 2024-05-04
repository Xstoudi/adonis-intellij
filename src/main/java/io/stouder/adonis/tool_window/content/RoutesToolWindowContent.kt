package io.stouder.adonis.tool_window.content

import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.ActionToolbar
import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.table.JBTable
import io.stouder.adonis.cli.json.routes.RouteDomain
import io.stouder.adonis.model.RoutesTableModel
import io.stouder.adonis.notifier.AdonisRouteUpdateNotifier
import javax.swing.JPanel
import javax.swing.table.TableModel
import java.awt.BorderLayout
import java.util.*

class RoutesToolWindowContent(project: Project) : AdonisRouteUpdateNotifier {
    val rootPanel: JPanel = JPanel()
    private val scrollPane: JBScrollPane = JBScrollPane()
    private val routesTable: JBTable = JBTable()

    init {
        buildUi()
    }

    private fun buildUi() {
        rootPanel.layout = BorderLayout()

        buildToolbar()
        buildTable()
    }

    private fun buildToolbar() {
        val actionGroup: ActionGroup = ActionManager.getInstance().getAction("io.stouder.adonis.RoutesToolbar") as ActionGroup
        val toolbar: ActionToolbar = ActionManager.getInstance().createActionToolbar("AdonisRoutesToolbar", actionGroup, true)

        toolbar.targetComponent = rootPanel
        rootPanel.add(toolbar.component, BorderLayout.NORTH)
    }

    private fun buildTable() {
        val model: TableModel = RoutesTableModel(HashMap<String, List<RouteDomain>>())

        routesTable.model = model
        routesTable.rowHeight = 30
        scrollPane.setViewportView(routesTable)

        rootPanel.add(scrollPane, BorderLayout.CENTER)
    }

    override fun routes(routes: MutableMap<String, Optional<Array<RouteDomain>>>?) {
        if (routes == null) return
        val model: TableModel = RoutesTableModel(
            routes
                .entries
                .filter { it.value.isPresent }
                .associate { it.key to it.value.get().toList() }
        )
        routesTable.model = model
    }
}