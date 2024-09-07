package io.stouder.adonis.edge.parsing

import com.intellij.lang.ASTFactory
import com.intellij.psi.impl.source.tree.LeafElement
import com.intellij.psi.templateLanguages.OuterLanguageElementImpl
import com.intellij.psi.tree.IElementType

class EdgeAstFactory : ASTFactory() {
    override fun createLeaf(type: IElementType, text: CharSequence): LeafElement? {
        if (type == EdgeTokenTypes.OUTER_ELEMENT_TYPE || type == EdgeTokenTypes.MUSTACHE) {
            return OuterLanguageElementImpl(type, text)
        }
        return super.createLeaf(type, text)
    }
}
