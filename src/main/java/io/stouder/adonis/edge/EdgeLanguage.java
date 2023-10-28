package io.stouder.adonis.edge;

import com.intellij.lang.html.HTMLLanguage;
import com.intellij.openapi.fileTypes.LanguageFileType;
import io.stouder.adonis.AdonisBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EdgeLanguage extends HTMLLanguage {
    public static final EdgeLanguage INSTANCE = new EdgeLanguage();

    public EdgeLanguage() {
        super(HTMLLanguage.INSTANCE, "Edge");
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
