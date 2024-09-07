package io.stouder.adonis.edge.parsing

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType

class EdgeParsing(private val builder: PsiBuilder) {

    fun parse() {
        while (!builder.eof()) {
            parseRoot()

            if (builder.eof()) {
                break
            }

            val problemOffset = builder.currentOffset

            if (builder.currentOffset == problemOffset) {
                // none of our error checks advanced the lexer, do it manually before we
                // try and resume parsing to avoid an infinite loop
                val problemMark = builder.mark()
                builder.advanceLexer()
                problemMark.error("Invalid")
            }
        }
    }

    private fun parseRoot() {
        parseProgram()
    }

    private fun parseProgram() {
        parseStatements()
    }

    private fun parseStatements() {
        val statementsMarker = builder.mark()

        while (true) {
            val optionalStatementMarker = builder.mark()
            if (parseStatement()) {
                optionalStatementMarker.drop()
            } else {
                optionalStatementMarker.rollbackTo()
                break
            }
        }

        statementsMarker.done(EdgeTokenTypes.STATEMENTS)
    }

    private fun parseStatement(): Boolean {
        val tokenType = builder.tokenType
        return when (tokenType) {
            EdgeTokenTypes.MUSTACHE_OPEN -> {
                parseMustache(EdgeTokenTypes.MUSTACHE_OPEN, EdgeTokenTypes.MUSTACHE_CLOSE)
                true
            }
            EdgeTokenTypes.SAFE_MUSTACHE_OPEN -> {
                parseMustache(EdgeTokenTypes.SAFE_MUSTACHE_OPEN, EdgeTokenTypes.SAFE_MUSTACHE_CLOSE)
                true
            }
            EdgeTokenTypes.ESCAPED_MUSTACHE_OPEN -> {
                parseMustache(EdgeTokenTypes.ESCAPED_MUSTACHE_OPEN, EdgeTokenTypes.ESCAPED_MUSTACHE_CLOSE)
                true
            }
            EdgeTokenTypes.ESCAPED_SAFE_MUSTACHE_OPEN -> {
                parseMustache(EdgeTokenTypes.ESCAPED_SAFE_MUSTACHE_OPEN, EdgeTokenTypes.ESCAPED_SAFE_MUSTACHE_CLOSE)
                true
            }
            EdgeTokenTypes.COMMENT_MUSTACHE_OPEN -> {
                parseMustacheComment()
                true
            }
            EdgeTokenTypes.TAG_NAME -> {
                parseTag()
                true
            }
            EdgeTokenTypes.CONTENT -> {
                builder.advanceLexer()
                true
            }
            else -> false
        }
    }

    private fun parseMustache(openMustache: IElementType, closeMustache: IElementType) {
        val mustacheMarker = builder.mark()

        parseLeafToken(openMustache)
        parseLeafToken(EdgeTokenTypes.MUSTACHE_CONTENT)
        parseLeafTokenGreedy(closeMustache)

        mustacheMarker.done(EdgeTokenTypes.MUSTACHE)
    }

    private fun parseMustacheComment() {
        val mustacheCommentMarker = builder.mark()

        parseLeafToken(EdgeTokenTypes.COMMENT_MUSTACHE_OPEN)
        parseLeafToken(EdgeTokenTypes.COMMENT_MUSTACHE_CONTENT)
        parseLeafTokenGreedy(EdgeTokenTypes.COMMENT_MUSTACHE_CLOSE)

        mustacheCommentMarker.done(EdgeTokenTypes.COMMENT_MUSTACHE)
    }

    private fun parseTag() {
        val tagMarker = builder.mark()

        parseLeafToken(EdgeTokenTypes.TAG_NAME)
        when (val nextToken = builder.tokenType) {
            EdgeTokenTypes.TAG_CONTENT_OPEN -> {
                parseLeafToken(EdgeTokenTypes.TAG_CONTENT_OPEN)
                parseLeafToken(EdgeTokenTypes.TAG_CONTENT)
                parseLeafToken(EdgeTokenTypes.TAG_CONTENT_CLOSE)
                parseLeafToken(EdgeTokenTypes.NEWLINE)
            }
            EdgeTokenTypes.NEWLINE -> parseLeafToken(EdgeTokenTypes.NEWLINE)
            else -> tagMarker.error("Unexpected token: $nextToken")
        }

        tagMarker.done(EdgeTokenTypes.TAG)
    }

    private fun parseLeafToken(leafTokenType: IElementType): Boolean {
        val leafTokenMark = builder.mark()
        val currentTokenType = builder.tokenType
        return when (currentTokenType) {
            leafTokenType -> {
                builder.advanceLexer()
                leafTokenMark.done(leafTokenType)
                true
            }
            EdgeTokenTypes.INVALID -> {
                while (!builder.eof() && builder.tokenType == EdgeTokenTypes.INVALID) {
                    builder.advanceLexer()
                }
                recordLeafTokenError(EdgeTokenTypes.INVALID, leafTokenMark)
                false
            }
            else -> {
                recordLeafTokenError(leafTokenType, leafTokenMark)
                false
            }
        }
    }

    private fun parseLeafTokenGreedy(elementType: IElementType) {
        if (builder.tokenType != elementType) {
            val unexpectedTokensMarker = builder.mark()
            while (!builder.eof() && builder.tokenType != elementType) {
                builder.advanceLexer()
            }
            unexpectedTokensMarker.error("Expected token: $elementType")
        }

        if (!builder.eof() && builder.tokenType == elementType) {
            parseLeafToken(elementType)
        }
    }

    private fun recordLeafTokenError(expectedToken: IElementType, unexpectedTokenMarker: PsiBuilder.Marker) {
        if (expectedToken is EdgeElementType) {
            unexpectedTokenMarker.error("Expected token: $expectedToken")
        } else {
            unexpectedTokenMarker.error("Unexpected token")
        }
    }
}
