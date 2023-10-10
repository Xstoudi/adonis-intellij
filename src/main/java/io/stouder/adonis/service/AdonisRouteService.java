package io.stouder.adonis.service;

import com.intellij.openapi.project.Project;
import io.stouder.adonis.cli.json.routes.RouteDomain;

import java.util.function.Consumer;

public interface AdonisRouteService {
    static AdonisRouteService getInstance(Project project) {
        return project.getService(AdonisRouteService.class);
    }

    void fetchRoutes(Consumer<RouteDomain[]> callback);
}
