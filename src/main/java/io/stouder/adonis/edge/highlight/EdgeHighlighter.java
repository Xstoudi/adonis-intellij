package io.stouder.adonis.edge.highlight;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import io.stouder.adonis.edge.parsing.EdgeRawLexer;
import io.stouder.adonis.edge.parsing.EdgeTokenTypes;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.HashMap;

public class EdgeHighlighter extends SyntaxHighlighterBase {

    private static final Map<IElementType, TextAttributesKey> keys;

    private static final TextAttributesKey MUSTACHES = TextAttributesKey.createTextAttributesKey(
            "EDGE.MUSTACHES",
            DefaultLanguageHighlighterColors.BRACES
    );

    private static final TextAttributesKey COMMENTS = TextAttributesKey.createTextAttributesKey(
            "EDGE.COMMENTS",
            DefaultLanguageHighlighterColors.BLOCK_COMMENT
    );

    static {
        keys = new HashMap<>();
        keys.put(EdgeTokenTypes.MUSTACHE_OPEN, MUSTACHES);
        keys.put(EdgeTokenTypes.MUSTACHE_CLOSE, MUSTACHES);
        keys.put(EdgeTokenTypes.SAFE_MUSTACHE_OPEN, MUSTACHES);
        keys.put(EdgeTokenTypes.SAFE_MUSTACHE_CLOSE, MUSTACHES);
        keys.put(EdgeTokenTypes.ESCAPED_MUSTACHE_OPEN, MUSTACHES);
        keys.put(EdgeTokenTypes.ESCAPED_MUSTACHE_CLOSE, MUSTACHES);
        keys.put(EdgeTokenTypes.ESCAPED_SAFE_MUSTACHE_OPEN, MUSTACHES);
        keys.put(EdgeTokenTypes.ESCAPED_SAFE_MUSTACHE_CLOSE, MUSTACHES);
        keys.put(EdgeTokenTypes.COMMENT_MUSTACHE_OPEN, COMMENTS);
        keys.put(EdgeTokenTypes.COMMENT_MUSTACHE_CLOSE, COMMENTS);
        keys.put(EdgeTokenTypes.COMMENT_MUSTACHE_CONTENT, COMMENTS);
    }

    public EdgeHighlighter() {
        super();
    }

    @Override
    public @NotNull Lexer getHighlightingLexer() {
        return new EdgeRawLexer();
    }

    @Override
    public TextAttributesKey @NotNull [] getTokenHighlights(IElementType tokenType) {
        return pack(keys.get(tokenType));
    }
}
