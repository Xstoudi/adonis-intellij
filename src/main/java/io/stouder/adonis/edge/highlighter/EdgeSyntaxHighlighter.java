package io.stouder.adonis.edge.highlighter;

import com.intellij.ide.highlighter.HtmlFileHighlighter;
import com.intellij.lang.javascript.highlighting.JSHighlighter;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.XmlHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.tree.IElementType;
import io.stouder.adonis.edge.lexer.EdgeLexer;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;
import static io.stouder.adonis.edge.psi.EdgeTokenTypes.COMMENT_TOKENS;
import static io.stouder.adonis.edge.psi.EdgeTokenTypes.MUSTACHE_TOKENS;

public class EdgeSyntaxHighlighter extends HtmlFileHighlighter {

    private final TextAttributesKey[] MUSTACHES_KEYS = pack(
            createTextAttributesKey("EDGE_MUSTACHES", JSHighlighter.JS_KEYWORD)
    );
    private final TextAttributesKey[] COMMENT = pack(
            createTextAttributesKey("EDGE_COMMENT", XmlHighlighterColors.HTML_COMMENT)
    );

    @Override
    @NotNull
    public Lexer getHighlightingLexer() {
        return new EdgeLexer();
    }

    @Override
    public TextAttributesKey @NotNull [] getTokenHighlights(IElementType tokenType) {
        if(MUSTACHE_TOKENS.contains(tokenType)) {
            return MUSTACHES_KEYS;
        }
        if(COMMENT_TOKENS.contains(tokenType)) {
            return COMMENT;
        }

        return super.getTokenHighlights(tokenType);
    }
}
