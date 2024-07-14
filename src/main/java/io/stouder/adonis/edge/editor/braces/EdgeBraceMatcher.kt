package io.stouder.adonis.edge.editor.braces

import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

class EdgeBraceMatcher : PairedBraceMatcher {
    override fun getPairs(): @NotNull Array<BracePair> {
        return EdgeBracePairs.BRACE_PAIRS
    }

    override fun isPairedBracesAllowedBeforeType(@NotNull lbraceType: IElementType, @Nullable contextType: IElementType?): Boolean {
        return true
    }

    override fun getCodeConstructStart(file: PsiFile, openingBraceOffset: Int): Int {
        return openingBraceOffset
    }
}