package io.stouder.adonis.edge;

import com.intellij.ide.highlighter.HtmlFileType;
import com.intellij.lang.InjectableLanguage;
import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.psi.templateLanguages.TemplateLanguage;
import io.stouder.adonis.AdonisBundle;
import io.stouder.adonis.edge.file.EdgeFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EdgeLanguage extends Language implements TemplateLanguage, InjectableLanguage {
    public static final EdgeLanguage INSTANCE = new EdgeLanguage();

    public static LanguageFileType getDefaultTemplateLang() {
        return HtmlFileType.INSTANCE;
    }

    public EdgeLanguage() {
        super("Edge");
    }

    public EdgeLanguage(@Nullable Language baseLanguage, @NotNull String ID, String... mimeTypes) {
        super(baseLanguage, ID, mimeTypes);
    }

    @Override
    public @NotNull String getDisplayName() {
        return AdonisBundle.message("adonis.edge.name");
    }

    @Override
    public @Nullable LanguageFileType getAssociatedFileType() {
        return EdgeFileType.INSTANCE;
    }


}
