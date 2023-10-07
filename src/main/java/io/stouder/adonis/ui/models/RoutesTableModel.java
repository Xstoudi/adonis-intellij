package io.stouder.adonis.ui.models;

import io.stouder.adonis.cli.json.RouteDomain;

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
                            route.getHandler().getType(),
                            ""
                        )
                    )
                )
            )
            .collect(Collectors.toList());
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
            return null;
        }
        return this.rows.get(rowIndex).getColValue(columnIndex);
    }

    @Override
    public String getColumnName(int column) {
        return this.columnNames[column];
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
                case 0 -> this.method;
                case 1 -> this.route;
                case 2 -> this.handler;
                case 3 -> this.middleware;
                default -> null;
            };
        }
    }
}
