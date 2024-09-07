package io.stouder.adonis.edge.parsing

import com.intellij.psi.tree.IElementType
import io.stouder.adonis.edge.EdgeLanguage

class EdgeCompositeElementType(debugName: String) : IElementType(debugName, EdgeLanguage.INSTANCE)
