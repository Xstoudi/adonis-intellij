package io.stouder.adonis.edge;

import com.intellij.javascript.web.html.WebFrameworkHtmlFileType;

public class EdgeFileType extends WebFrameworkHtmlFileType {

    public static final EdgeFileType INSTANCE = new EdgeFileType();

    public EdgeFileType() {
        super(EdgeLanguage.INSTANCE, "Edge", "edge");
    }
/*
    private EdgeFileType() {
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
    }*/
}
