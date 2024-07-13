package io.stouder.adonis.edge.highlight

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import io.stouder.adonis.edge.parsing.EdgeRawLexer
import io.stouder.adonis.edge.parsing.EdgeTokenTypes
import java.util.HashMap

class EdgeHighlighter : SyntaxHighlighterBase() {

    private val keys: MutableMap<IElementType, TextAttributesKey> = HashMap()

    init {
        keys[EdgeTokenTypes.MUSTACHE_OPEN] = MUSTACHES
        keys[EdgeTokenTypes.MUSTACHE_CLOSE] = MUSTACHES
        keys[EdgeTokenTypes.SAFE_MUSTACHE_OPEN] = MUSTACHES
        keys[EdgeTokenTypes.SAFE_MUSTACHE_CLOSE] = MUSTACHES
        keys[EdgeTokenTypes.ESCAPED_MUSTACHE_OPEN] = MUSTACHES
        keys[EdgeTokenTypes.ESCAPED_MUSTACHE_CLOSE] = MUSTACHES
        keys[EdgeTokenTypes.ESCAPED_SAFE_MUSTACHE_OPEN] = MUSTACHES
        keys[EdgeTokenTypes.ESCAPED_SAFE_MUSTACHE_CLOSE] = MUSTACHES
        keys[EdgeTokenTypes.COMMENT_MUSTACHE_OPEN] = COMMENTS
        keys[EdgeTokenTypes.COMMENT_MUSTACHE_CLOSE] = COMMENTS
        keys[EdgeTokenTypes.COMMENT_MUSTACHE_CONTENT] = COMMENTS
        keys[EdgeTokenTypes.TAG_CONTENT_OPEN] = MUSTACHES
        keys[EdgeTokenTypes.TAG_CONTENT_CLOSE] = MUSTACHES
        keys[EdgeTokenTypes.TAG_NAME] = TAG
    }

    override fun getHighlightingLexer(): Lexer {
        return EdgeRawLexer()
    }

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> {
        return pack(keys[tokenType])
    }

    companion object {
        private val MUSTACHES = TextAttributesKey.createTextAttributesKey(
            "EDGE.MUSTACHES",
            DefaultLanguageHighlighterColors.KEYWORD
        )

        private val COMMENTS = TextAttributesKey.createTextAttributesKey(
            "EDGE.COMMENTS",
            DefaultLanguageHighlighterColors.BLOCK_COMMENT
        )

        private val TAG = TextAttributesKey.createTextAttributesKey(
            "EDGE.TAG",
            DefaultLanguageHighlighterColors.KEYWORD
        )
    }
}
