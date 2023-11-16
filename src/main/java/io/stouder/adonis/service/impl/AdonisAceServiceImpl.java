package io.stouder.adonis.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.ScriptRunnerUtil;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import io.stouder.adonis.cli.json.ace.Command;
import io.stouder.adonis.cli.json.routes.RouteHandler;
import io.stouder.adonis.service.AdonisAceService;
import io.stouder.adonis.service.AdonisAppService;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class AdonisAceServiceImpl implements AdonisAceService {

    private static final Logger LOG = Logger.getInstance(AdonisAceService.class);

    private final Gson gson;
    private final Project project;

    public AdonisAceServiceImpl(Project project) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(RouteHandler.class, new RouteHandler.Deserializer());
        gsonBuilder.registerTypeAdapter(Command.class, new Command.Deserializer());
        this.gson = gsonBuilder.create();
        this.project = project;

    }

    @Override
    public <T> Map<String, Optional<T>> runAceGetCommand(String progressTitle, List<String> parameters, Class<T> responseType) {
        List<String> params = new ArrayList<>(parameters);
        params.add(0, "ace");

        return ProgressManager.getInstance().runProcessWithProgressSynchronously(
                () -> AdonisAppService.getInstance(this.project).getAdonisRoots()
                        .stream()
                        .collect(
                                Collectors.toMap(
                                        basePath -> basePath,
                                        basePath -> {
                                            try {
                                                GeneralCommandLine commandLine = new GeneralCommandLine()
                                                        .withExePath("node")
                                                        .withWorkDirectory(basePath)
                                                        .withParameters(params);
                                                String jsonOutput = ScriptRunnerUtil.getProcessOutput(commandLine);
                                                return Optional.ofNullable(gson.fromJson(jsonOutput, responseType));
                                            } catch (ExecutionException e) {
                                                return Optional.empty();
                                            }
                                        }
                                )
                        ),
                progressTitle,
                true,
                this.project
        );
    }

    @Override
    public <T> void runAceGetCommandAsync(Consumer<Map<String, Optional<T>>> callback, List<String> parameters, Class<T> responseType) {
        List<String> params = new ArrayList<>(parameters);
        params.add(0, "ace");

        ApplicationManager.getApplication().executeOnPooledThread(() -> {
            Map<String, Optional<T>> result = AdonisAppService.getInstance(this.project).getAdonisRoots()
                    .stream()
                    .collect(
                            Collectors.toMap(
                                    basePath -> basePath,
                                    basePath -> {
                                        try {
                                            GeneralCommandLine commandLine = new GeneralCommandLine()
                                                    .withExePath("node")
                                                    .withWorkDirectory(basePath)
                                                    .withParameters(params);
                                            String jsonOutput = ScriptRunnerUtil.getProcessOutput(commandLine);
                                            System.out.println(basePath + " " + jsonOutput);
                                            return Optional.ofNullable(gson.fromJson(jsonOutput, responseType));
                                        } catch (ExecutionException e) {
                                            return Optional.empty();
                                        }
                                    }
                            )
                    );
            callback.accept(result);
        });
    }

    @Override
    public <T> T runAceCommand(String progressTitle, List<String> parameters, Class<T> responseType) {
        List<String> params = new ArrayList<>(parameters);
        params.add(0, "ace");
        GeneralCommandLine commandLine = new GeneralCommandLine()
                .withExePath("node")
                .withWorkDirectory(this.project.getBasePath())
                .withParameters(params);
        LOG.info("Running command with progress bar: " + commandLine.getCommandLineString());
        return ProgressManager.getInstance().runProcessWithProgressSynchronously(
                () -> {
                    try {
                        String jsonOutput = ScriptRunnerUtil.getProcessOutput(commandLine);
                        return gson.fromJson(jsonOutput, responseType);
                    } catch (ExecutionException e) {
                        return null;
                    }
                },
                progressTitle,
                true,
                this.project
        );
    }

    @Override
    public boolean runAceCommand(String progressTitle, List<String> parameters) {
        List<String> params = new ArrayList<>(parameters);
        params.add(0, "ace");
        GeneralCommandLine commandLine = new GeneralCommandLine()
                .withExePath("node")
                .withWorkDirectory(this.project.getBasePath())
                .withParameters(params);
        LOG.info("Running command with progress bar: " + commandLine.getCommandLineString());
        return ProgressManager.getInstance().runProcessWithProgressSynchronously(
                () -> {
                    try {
                        ScriptRunnerUtil.getProcessOutput(commandLine);
                        return true;
                    } catch (ExecutionException e) {
                        return false;
                    }
                },
                progressTitle,
                true,
                this.project
        );
    }

    @Override
    public <T> void runAceCommandAsync(Consumer<T> callback, List<String> parameters, Class<T> responseType) {
        List<String> params = new ArrayList<>(parameters);
        params.add(0, "ace");
        GeneralCommandLine commandLine = new GeneralCommandLine()
                .withExePath("node")
                .withWorkDirectory(this.project.getBasePath())
                .withParameters(params);
        LOG.info("Running command: " + commandLine.getCommandLineString());
        ApplicationManager.getApplication().executeOnPooledThread(() -> {
            try {
                String jsonOutput = ScriptRunnerUtil.getProcessOutput(commandLine);
                callback.accept(gson.fromJson(jsonOutput, responseType));
            } catch (ExecutionException e) {
                LOG.error(e);
                callback.accept(null);
            }
        });
    }

    @Override
    public void runAceCommandAsync(BooleanConsumer callback, List<String> parameters) {
        List<String> params = new ArrayList<>(parameters);
        params.add(0, "ace");
        GeneralCommandLine commandLine = new GeneralCommandLine()
                .withExePath("node")
                .withWorkDirectory(this.project.getBasePath())
                .withParameters(params);
        LOG.info("Running command: " + commandLine.getCommandLineString());
        ApplicationManager.getApplication().executeOnPooledThread(() -> {
            try {
                ScriptRunnerUtil.getProcessOutput(commandLine);
                callback.accept(true);
            } catch (ExecutionException e) {
                LOG.error(e);
                callback.accept(false);
            }
        });
    }

    @Override
    public void fetchCommands(Consumer<Map<String, Optional<Command[]>>> callback) {
        AdonisAceService adonisAceService = AdonisAceService.getInstance(this.project);
        adonisAceService.runAceGetCommandAsync(callback, List.of("list","--json"), Command[].class);
    }
}
