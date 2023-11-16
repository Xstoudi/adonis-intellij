package io.stouder.adonis.service;

import com.intellij.openapi.project.Project;
import io.stouder.adonis.cli.json.ace.Command;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public interface AdonisAceService {
    static AdonisAceService getInstance(Project project) {
        return project.getService(AdonisAceService.class);
    }

    <T> Map<String, Optional<T>> runAceGetCommand(String progressTitle, List<String> parameters, Class<T> responseType);
    <T> void runAceGetCommandAsync(Consumer<Map<String, Optional<T>>> callback, List<String> parameters, Class<T> responseType);
    <T> T runAceCommand(String progressTitle, List<String> parameters, Class<T> responseType);
    boolean runAceCommand(String progressTitle, List<String> parameters);

    <T> void runAceCommandAsync(Consumer<T> callback, List<String> parameters, Class<T> responseType);
    void runAceCommandAsync(BooleanConsumer callback, List<String> parameters);
    void fetchCommands(Consumer<Map<String, Optional<Command[]>>> callback);
}
