package io.stouder.adonis.module;

import com.intellij.execution.filters.Filter;
import com.intellij.ide.util.projectWizard.SettingsStep;
import com.intellij.lang.javascript.boilerplate.NpmPackageProjectGenerator;
import com.intellij.lang.javascript.boilerplate.NpxPackageDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.platform.ProjectGeneratorPeer;
import com.intellij.ui.components.JBCheckBox;
import io.stouder.adonis.AdonisBundle;
import io.stouder.adonis.AdonisIcons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
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
                new NpxPackageDescriptor.NpxCommand("create-adonisjs@latest", "create-adonisjs@latest")
        );
    }

    @Override
    protected @NotNull String presentablePackageName() {
        return AdonisBundle.message("adonis.project.generator.presentable.package.name");
    }

    @Override
    public Icon getIcon() {
        return AdonisIcons.ADONIS;
    }

    @Override
    public String getDescription() {
        return AdonisBundle.message("adonis.project.generator.description");
    }

    @Override
    public @NotNull String getName() {
        return AdonisBundle.message("adonis.project.generator.name");
    }

    @Override
    public @NotNull ProjectGeneratorPeer<Settings> createPeer() {
        return new CreatePeer();
    }

    @Override
    protected String[] generatorArgs(Project project, VirtualFile baseDir, Settings settings) {
        String workingDirectory = generateInTemp() ? baseDir.getName() : ".";
        String template = AdonisSettings.extractStarterKit(settings);
        Boolean installDependencies = AdonisSettings.extractInstallDependencies(settings);
        Boolean initializeGit = AdonisSettings.extractInitializeGit(settings);
        return new String[] {
                workingDirectory,
                "--kit",
                template,
                "--" + (installDependencies ? "" : "no-") + "install",
                "--" + (initializeGit ? "" : "no-") + "git-init"
        };
    }

    @Override
    protected boolean generateInTemp() {
        return true;
    }

    class CreatePeer extends NpmPackageGeneratorPeer {

        private ComboBox<String> starterKit;
        private JBCheckBox installDependencies;
        private JBCheckBox initializeGit;

        @Override
        protected JPanel createPanel() {
            JPanel panel = super.createPanel();
            this.starterKit = new ComboBox<>(
                    Arrays.stream(AdonisStarterKit.values())
                            .map(AdonisStarterKit::getName)
                            .toList()
                            .toArray(new String[0])
            );
            this.installDependencies = new JBCheckBox(AdonisBundle.message("adonis.project.generator.install.dependencies"));
            this.initializeGit = new JBCheckBox(AdonisBundle.message("adonis.project.generator.initialize.git"));

            panel.add(createLabeledComponent(AdonisBundle.message("adonis.project.generator.starter.kit"), this.starterKit));
            panel.add(createLabeledComponent(AdonisBundle.message("adonis.project.generator.options"), this.installDependencies));
            panel.add(createLabeledComponent("", this.initializeGit));

            return panel;
        }

        @Override
        public void buildUI(@NotNull SettingsStep settingsStep) {
            super.buildUI(settingsStep);
            settingsStep.addSettingsField(AdonisBundle.message("adonis.project.generator.starter.kit"), this.starterKit);
            settingsStep.addSettingsField(AdonisBundle.message("adonis.project.generator.options"), this.installDependencies);
            settingsStep.addSettingsComponent(this.initializeGit);
        }

        @Override
        @NotNull
        public Settings getSettings() {
            Settings settings = super.getSettings();
            settings.putUserData(AdonisSettings.STARTER_KIT, this.starterKit.getSelectedItem().toString());
            settings.putUserData(AdonisSettings.INSTALL_DEPENDENCIES, this.installDependencies.isSelected());
            settings.putUserData(AdonisSettings.INITIALIZE_GIT, this.initializeGit.isSelected());
            return settings;
        }

        private LabeledComponent<JComponent> createLabeledComponent(String text, JComponent component) {
            LabeledComponent<JComponent> labeledComponent = LabeledComponent.create(component, text);
            labeledComponent.setLabelLocation(BorderLayout.WEST);
            return labeledComponent;
        }
    }
}
