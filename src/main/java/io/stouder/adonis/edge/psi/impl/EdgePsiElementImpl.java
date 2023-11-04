package io.stouder.adonis.edge.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import io.stouder.adonis.edge.psi.EdgePsiElement;
import org.jetbrains.annotations.NotNull;

public class EdgePsiElementImpl extends ASTWrapperPsiElement implements EdgePsiElement {
    public EdgePsiElementImpl(@NotNull ASTNode node) {
        super(node);
    }
}
