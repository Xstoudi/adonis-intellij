package io.stouder.adonis.service;

import com.intellij.openapi.project.Project;
import io.stouder.adonis.cli.json.ace.Command;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.function.Consumer;

public interface AdonisAceService {
    static AdonisAceService getInstance(Project project) {
        return project.getService(AdonisAceService.class);
    }

    <T> void runAceCommand(Class<T> responseType, Consumer<T> callback, String... parameters);

    void fetchCommands(Consumer<Command[]> callback);
}
