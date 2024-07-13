package io.stouder.adonis.edge.psi

import com.intellij.psi.PsiElement

interface EdgePsiElement : PsiElement {
    fun getName(): String?
}
