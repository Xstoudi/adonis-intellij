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
import java.awt.BorderLayout
import javax.swing.Icon
import javax.swing.JComponent
import javax.swing.JPanel

class AdonisProjectGenerator : NpmPackageProjectGenerator() {
    override fun filters(project: Project, virtualFile: VirtualFile): Array<Filter> {
        return Filter.EMPTY_ARRAY
    }

    override fun customizeModule(virtualFile: VirtualFile, contentEntry: ContentEntry) {}

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
        val installDependencies = AdonisSettings.extractInstallDependencies(settings)
        val initializeGit = AdonisSettings.extractInitializeGit(settings)
        return arrayOf(
            workingDirectory,
            "--kit",
            template,
            "--" + (if (installDependencies) "" else "no-") + "install",
            "--" + (if (initializeGit) "" else "no-") + "git-init"
        )
    }

    override fun generateInTemp(): Boolean {
        return true
    }

    inner class CreatePeer : NpmPackageGeneratorPeer() {
        private val starterKit: ComboBox<String> = ComboBox(
            AdonisStarterKit.entries
                .map { it.name }
                .toTypedArray()
        )
        private val installDependencies: JBCheckBox = JBCheckBox(AdonisBundle.message("adonis.project.generator.install.dependencies"))
        private val initializeGit: JBCheckBox = JBCheckBox(AdonisBundle.message("adonis.project.generator.initialize.git"))

        override fun createPanel(): JPanel {
            val panel = super.createPanel()
            panel.add(createLabeledComponent(AdonisBundle.message("adonis.project.generator.starter.kit"), starterKit))
            panel.add(createLabeledComponent(AdonisBundle.message("adonis.project.generator.options"), installDependencies))
            panel.add(createLabeledComponent("", initializeGit))
            return panel
        }

        override fun buildUI(settingsStep: SettingsStep) {
            super.buildUI(settingsStep)
            settingsStep.addSettingsField(AdonisBundle.message("adonis.project.generator.starter.kit"), starterKit)
            settingsStep.addSettingsField(AdonisBundle.message("adonis.project.generator.options"), installDependencies)
            settingsStep.addSettingsField("", initializeGit)
        }

        override fun getSettings(): Settings {
            val settings = super.getSettings()
            settings.putUserData(AdonisSettings.STARTER_KIT, starterKit.selectedItem?.toString() ?: AdonisStarterKit.API.toString())
            settings.putUserData(AdonisSettings.INSTALL_DEPENDENCIES, installDependencies.isSelected)
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