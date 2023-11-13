package io.stouder.adonis.service;

import com.intellij.openapi.project.Project;

import java.util.List;

public interface AdonisAppService {
    static AdonisAppService getInstance(Project project) {
        return project.getService(AdonisAppService.class);
    }

    boolean isAdonisProject();

    List<String> getAdonisRoots();
}
