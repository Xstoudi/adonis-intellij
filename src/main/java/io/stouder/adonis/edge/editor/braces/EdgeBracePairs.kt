package io.stouder.adonis.edge.editor.braces

import com.intellij.lang.BracePair
import io.stouder.adonis.edge.parsing.EdgeTokenTypes

object EdgeBracePairs {
    val BRACE_PAIRS = arrayOf(
        BracePair(EdgeTokenTypes.MUSTACHE_OPEN, EdgeTokenTypes.MUSTACHE_CLOSE, true),
        BracePair(EdgeTokenTypes.SAFE_MUSTACHE_OPEN, EdgeTokenTypes.SAFE_MUSTACHE_CLOSE, true),
        BracePair(EdgeTokenTypes.ESCAPED_MUSTACHE_OPEN, EdgeTokenTypes.ESCAPED_MUSTACHE_CLOSE, true),
        BracePair(EdgeTokenTypes.ESCAPED_SAFE_MUSTACHE_OPEN, EdgeTokenTypes.ESCAPED_SAFE_MUSTACHE_CLOSE, true),
        BracePair(EdgeTokenTypes.COMMENT_MUSTACHE_OPEN, EdgeTokenTypes.COMMENT_MUSTACHE_CLOSE, true)
    )
}