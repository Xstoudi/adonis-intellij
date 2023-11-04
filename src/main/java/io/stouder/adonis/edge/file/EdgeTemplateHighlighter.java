package io.stouder.adonis.edge.file;

import com.intellij.lang.Language;
import com.intellij.lang.javascript.JavaScriptFileType;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.ex.util.LayerDescriptor;
import com.intellij.openapi.editor.ex.util.LayeredLexerEditorHighlighter;
import com.intellij.openapi.fileTypes.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.templateLanguages.TemplateDataLanguageMappings;
import io.stouder.adonis.edge.EdgeLanguage;
import io.stouder.adonis.edge.highlight.EdgeHighlighter;
import io.stouder.adonis.edge.parsing.EdgeTokenTypes;

public class EdgeTemplateHighlighter extends LayeredLexerEditorHighlighter {
    public EdgeTemplateHighlighter(Project project, VirtualFile virtualFile, EditorColorsScheme scheme) {
        super(new EdgeHighlighter(), scheme);

        LanguageFileType languageFileType = null;
        if(project == null || virtualFile == null) {
            languageFileType = FileTypes.PLAIN_TEXT;
        } else {
            Language language = TemplateDataLanguageMappings.getInstance(project).getMapping(virtualFile);
            if(language != null) languageFileType = language.getAssociatedFileType();
            if(languageFileType == null) languageFileType = EdgeLanguage.getDefaultTemplateLang();
        }

        SyntaxHighlighter outerHighlighter = SyntaxHighlighterFactory.getSyntaxHighlighter(languageFileType, project, virtualFile);
        SyntaxHighlighter javascriptHighlighter = SyntaxHighlighterFactory.getSyntaxHighlighter(JavaScriptFileType.INSTANCE, project, virtualFile);

        assert outerHighlighter != null;
        assert javascriptHighlighter != null;

        this.registerLayer(EdgeTokenTypes.CONTENT, new LayerDescriptor(outerHighlighter, ""));
        this.registerLayer(EdgeTokenTypes.MUSTACHE_CONTENT, new LayerDescriptor(javascriptHighlighter, ""));
        this.registerLayer(EdgeTokenTypes.TAG_CONTENT, new LayerDescriptor(javascriptHighlighter, ""));
    }
}
