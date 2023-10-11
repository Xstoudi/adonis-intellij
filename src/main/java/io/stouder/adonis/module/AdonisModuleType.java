package io.stouder.adonis.module;

import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.ModuleTypeManager;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import io.stouder.adonis.AdonisIcons;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class AdonisModuleType extends ModuleType<AdonisModuleBuilder> {
    private static final String ID = "ADONIS_MODULE_TYPE";

    public AdonisModuleType() {
        super(ID);
    }

    public static AdonisModuleType getInstance() {
        return (AdonisModuleType) ModuleTypeManager.getInstance().findByID(ID);
    }


    @Override
    public @NotNull AdonisModuleBuilder createModuleBuilder() {
        return new AdonisModuleBuilder();
    }

    @Override
    public @NotNull @Nls(capitalization = Nls.Capitalization.Title) String getName() {
        return "Adonis project";
    }

    @Override
    public @NotNull @Nls(capitalization = Nls.Capitalization.Sentence) String getDescription() {
        return "Adonis project";
    }

    @Override
    public @NotNull Icon getNodeIcon(boolean isOpened) {
        return AdonisIcons.ADONIS;
    }

    @Override
    public ModuleWizardStep @NotNull [] createWizardSteps(
            @NotNull WizardContext wizardContext,
            @NotNull AdonisModuleBuilder moduleBuilder,
            @NotNull ModulesProvider modulesProvider
    ) {
        return super.createWizardSteps(wizardContext, moduleBuilder, modulesProvider);
    }
}
