package io.stouder.adonis.service.impl;

import com.intellij.openapi.project.Project;
import io.stouder.adonis.cli.json.routes.RouteDomain;
import io.stouder.adonis.service.AdonisAceService;
import io.stouder.adonis.service.AdonisRouteService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class AdonisRouteServiceImpl implements AdonisRouteService {
    private final Project project;

    public AdonisRouteServiceImpl(Project project) {
        this.project = project;
    }

    @Override
    public void fetchRoutes(Consumer<Map<String, Optional<RouteDomain[]>>> callback) {
        AdonisAceService adonisAceService = AdonisAceService.getInstance(this.project);
        adonisAceService.runAceGetCommandAsyncOnEveryRoots(callback, List.of("list:routes", "--json"), RouteDomain[].class);
    }

}
