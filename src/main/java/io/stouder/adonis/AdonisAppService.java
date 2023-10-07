package io.stouder.adonis;

import com.intellij.openapi.project.Project;
import io.stouder.adonis.cli.json.RouteDomain;

import java.util.List;

public interface AdonisAppService {
    static AdonisAppService getInstance(Project project) {
        return project.getService(AdonisAppService.class);
    }

    List<RouteDomain> getRoutes();
    boolean isAdonisProject();
}
