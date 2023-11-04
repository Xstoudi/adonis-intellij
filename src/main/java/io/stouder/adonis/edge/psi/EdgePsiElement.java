package io.stouder.adonis.edge.psi;

import com.intellij.openapi.util.NlsSafe;
import com.intellij.psi.PsiElement;

public interface EdgePsiElement extends PsiElement {
    @NlsSafe
    String getName();
}
