package io.stouder.adonis.module

import com.intellij.execution.filters.Filter
import com.intellij.ide.util.projectWizard.SettingsStep
import com.intellij.lang.javascript.boilerplate.NpmPackageProjectGenerator
import com.intellij.lang.javascript.boilerplate.NpxPackageDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ContentEntry
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.LabeledComponent
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.ProjectGeneratorPeer
import com.intellij.ui.components.JBCheckBox
import io.stouder.adonis.AdonisBundle
import io.stouder.adonis.AdonisIcons
import org.jetbrains.annotations.NotNull
import java.awt.BorderLayout
import java.util.*
import javax.swing.Icon
import javax.swing.JComponent
import javax.swing.JPanel

class AdonisProjectGenerator : NpmPackageProjectGenerator() {

    private val installDependencies = JBCheckBox(AdonisBundle.message("adonis.project.generator.install.dependencies"))
    private val initializeGit = JBCheckBox(AdonisBundle.message("adonis.project.generator.initialize.git"))

    private val starterKit = ComboBox(
        Arrays.stream(AdonisStarterKit.entries.toTypedArray())
            .map { it.kitName }
            .toList()
            .toTypedArray()
    )

    override fun filters(@NotNull project: Project, @NotNull virtualFile: VirtualFile): Array<Filter> {
        return Filter.EMPTY_ARRAY
    }

    override fun customizeModule(@NotNull virtualFile: VirtualFile, contentEntry: ContentEntry) {}

    override fun packageName(): String {
        return "create-adonisjs"
    }

    override fun getNpxCommands(): List<NpxPackageDescriptor.NpxCommand> {
        return listOf(
            NpxPackageDescriptor.NpxCommand("create-adonisjs@latest", "create-adonisjs@latest")
        )
    }

    override fun presentablePackageName(): String {
        return AdonisBundle.message("adonis.project.generator.presentable.package.name")
    }

    override fun getIcon(): Icon {
        return AdonisIcons.ADONIS
    }

    override fun getDescription(): String {
        return AdonisBundle.message("adonis.project.generator.description")
    }

    override fun getName(): String {
        return AdonisBundle.message("adonis.project.generator.name")
    }

    override fun createPeer(): ProjectGeneratorPeer<Settings> {
        return CreatePeer()
    }

    override fun generatorArgs(project: Project, baseDir: VirtualFile, settings: Settings): Array<String> {
        val workingDirectory = if (generateInTemp()) baseDir.name else "."
        val template = AdonisSettings.extractStarterKit(settings)
        val initializeGit = AdonisSettings.extractInitializeGit(settings)
        return arrayOf(
            workingDirectory,
            "--kit",
            template,
            if (initializeGit) "--git-init" else ""
        )
    }

    override fun generateInTemp(): Boolean {
        return true
    }

    inner class CreatePeer : NpmPackageGeneratorPeer() {

        override fun createPanel(): JPanel {
            val panel = super.createPanel()
            panel.add(createLabeledComponent(AdonisBundle.message("adonis.project.generator.starter.kit"), starterKit))
            panel.add(
                createLabeledComponent(
                    AdonisBundle.message("adonis.project.generator.options"),
                    installDependencies
                )
            )
            panel.add(createLabeledComponent("", initializeGit))
            return panel
        }

        override fun buildUI(@NotNull settingsStep: SettingsStep) {
            super.buildUI(settingsStep)
            settingsStep.addSettingsField(AdonisBundle.message("adonis.project.generator.starter.kit"), starterKit)
            settingsStep.addSettingsField(AdonisBundle.message("adonis.project.generator.options"), initializeGit)
        }

        override fun getSettings(): Settings {
            val settings = super.getSettings()
            settings.putUserData(
                AdonisSettings.STARTER_KIT,
                AdonisStarterKit.entries
                    .stream()
                    .filter { it.kitName == starterKit.selectedItem }
                    .findFirst()
                    .orElse(AdonisStarterKit.SLIM)
                    .repositoryUrl
            )
            settings.putUserData(AdonisSettings.INITIALIZE_GIT, initializeGit.isSelected)
            return settings
        }

        private fun createLabeledComponent(text: String, component: JComponent): LabeledComponent<JComponent> {
            val labeledComponent = LabeledComponent.create(component, text)
            labeledComponent.labelLocation = BorderLayout.WEST
            return labeledComponent
        }
    }
}