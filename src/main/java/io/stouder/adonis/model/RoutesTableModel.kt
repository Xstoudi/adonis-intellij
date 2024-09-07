package io.stouder.adonis.model

import io.stouder.adonis.AdonisBundle
import io.stouder.adonis.cli.json.routes.ClosureRouteHandler
import io.stouder.adonis.cli.json.routes.ControllerRouteHandler
import io.stouder.adonis.cli.json.routes.RouteDomain
import io.stouder.adonis.cli.json.routes.RouteHandler
import javax.swing.table.AbstractTableModel
import java.nio.file.Path
import java.util.stream.Collectors

class RoutesTableModel(private val modules: Map<String, List<RouteDomain>>) : AbstractTableModel() {

    private val columnNames = arrayOf(
        AdonisBundle.message("adonis.routes.columns.module"),
        AdonisBundle.message("adonis.routes.columns.domain"),
        AdonisBundle.message("adonis.routes.columns.method"),
        AdonisBundle.message("adonis.routes.columns.route"),
        AdonisBundle.message("adonis.routes.columns.handler"),
        AdonisBundle.message("adonis.routes.columns.middleware")
    )

    private val ignoreColumns: Int = if (modules.size > 1) 0 else 1
    private val rows: List<Row>

    init {
        this.rows = generateRows(modules)
    }

    override fun getRowCount(): Int {
        return rows.size
    }

    override fun getColumnCount(): Int {
        return columnNames.size - ignoreColumns
    }

    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any? {
        if (rowIndex !in rows.indices) {
            return "MISSING"
        }
        return rows[rowIndex].getColValue(columnIndex + ignoreColumns)
    }

    override fun getColumnName(column: Int): String {
        return columnNames[column + ignoreColumns]
    }

    override fun getColumnClass(columnIndex: Int): Class<*> {
        return String::class.java
    }

    private fun generateRows(modules: Map<String, List<RouteDomain>>): List<Row> {
        return modules.entries.stream()
            .flatMap { entry ->
                entry.value.stream()
                    .flatMap { domain ->
                        domain.routes.stream()
                            .flatMap { route ->
                                route.methods.stream()
                                    .map { method ->
                                        Row(
                                            Path.of(entry.key).fileName.toString(),
                                            domain.domain,
                                            method,
                                            route.pattern + if (route.name.isEmpty()) "" else " (${route.name})",
                                            if (route.handler is ClosureRouteHandler) "closure" else "${(route.handler as ControllerRouteHandler).moduleNameOrPath}.${route.handler.method}",
                                            route.middleware.joinToString(", ")
                                        )
                                    }
                            }
                    }
            }
            .sorted()
            .collect(Collectors.toList())
    }

    private fun handleHandler(handler: RouteHandler): String {
        return when (handler) {
            is ClosureRouteHandler -> {
                "closure"
            }

            is ControllerRouteHandler -> {
                "${handler.moduleNameOrPath}.${handler.method}"
            }

            else -> {
                throw RuntimeException("Unknown handler type: ${handler::class.java.name}")
            }
        }
    }

    private data class Row(
        val module: String,
        val domain: String,
        val method: String,
        val route: String,
        val handler: String,
        val middleware: String
    ) : Comparable<Row> {
        fun getColValue(columnIndex: Int): String? {
            return when (columnIndex) {
                0 -> module
                1 -> domain
                2 -> method
                3 -> route
                4 -> handler
                5 -> middleware
                else -> null
            }
        }

        override fun compareTo(other: Row): Int {
            return compareValuesBy(this, other, Row::module, Row::domain, Row::route, Row::method, Row::handler, Row::middleware)
        }
    }
}
