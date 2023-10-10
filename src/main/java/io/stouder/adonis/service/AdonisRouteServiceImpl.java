package io.stouder.adonis.service;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import io.stouder.adonis.cli.json.routes.RouteDomain;

import java.util.List;
import java.util.function.Consumer;

public class AdonisRouteServiceImpl implements AdonisRouteService {
    private static final Logger LOG = Logger.getInstance(AdonisRouteService.class);

    private List<RouteDomain> routes = List.of();

    private final Project project;

    public AdonisRouteServiceImpl(Project project) {
        this.project = project;
    }

    @Override
    public void fetchRoutes(Consumer<RouteDomain[]> callback) {
        AdonisAceService adonisAceService = AdonisAceService.getInstance(this.project);
        adonisAceService.runAceCommandAsync(RouteDomain[].class, callback, "list:routes", "--json");
    }

}
