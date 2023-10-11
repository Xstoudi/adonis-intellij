package io.stouder.adonis.edge;

import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.openapi.util.NlsSafe;
import io.stouder.adonis.AdonisIcons;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class EdgeFileType extends LanguageFileType {

    public static final EdgeFileType INSTANCE = new EdgeFileType();

    private EdgeFileType() {
        super(EdgeLanguage.INSTANCE);
    }

    @Override
    public @NonNls @NotNull String getName() {
        return "Edge file";
    }

    @Override
    public @NlsContexts.Label @NotNull String getDescription() {
        return "Edge language file";
    }

    @Override
    public @NlsSafe @NotNull String getDefaultExtension() {
        return "edge";
    }

    @Override
    public Icon getIcon() {
        return AdonisIcons.EDGE;
    }
}
