package io.stouder.adonis.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.process.ScriptRunnerUtil;
import com.intellij.execution.process.mediator.daemon.ProcessManager;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import io.stouder.adonis.cli.json.ace.Command;
import io.stouder.adonis.cli.json.routes.RouteHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class AdonisAceServiceImpl implements AdonisAceService {

    private static final Logger LOG = Logger.getInstance(AdonisAceService.class);

    private final Gson gson;
    private final Project project;

    public AdonisAceServiceImpl(Project project) {
        this.project = project;

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(RouteHandler.class, new RouteHandler.Deserializer());
        gsonBuilder.registerTypeAdapter(Command.class, new Command.Deserializer());
        this.gson = gsonBuilder.create();
    }

    @Override
    public <T> T runAceCommand(Class<T> responseType, String progressTitle, String... parameters) {
        List<String> params = new ArrayList<>(Arrays.asList(parameters));
        params.add(0, "ace");
        GeneralCommandLine commandLine = new GeneralCommandLine()
                .withExePath("node")
                .withWorkDirectory(this.project.getBasePath())
                .withParameters(params);

        // run async with progress bar
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
    public <T> void runAceCommandAsync(Class<T> responseType, Consumer<T> callback, String... parameters) {
        List<String> params = new ArrayList<>(Arrays.asList(parameters));
        params.add(0, "ace");
        GeneralCommandLine commandLine = new GeneralCommandLine()
                .withExePath("node")
                .withWorkDirectory(this.project.getBasePath())
                .withParameters(params);
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
    public void fetchCommands(Consumer<Command[]> callback) {
        AdonisAceService adonisAceService = AdonisAceService.getInstance(this.project);
        adonisAceService.runAceCommandAsync(Command[].class, callback, "list","--json");
    }
}
