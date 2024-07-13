package io.stouder.adonis.edge.parsing

import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet

object EdgeTokenTypes {
    val STATEMENTS: IElementType = EdgeCompositeElementType("STATEMENTS")
    val TAG: IElementType = EdgeCompositeElementType("TAG")
    val MUSTACHE: IElementType = EdgeCompositeElementType("MUSTACHE")
    val COMMENT_MUSTACHE: IElementType = EdgeCompositeElementType("COMMENT_MUSTACHE")

    val MUSTACHE_CONTENT: IElementType = EdgeElementType("MUSTACHE_CONTENT")
    val COMMENT_MUSTACHE_CONTENT: IElementType = EdgeElementType("COMMENT_MUSTACHE_CONTENT")

    val MUSTACHE_OPEN: IElementType = EdgeElementType("MUSTACHE_OPEN")
    val MUSTACHE_CLOSE: IElementType = EdgeElementType("MUSTACHE_CLOSE")

    val SAFE_MUSTACHE_OPEN: IElementType = EdgeElementType("SAFE_MUSTACHE_OPEN")
    val SAFE_MUSTACHE_CLOSE: IElementType = EdgeElementType("SAFE_MUSTACHE_CLOSE")

    val ESCAPED_MUSTACHE_OPEN: IElementType = EdgeElementType("ESCAPED_MUSTACHE_OPEN")
    val ESCAPED_MUSTACHE_CLOSE: IElementType = EdgeElementType("ESCAPED_MUSTACHE_CLOSE")

    val ESCAPED_SAFE_MUSTACHE_OPEN: IElementType = EdgeElementType("ESCAPED_SAFE_MUSTACHE_OPEN")
    val ESCAPED_SAFE_MUSTACHE_CLOSE: IElementType = EdgeElementType("ESCAPED_SAFE_MUSTACHE_CLOSE")

    val COMMENT_MUSTACHE_OPEN: IElementType = EdgeElementType("COMMENT_MUSTACHE_OPEN")
    val COMMENT_MUSTACHE_CLOSE: IElementType = EdgeElementType("COMMENT_MUSTACHE_CLOSE")

    val TAG_NAME: IElementType = EdgeElementType("TAG_NAME")
    val TAG_CONTENT_OPEN: IElementType = EdgeElementType("TAG_CONTENT_OPEN")
    val TAG_CONTENT_CLOSE: IElementType = EdgeElementType("TAG_CONTENT_CLOSE")

    val TAG_CONTENT: IElementType = EdgeElementType("TAG_CONTENT")
    val NEWLINE: IElementType = EdgeElementType("NEWLINE")
    val WHITE_SPACE: IElementType = EdgeElementType("WHITE_SPACE")
    val CONTENT: IElementType = EdgeElementType("CONTENT")
    val OUTER_ELEMENT_TYPE: IElementType = EdgeElementType("OUTER_ELEMENT_TYPE")

    val INVALID: IElementType = EdgeElementType("INVALID")

    val WHITESPACES: TokenSet = TokenSet.create(WHITE_SPACE)
    val COMMENTS: TokenSet = TokenSet.create()
}
