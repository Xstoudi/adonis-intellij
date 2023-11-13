package io.stouder.adonis.service.impl;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import io.stouder.adonis.service.AdonisAppService;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AdonisAppServiceImpl implements AdonisAppService {
    private static final Logger LOG = Logger.getInstance(AdonisAppService.class);

    private final Project project;

    public AdonisAppServiceImpl(Project project) {
        this.project = project;
    }

    public boolean isAdonisProject() {
        return this.getAdonisRoots()
                .stream()
                .anyMatch(this::isAdonisModule);
    }

    public List<String> getAdonisRoots() {
        VirtualFile[] modulesRoots = ProjectRootManager.getInstance(project).getContentRoots();
        return Arrays.stream(modulesRoots)
                .map(VirtualFile::getCanonicalPath)
                .filter(this::isAdonisModule)
                .collect(Collectors.toList());
    }

    private boolean isAdonisModule(String basePath) {
        String adonisrcFilePath = FileUtil.toSystemIndependentName(basePath + "/adonisrc.ts");
        File adonisrcFile = new File(adonisrcFilePath);
        return adonisrcFile.exists();
    }
}
