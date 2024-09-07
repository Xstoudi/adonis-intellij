package io.stouder.adonis.edge.parsing

import com.intellij.psi.tree.IElementType
import io.stouder.adonis.edge.EdgeLanguage

class EdgeElementType(debugName: String) : IElementType(debugName, EdgeLanguage.INSTANCE) {
    override fun toString(): String {
        return "[Edge] ${super.toString()}"
    }
}
