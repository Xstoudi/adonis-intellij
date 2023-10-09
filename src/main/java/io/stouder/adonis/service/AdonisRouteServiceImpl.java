package io.stouder.adonis.service;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import io.stouder.adonis.cli.json.routes.RouteDomain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
        adonisAceService.runAceCommand(RouteDomain[].class, callback, "list:routes", "--json");
    }

}
