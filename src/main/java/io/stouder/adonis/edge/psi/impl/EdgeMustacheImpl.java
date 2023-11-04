package io.stouder.adonis.edge.psi.impl;

import com.intellij.lang.ASTNode;
import io.stouder.adonis.edge.psi.EdgeMustache;
import org.jetbrains.annotations.NotNull;

public class EdgeMustacheImpl extends EdgePsiElementImpl implements EdgeMustache {
    public EdgeMustacheImpl(@NotNull ASTNode node) {
        super(node);
    }
}
