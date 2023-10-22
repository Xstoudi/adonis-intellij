package io.stouder.adonis.edge.highlighter;

import com.intellij.ide.highlighter.HtmlFileHighlighter;
import com.intellij.lang.javascript.DialectOptionHolder;
import com.intellij.lang.javascript.JavascriptLanguage;
import com.intellij.lang.javascript.highlighting.JSHighlighter;
import com.intellij.lang.javascript.highlighting.JavaScriptHighlightDescriptor;
import com.intellij.lang.javascript.highlighting.TypeScriptHighlighter;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.XmlHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.ArrayUtil;
import io.stouder.adonis.edge.EdgeLanguage;
import io.stouder.adonis.edge.lexer.EdgeLexer;
import io.stouder.adonis.edge.psi.EdgeTokenTypes;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class EdgeSyntaxHighlighter extends HtmlFileHighlighter {
    private static final Map<IElementType, TextAttributesKey[]> KEYS = new HashMap<>();
    private static final JSHighlighter jsHighlighter = new JSHighlighter(DialectOptionHolder.JS_1_5);
    private static final TypeScriptHighlighter tsHighlighter = new TypeScriptHighlighter(false);
    private static final Map<Pair<TextAttributesKey, IElementType>, TextAttributesKey> tsKeyMap = new HashMap<>();

    private static void put(IElementType type, TextAttributesKey... keys) {
        KEYS.put(type, keys);
    }

    public EdgeSyntaxHighlighter() {
        for(IElementType type : new IElementType[] { EdgeTokenTypes.MUSTACHE_OPEN, EdgeTokenTypes.MUSTACHE_CLOSE }) {
            put(type, XmlHighlighterColors.HTML_CODE);
        }
        for(IElementType type : new IElementType[] { EdgeTokenTypes.MUSTACHE }) {
            put(type, tsHighlighter.getTokenHighlights(type));
        }
    }


    @Override
    public Lexer getHighlightingLexer() {
        return new EdgeLexer();
    }

    @Override
    public TextAttributesKey @NotNull [] getTokenHighlights(IElementType tokenType) {
        TextAttributesKey[] result = KEYS.get(tokenType);
        if(result == null) {
            result = super.getTokenHighlights(tokenType);
        } else {
            return result;
        }
        if(tokenType.getLanguage().is(EdgeLanguage.INSTANCE) || tokenType.getLanguage().is(JavascriptLanguage.INSTANCE)) {
            result = ArrayUtil.insert(result, 1, EdgeSyntaxHighlighterColors.EDGE_EXPRESSION);
        }
        return result;
    }

    /*private mapToTsKeys(TextAttributesKey[] tokenHighlights, IElementType tokenType) {
       // return Arrays.stream(tokenHighlights).map()
    }*/

    private TextAttributesKey getTsMappedKey(TextAttributesKey key, IElementType tokenType) {
        if(key.getExternalName().startsWith("JS.")) {
            return key;
        }
        return tsKeyMap.computeIfAbsent(Pair.create(key, tokenType), (pair) -> {
            TextAttributesKey[] jsHighlights = jsHighlighter.getTokenHighlights(pair.second);
            TextAttributesKey[] tsHighlights = tsHighlighter.getTokenHighlights(pair.second);
            TextAttributesKey jsKey = jsHighlights.length == 0 ? null : jsHighlights[jsHighlights.length - 1];
            TextAttributesKey tsKey = tsHighlights.length == 0 ? null : tsHighlights[tsHighlights.length - 1];
            return (pair.first.equals(jsKey) && tsKey != null) ? tsKey : pair.first;
        });
    }
}
