package io.stouder.adonis.edge.editor.braces

import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import io.stouder.adonis.edge.parsing.EdgeTokenTypes
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

class EdgeBraceMatcher : PairedBraceMatcher {
    companion object {
        private val BRACE_PAIRS = arrayOf(
            BracePair(EdgeTokenTypes.MUSTACHE_OPEN, EdgeTokenTypes.MUSTACHE_CLOSE, true),
            BracePair(EdgeTokenTypes.SAFE_MUSTACHE_OPEN, EdgeTokenTypes.SAFE_MUSTACHE_CLOSE, true),
            BracePair(EdgeTokenTypes.ESCAPED_MUSTACHE_OPEN, EdgeTokenTypes.ESCAPED_MUSTACHE_CLOSE, true),
            BracePair(EdgeTokenTypes.ESCAPED_SAFE_MUSTACHE_OPEN, EdgeTokenTypes.ESCAPED_SAFE_MUSTACHE_CLOSE, true),
            BracePair(EdgeTokenTypes.COMMENT_MUSTACHE_OPEN, EdgeTokenTypes.COMMENT_MUSTACHE_CLOSE, true)
        )
    }

    override fun getPairs(): @NotNull Array<BracePair> {
        return BRACE_PAIRS
    }

    override fun isPairedBracesAllowedBeforeType(@NotNull lbraceType: IElementType, @Nullable contextType: IElementType?): Boolean {
        return true
    }

    override fun getCodeConstructStart(file: PsiFile, openingBraceOffset: Int): Int {
        return openingBraceOffset
    }

}