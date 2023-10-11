package io.stouder.adonis.module;

import com.intellij.ide.util.projectWizard.WebTemplateNewProjectWizard;
import com.intellij.ide.wizard.GeneratorNewProjectWizard;
import com.intellij.ide.wizard.GeneratorNewProjectWizardBuilderAdapter;
import org.jetbrains.annotations.NotNull;

public class AdonisModuleBuilder extends GeneratorNewProjectWizardBuilderAdapter {

    public AdonisModuleBuilder() {
        super(new WebTemplateNewProjectWizard(new AdonisProjectGenerator()));
    }
}
