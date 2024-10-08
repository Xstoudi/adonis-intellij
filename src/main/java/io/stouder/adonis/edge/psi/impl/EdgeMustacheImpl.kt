package io.stouder.adonis.edge.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import io.stouder.adonis.edge.psi.EdgeMustache
import io.stouder.adonis.edge.psi.EdgePsiElement

class EdgeMustacheImpl(node: ASTNode) : ASTWrapperPsiElement(node), EdgeMustache, EdgePsiElement
