package io.stouder.adonis.module;

import com.intellij.ide.util.projectWizard.WebTemplateNewProjectWizard;
import com.intellij.ide.wizard.GeneratorNewProjectWizardBuilderAdapter;

public class AdonisModuleBuilder extends GeneratorNewProjectWizardBuilderAdapter {

    public AdonisModuleBuilder() {
        super(new WebTemplateNewProjectWizard(new AdonisProjectGenerator()));
    }
}
