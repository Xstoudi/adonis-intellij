package io.stouder.adonis.edge.parsing

import com.intellij.lang.ASTNode
import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.psi.tree.IElementType

class EdgeParser : PsiParser {
    override fun parse(root: IElementType, builder: PsiBuilder): ASTNode {
        val rootMarker = builder.mark()

        getParsing(builder).parse()

        rootMarker.done(root)

        return builder.treeBuilt
    }

    private fun getParsing(builder: PsiBuilder): EdgeParsing {
        return EdgeParsing(builder)
    }
}
