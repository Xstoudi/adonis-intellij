package io.stouder.adonis.service;

import com.intellij.openapi.project.Project;

public interface TextmateService {
    static TextmateService getInstance(Project project) {
        return project.getService(TextmateService.class);
    }

    void ensureEdgeIsInstalled();
}
