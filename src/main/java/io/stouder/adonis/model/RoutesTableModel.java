package io.stouder.adonis.model;

import io.stouder.adonis.AdonisBundle;
import io.stouder.adonis.cli.json.routes.ClosureRouteHandler;
import io.stouder.adonis.cli.json.routes.ControllerRouteHandler;
import io.stouder.adonis.cli.json.routes.RouteDomain;
import io.stouder.adonis.cli.json.routes.RouteHandler;
import org.jetbrains.annotations.NotNull;

import javax.swing.table.AbstractTableModel;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RoutesTableModel extends AbstractTableModel {

    private final String[] columnNames = {
            "Module",
            AdonisBundle.message("adonis.routes.columns.domain"),
            AdonisBundle.message("adonis.routes.columns.method"),
            AdonisBundle.message("adonis.routes.columns.route"),
            AdonisBundle.message("adonis.routes.columns.handler"),
            AdonisBundle.message("adonis.routes.columns.middleware")
    };

    private final int ignoreColumns;
    private final List<Row> rows;

    public RoutesTableModel(Map<String, List<RouteDomain>> modules) {
        super();
        this.ignoreColumns = modules.size() > 1 ? 0 : 1;
        this.rows = this.generateRows(modules);
    }


    @Override
    public int getRowCount() {
        return this.rows.size();
    }

    @Override
    public int getColumnCount() {
        return this.columnNames.length - this.ignoreColumns;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(rowIndex < 0 || rowIndex >= this.rows.size()) {
            return "MISSING";
        }
        return this.rows.get(rowIndex).getColValue(columnIndex + this.ignoreColumns);
    }

    @Override
    public String getColumnName(int column) {
        return this.columnNames[column + this.ignoreColumns];
    }

    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    private List<Row> generateRows(Map<String, List<RouteDomain>> modules) {
        return modules
                .entrySet()
                .stream()
                .flatMap(entry ->
                    entry.getValue()
                            .stream()
                            .flatMap(domain ->
                                    domain.getRoutes().stream().flatMap(route ->
                                            route.getMethods().stream().map(method ->
                                                    new Row(
                                                            Path.of(entry.getKey()).getFileName().toString(),
                                                            domain.getDomain(),
                                                            method,
                                                            route.getPattern() + (route.getName().isEmpty() ? "" : " (" + route.getName() + ")"),
                                                            route.getHandler() instanceof ClosureRouteHandler ? "closure" : ((ControllerRouteHandler) route.getHandler()).getModuleNameOrPath() + "." + ((ControllerRouteHandler) route.getHandler()).getMethod(),
                                                            String.join(", ", route.getMiddleware())
                                                    )
                                            )
                                    )
                            )
                )
                .sorted()
                .collect(Collectors.toList());
    }

    private String handleHandler(RouteHandler handler) {
        if(handler instanceof ClosureRouteHandler) {
            return "closure";
        } else if (handler instanceof ControllerRouteHandler) {
            return ((ControllerRouteHandler) handler).getModuleNameOrPath() + "." + ((ControllerRouteHandler) handler).getMethod();
        }
        throw new RuntimeException("Unknown handler type: " + handler.getClass().getName());
    }

    private record Row(
        String module,
        String domain,
        String method,
        String route,
        String handler,
        String middleware
    ) implements Comparable<Row> {
        public String getColValue(int columnIndex) {
            return switch (columnIndex) {
                case 0 -> this.module;
                case 1 -> this.domain;
                case 2 -> this.method;
                case 3 -> this.route;
                case 4 -> this.handler;
                case 5 -> this.middleware;
                default -> null;
            };
        }

        @Override
        public int compareTo(@NotNull RoutesTableModel.Row other) {
            return Comparator.comparing(Row::module)
                    .thenComparing(Row::domain)
                    .thenComparing(Row::route)
                    .thenComparing(Row::method)
                    .thenComparing(Row::handler)
                    .thenComparing(Row::middleware)
                    .compare(this, other);
        }
    }
}
