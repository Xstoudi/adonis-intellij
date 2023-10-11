package io.stouder.adonis.module;

import com.intellij.execution.filters.Filter;
import com.intellij.javascript.nodejs.interpreter.NodeJsInterpreterField;
import com.intellij.lang.javascript.boilerplate.NpmPackageProjectGenerator;
import com.intellij.lang.javascript.boilerplate.NpxPackageDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.platform.ProjectGeneratorPeer;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Collections;
import java.util.List;

public class AdonisProjectGenerator extends NpmPackageProjectGenerator {

    @Override
    protected Filter @NotNull [] filters(@NotNull Project project, @NotNull VirtualFile virtualFile) {
        return new Filter[0];
    }

    @Override
    protected void customizeModule(@NotNull VirtualFile virtualFile, ContentEntry contentEntry) {

    }

    @Override
    protected @NotNull String packageName() {
        return "create-adonisjs";
    }

    @Override
    protected @NotNull List<NpxPackageDescriptor.NpxCommand> getNpxCommands() {
        return List.of(
                new NpxPackageDescriptor.NpxCommand("create-adonisjs", "create-adonisjs")
        );
    }

    @Override
    protected @NotNull String presentablePackageName() {
        return "AdonisJS"; // TODO proper use Bundle
    }

    @Override
    public @NlsContexts.DetailedDescription String getDescription() {
        return "AdonisJS project";
    }

    @Override
    public @NotNull @NlsContexts.Label String getName() {
        return "AdonisJS"; // TODO proper use Bundle
    }

    @Override
    public @NotNull ProjectGeneratorPeer<Settings> createPeer() {
        return new NpmPackageGeneratorPeer();
    }

    class createPeer extends NpmPackageGeneratorPeer {
        @Override
        protected JPanel createPanel() {
            JPanel panel = super.createPanel();

            return panel;
        }
    }
}
