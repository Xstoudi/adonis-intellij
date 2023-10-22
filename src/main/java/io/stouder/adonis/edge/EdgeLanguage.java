package io.stouder.adonis.edge;

import com.intellij.javascript.web.html.WebFrameworkHtmlDialect;
import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.psi.templateLanguages.TemplateLanguage;
import io.stouder.adonis.AdonisBundle;
import org.jetbrains.annotations.NotNull;

public class EdgeLanguage extends WebFrameworkHtmlDialect {
    public static final EdgeLanguage INSTANCE = new EdgeLanguage();

    public EdgeLanguage() {
        super("Edge");
    }

    @Override
    public @NotNull String getDisplayName() {
        return AdonisBundle.message("adonis.edge.name");
    }

}
