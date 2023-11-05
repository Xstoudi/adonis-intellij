package io.stouder.adonis.edge.file;

import com.intellij.ide.highlighter.XmlLikeFileType;
import com.intellij.openapi.fileTypes.TemplateLanguageFileType;
import io.stouder.adonis.AdonisBundle;
import io.stouder.adonis.AdonisIcons;
import io.stouder.adonis.edge.EdgeLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class EdgeFileType extends XmlLikeFileType implements TemplateLanguageFileType {

    public static final EdgeFileType INSTANCE = new EdgeFileType();

    public EdgeFileType() {
        super(EdgeLanguage.INSTANCE);
    }

    @Override
    public @NonNls @NotNull String getName() {
        return "Edge";
    }

    @Override
    public @NotNull String getDescription() {
        return AdonisBundle.message("adonis.edge.description");
    }

    @Override
    public @NotNull String getDefaultExtension() {
        return "edge";
    }

    @Override
    public Icon getIcon() {
        return AdonisIcons.EDGE;
    }
}
