package io.stouder.adonis.edge.language;

import com.intellij.lang.Language;

public class EdgeLanguage extends Language {
    public static final EdgeLanguage INSTANCE = new EdgeLanguage();

    private EdgeLanguage() {
        super("Edge");
    }
}
