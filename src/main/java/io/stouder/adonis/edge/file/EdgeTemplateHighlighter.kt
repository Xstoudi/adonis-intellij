package io.stouder.adonis.edge.file

import com.intellij.lang.Language
import com.intellij.lang.javascript.JavaScriptFileType
import com.intellij.openapi.editor.colors.EditorColorsScheme
import com.intellij.openapi.editor.ex.util.LayerDescriptor
import com.intellij.openapi.editor.ex.util.LayeredLexerEditorHighlighter
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.fileTypes.FileTypeRegistry
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.templateLanguages.TemplateDataLanguageMappings
import io.stouder.adonis.edge.EdgeLanguage
import io.stouder.adonis.edge.highlight.EdgeHighlighter
import io.stouder.adonis.edge.parsing.EdgeTokenTypes

class EdgeTemplateHighlighter(project: Project?, virtualFile: VirtualFile?, scheme: EditorColorsScheme) :
    LayeredLexerEditorHighlighter(EdgeHighlighter(), scheme) {

    init {
        var languageFileType: FileType?
        if (project == null || virtualFile == null) {
            languageFileType = FileTypeRegistry.getInstance().getFileTypeByFileName(virtualFile?.name ?: "")
        } else {
            val language = TemplateDataLanguageMappings.getInstance(project).getMapping(virtualFile)
            languageFileType = language?.associatedFileType
            if (languageFileType == null) {
                languageFileType = EdgeLanguage.getDefaultTemplateLang()
            }
        }

        val outerHighlighter = SyntaxHighlighterFactory.getSyntaxHighlighter(languageFileType, project, virtualFile)
        val javascriptHighlighter = SyntaxHighlighterFactory.getSyntaxHighlighter(JavaScriptFileType.INSTANCE, project, virtualFile)

        checkNotNull(outerHighlighter) { "Failed to get syntax highlighter for languageFileType: $languageFileType" }
        checkNotNull(javascriptHighlighter) { "Failed to get syntax highlighter for JavaScriptFileType" }

        registerLayer(EdgeTokenTypes.CONTENT, LayerDescriptor(outerHighlighter, ""))
        registerLayer(EdgeTokenTypes.MUSTACHE_CONTENT, LayerDescriptor(javascriptHighlighter, ""))
        registerLayer(EdgeTokenTypes.TAG_CONTENT, LayerDescriptor(javascriptHighlighter, ""))
    }
}
