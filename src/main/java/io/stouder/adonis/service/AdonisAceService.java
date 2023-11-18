package io.stouder.adonis.service;

import com.intellij.openapi.project.Project;
import io.stouder.adonis.cli.json.ace.Command;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public interface AdonisAceService {
    static AdonisAceService getInstance(Project project) {
        return project.getService(AdonisAceService.class);
    }

    <T> Map<String, Optional<T>> runAceGetCommandOnEveryRoots(String progressTitle, List<String> parameters, Class<T> responseType);
    <T> void runAceGetCommandAsyncOnEveryRoots(Consumer<Map<String, Optional<T>>> callback, List<String> parameters, Class<T> responseType);
    boolean execAceCommand(String progressTitle, List<String> parameters, String basePath);
    void fetchCommands(Consumer<Map<String, Optional<Command[]>>> callback);
}
