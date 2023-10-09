package io.stouder.adonis.service;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;

import java.io.File;

public class AdonisAppServiceImpl implements AdonisAppService {
    private static final Logger LOG = Logger.getInstance(AdonisAppService.class);

    private final Project project;

    public AdonisAppServiceImpl(Project project) {
        this.project = project;
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
}
