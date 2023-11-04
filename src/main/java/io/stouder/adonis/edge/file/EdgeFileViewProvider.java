package io.stouder.adonis.edge.file;

import com.intellij.lang.Language;
import com.intellij.lang.LanguageParserDefinitions;
import com.intellij.lang.ParserDefinition;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.LanguageSubstitutors;
import com.intellij.psi.MultiplePsiFilesPerDocumentFileViewProvider;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.source.PsiFileImpl;
import com.intellij.psi.templateLanguages.ConfigurableTemplateLanguageFileViewProvider;
import com.intellij.psi.templateLanguages.TemplateDataElementType;
import com.intellij.psi.templateLanguages.TemplateDataLanguageMappings;
import com.intellij.psi.tree.IElementType;
import io.stouder.adonis.edge.EdgeLanguage;
import io.stouder.adonis.edge.parsing.EdgeTokenTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class EdgeFileViewProvider extends MultiplePsiFilesPerDocumentFileViewProvider implements ConfigurableTemplateLanguageFileViewProvider {

    private final Language baseLanguage;

    private final Language templateLanguage;
    private final TemplateDataElementType templateDataElementType = new TemplateDataElementType("EDGE_TEMPLATE_DATA", EdgeLanguage.INSTANCE, EdgeTokenTypes.CONTENT, EdgeTokenTypes.OUTER_ELEMENT_TYPE);

    public EdgeFileViewProvider(PsiManager manager, VirtualFile file, boolean physical, Language baseLanguage) {
        this(manager, file, physical, baseLanguage, getTemplateDataLanguage(manager, file));
    }
    public EdgeFileViewProvider(PsiManager manager, VirtualFile virtualFile, boolean eventSystemEnabled, Language baseLanguage, Language templateLanguage) {
        super(manager, virtualFile, eventSystemEnabled);
        this.baseLanguage = baseLanguage;
        this.templateLanguage = templateLanguage;
    }

    @Override
    public boolean supportsIncrementalReparse(@NotNull Language rootLanguage) {
        return false;
    }

    private static @NotNull Language getTemplateDataLanguage(PsiManager manager, VirtualFile file) {
        Language dataLang = TemplateDataLanguageMappings.getInstance(manager.getProject()).getMapping(file);
        if (dataLang == null) {
            dataLang = EdgeLanguage.getDefaultTemplateLang().getLanguage();
        }

        Language substituteLang = LanguageSubstitutors.getInstance().substituteLanguage(dataLang, file, manager.getProject());

        if (TemplateDataLanguageMappings.getTemplateableLanguages().contains(substituteLang)) {
            dataLang = substituteLang;
        }

        return dataLang;
    }

    @Override
    public @NotNull Language getBaseLanguage() {
        return this.baseLanguage;
    }

    @Override
    public @NotNull Language getTemplateDataLanguage() {
        return this.templateLanguage;
    }

    @Override
    public @NotNull Set<Language> getLanguages() {
        return Set.of(
                this.baseLanguage,
                this.templateLanguage
        );
    }

    @Override
    protected @NotNull MultiplePsiFilesPerDocumentFileViewProvider cloneInner(@NotNull VirtualFile virtualFile) {
        return new EdgeFileViewProvider(getManager(), virtualFile, false, this.baseLanguage, this.templateLanguage);
    }

    @Override
    protected PsiFile createFile(@NotNull Language lang) {
        ParserDefinition parserDefinition = LanguageParserDefinitions.INSTANCE.forLanguage(lang);
        if (parserDefinition == null) {
            return null;
        }

        if (this.getLanguages().contains(lang)) {
            PsiFile file = parserDefinition.createFile(this);
            IElementType type = this.getContentElementType(lang);
            if (type != null) {
                ((PsiFileImpl) file).setContentElementType(type);
            }
            return file;
        }
        return null;
    }

    @Override
    public @Nullable IElementType getContentElementType(@NotNull Language language) {
        if (language.is(this.templateLanguage))
            return this.templateDataElementType;
        return null;
    }
}
