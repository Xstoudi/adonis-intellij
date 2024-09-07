package io.stouder.adonis.edge.parsing

import com.intellij.lexer.MergingLexerAdapter
import com.intellij.psi.tree.TokenSet

class EdgeLexer(tokensToMerge: TokenSet?) : MergingLexerAdapter(EdgeRawLexer(), tokensToMerge) {

    companion object {
        private val TOKENS_TO_MERGE = TokenSet.create(
            EdgeTokenTypes.CONTENT,
            EdgeTokenTypes.MUSTACHE_CONTENT,
            EdgeTokenTypes.COMMENT_MUSTACHE_CONTENT,
            EdgeTokenTypes.TAG_CONTENT,
            EdgeTokenTypes.WHITE_SPACE
        )
    }

    constructor() : this(TOKENS_TO_MERGE)
}
