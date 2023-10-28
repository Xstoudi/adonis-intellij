package io.stouder.adonis.edge;

import com.intellij.ide.highlighter.HtmlFileType;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.openapi.util.NlsSafe;
import io.stouder.adonis.AdonisBundle;
import io.stouder.adonis.AdonisIcons;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class EdgeFileType extends HtmlFileType {

    public static final EdgeFileType INSTANCE = new EdgeFileType();

    public EdgeFileType() {
        super(EdgeLanguage.INSTANCE);
    }

    @Override
    public @NonNls @NotNull String getName() {
        return "Edge";
    }

    @Override
    public @NlsContexts.Label @NotNull String getDescription() {
        return AdonisBundle.message("adonis.edge.description");
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
