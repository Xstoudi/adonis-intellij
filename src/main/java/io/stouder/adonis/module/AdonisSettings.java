package io.stouder.adonis.module;

import com.intellij.lang.javascript.boilerplate.NpmPackageProjectGenerator;
import com.intellij.openapi.util.Key;

public class AdonisSettings {
    public static Key<String> STARTER_KIT = Key.create("ADONIS_STARTER_KIT");
    public static Key<Boolean> INSTALL_DEPENDENCIES = Key.create("ADONIS_INSTALL_DEPENDENCIES");
    public static Key<Boolean> INITIALIZE_GIT = Key.create("ADONIS_INITIALIZE_GIT");

    public static String extractStarterKit(NpmPackageProjectGenerator.Settings settings) {
        String starterKit = settings.getUserData(STARTER_KIT);
        return (starterKit == null ? AdonisStarterKit.SLIM : AdonisStarterKit.fromName(starterKit)).getRepositoryUrl();
    }

    public static Boolean extractInstallDependencies(NpmPackageProjectGenerator.Settings settings) {
        Boolean installDependencies = settings.getUserData(INSTALL_DEPENDENCIES);
        return installDependencies == null || installDependencies;
    }

    public static Boolean extractInitializeGit(NpmPackageProjectGenerator.Settings settings) {
        Boolean initializeGit = settings.getUserData(INITIALIZE_GIT);
        return initializeGit == null || initializeGit;
    }
}
