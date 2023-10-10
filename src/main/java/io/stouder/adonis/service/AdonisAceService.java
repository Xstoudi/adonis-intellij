package io.stouder.adonis.service;

import com.intellij.openapi.project.Project;
import io.stouder.adonis.cli.json.ace.Command;

import java.util.function.Consumer;

public interface AdonisAceService {
    static AdonisAceService getInstance(Project project) {
        return project.getService(AdonisAceService.class);
    }

    <T> T runAceCommand(Class<T> responseType, String progressTitle, String... parameters);
    <T> void runAceCommandAsync(Class<T> responseType, Consumer<T> callback, String... parameters);

    void fetchCommands(Consumer<Command[]> callback);
}
