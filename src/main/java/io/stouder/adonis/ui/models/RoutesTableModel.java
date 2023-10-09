package io.stouder.adonis.ui.models;

import io.stouder.adonis.cli.json.routes.ClosureRouteHandler;
import io.stouder.adonis.cli.json.routes.ControllerRouteHandler;
import io.stouder.adonis.cli.json.routes.RouteDomain;
import io.stouder.adonis.cli.json.routes.RouteHandler;

import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.stream.Collectors;

public class RoutesTableModel extends AbstractTableModel {

    private final String[] columnNames = {"Domain", "Method", "Route", "Handler", "Middleware"};

    private final List<Row> rows;

    public RoutesTableModel(List<RouteDomain> domains) {
        super();
        this.rows = this.generateRows(domains);
    }


    @Override
    public int getRowCount() {
        return this.rows.size();
    }

    @Override
    public int getColumnCount() {
        return this.columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(rowIndex < 0 || rowIndex >= this.rows.size()) {
            return "MISSING";
        }
        return this.rows.get(rowIndex).getColValue(columnIndex);
    }

    @Override
    public String getColumnName(int column) {
        return this.columnNames[column];
    }

    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    private List<Row> generateRows(List<RouteDomain> domains) {
        return domains
                .stream()
                .flatMap(domain ->
                        domain.getRoutes().stream().flatMap(route ->
                                route.getMethods().stream().map(method ->
                                        new Row(
                                                domain.getDomain(),
                                                method,
                                                route.getPattern() + (route.getName().isEmpty() ? "" : " (" + route.getName() + ")"),
                                                route.getHandler() instanceof ClosureRouteHandler ? "closure" : ((ControllerRouteHandler) route.getHandler()).getModuleNameOrPath() + "." + ((ControllerRouteHandler) route.getHandler()).getMethod(),
                                                String.join(", ", route.getMiddleware())
                                        )
                                )
                        )
                )
                .collect(Collectors.toList());
    }

    private String handleHandler(RouteHandler handler) {
        if(handler instanceof ClosureRouteHandler) {
            return "closure";
        } else if (handler instanceof ControllerRouteHandler) {
            return ((ControllerRouteHandler) handler).getModuleNameOrPath() + "." + ((ControllerRouteHandler) handler).getMethod();
        }
        return "MEH";
    }

    private record Row(
        String domain,
        String method,
        String route,
        String handler,
        String middleware
    ) {
        public String getColValue(int columnIndex) {
            return switch (columnIndex) {
                case 0 -> this.domain;
                case 1 -> this.method;
                case 2 -> this.route;
                case 3 -> this.handler;
                case 4 -> this.middleware;
                default -> null;
            };
        }
    }
}
