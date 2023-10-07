package io.stouder.adonis;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.ScriptRunnerUtil;
import com.intellij.lang.javascript.index.JavaScriptIndex;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.startup.StartupManager;
import com.intellij.openapi.util.ThrowableComputable;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import io.stouder.adonis.cli.json.RouteDomain;
import io.stouder.adonis.cli.json.RouteHandler;
import org.apache.tools.ant.ProjectComponent;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class AdonisAppServiceImpl implements AdonisAppService {
    private static final Logger LOG = Logger.getInstance(AdonisAppService.class);
    private final Project project;
    private final Gson gson;

    public AdonisAppServiceImpl(Project project) {
        this.project = project;

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(RouteHandler.class, new RouteHandler.Deserializer());
        this.gson = gsonBuilder.create();
    }

    public List<RouteDomain> getRoutes() {
        return Arrays.stream(ProjectRootManager.getInstance(this.project).getContentRoots())
                .map(contentRoot -> runAceCommand(RouteDomain[].class, "list:routes", "--json"))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .flatMap(Arrays::stream)
                .collect(Collectors.toList());
    }

    public boolean isAdonisProject() {
        String projectBasePath = this.project.getBasePath();
        if(projectBasePath == null) {
            return false;
        }
        String adonisrcFilePath = FileUtil.toSystemIndependentName(projectBasePath + "/adonisrc.ts");
        File adonisrcFile = new File(adonisrcFilePath);
        return adonisrcFile.exists();
    }

    private <T> Optional<T> runAceCommand(Class<T> responseType, String... parameters) {
        List<String> params = new ArrayList<>(Arrays.asList(parameters));
        params.add(0, "ace");
        GeneralCommandLine commandLine = new GeneralCommandLine()
                .withExePath("node")
                .withWorkDirectory(this.project.getBasePath())
                .withParameters(params);
        try {
            String jsonOutput = ProgressManager.getInstance().runProcessWithProgressSynchronously(
                    () -> ScriptRunnerUtil.getProcessOutput(commandLine),
                    "Running ace command",
                    false,
                    this.project
            );
            T target = gson.fromJson(jsonOutput, responseType);
            return Optional.of(target);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
