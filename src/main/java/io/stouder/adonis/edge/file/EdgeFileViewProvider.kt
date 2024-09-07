package io.stouder.adonis.edge.file

import com.intellij.lang.Language
import com.intellij.lang.LanguageParserDefinitions
import com.intellij.lang.ParserDefinition
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.LanguageSubstitutors
import com.intellij.psi.MultiplePsiFilesPerDocumentFileViewProvider
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.impl.source.PsiFileImpl
import com.intellij.psi.templateLanguages.ConfigurableTemplateLanguageFileViewProvider
import com.intellij.psi.templateLanguages.TemplateDataElementType
import com.intellij.psi.templateLanguages.TemplateDataLanguageMappings
import com.intellij.psi.tree.IElementType
import io.stouder.adonis.edge.EdgeLanguage
import io.stouder.adonis.edge.parsing.EdgeTokenTypes

class EdgeFileViewProvider(
    manager: PsiManager,
    virtualFile: VirtualFile,
    eventSystemEnabled: Boolean,
    private val baseLanguage: Language,
    private val templateLanguage: Language = getTemplateDataLanguage(manager, virtualFile)
) : MultiplePsiFilesPerDocumentFileViewProvider(manager, virtualFile, eventSystemEnabled),
    ConfigurableTemplateLanguageFileViewProvider {

    private val templateDataElementType = TemplateDataElementType("EDGE_TEMPLATE_DATA", EdgeLanguage.INSTANCE, EdgeTokenTypes.CONTENT, EdgeTokenTypes.OUTER_ELEMENT_TYPE)

    constructor(manager: PsiManager, file: VirtualFile, physical: Boolean, baseLanguage: Language) : this(
        manager, file, physical, baseLanguage, getTemplateDataLanguage(manager, file)
    )

    override fun supportsIncrementalReparse(rootLanguage: Language): Boolean {
        return false
    }

    companion object {
        private fun getTemplateDataLanguage(manager: PsiManager, file: VirtualFile): Language {
            var dataLang = TemplateDataLanguageMappings.getInstance(manager.project).getMapping(file)
            if (dataLang == null) {
                dataLang = EdgeLanguage.getDefaultTemplateLang().language
            }

            val substituteLang = LanguageSubstitutors.getInstance().substituteLanguage(dataLang, file, manager.project)

            if (TemplateDataLanguageMappings.getTemplateableLanguages().contains(substituteLang)) {
                dataLang = substituteLang
            }

            return dataLang
        }
    }

    override fun getBaseLanguage(): Language {
        return baseLanguage
    }

    override fun getTemplateDataLanguage(): Language {
        return templateLanguage
    }

    override fun getLanguages(): Set<Language> {
        return setOf(baseLanguage, templateLanguage)
    }

    override fun cloneInner(virtualFile: VirtualFile): MultiplePsiFilesPerDocumentFileViewProvider {
        return EdgeFileViewProvider(manager, virtualFile, false, baseLanguage, templateLanguage)
    }

    override fun createFile(lang: Language): PsiFile? {
        val parserDefinition: ParserDefinition = LanguageParserDefinitions.INSTANCE.forLanguage(lang) ?: return null

        if (languages.contains(lang)) {
            val file = parserDefinition.createFile(this)
            val type: IElementType? = getContentElementType(lang)
            if (type != null) {
                (file as PsiFileImpl).setContentElementType(type)
            }
            return file
        }
        return null
    }

    @Suppress("UnstableApiUsage")
    override fun getContentElementType(language: Language): IElementType? {
        return if (language.isKindOf(templateLanguage)) templateDataElementType else null
    }
}
