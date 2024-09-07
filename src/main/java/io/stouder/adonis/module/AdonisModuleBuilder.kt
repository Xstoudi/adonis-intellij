package io.stouder.adonis.module

import com.intellij.ide.util.projectWizard.WebTemplateNewProjectWizard
import com.intellij.ide.wizard.GeneratorNewProjectWizardBuilderAdapter

class AdonisModuleBuilder : GeneratorNewProjectWizardBuilderAdapter(WebTemplateNewProjectWizard(AdonisProjectGenerator()))
