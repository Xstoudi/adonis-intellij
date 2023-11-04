package io.stouder.adonis.edge.parsing;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

public class EdgeParser implements PsiParser {
    @Override
    public @NotNull ASTNode parse(@NotNull IElementType root, @NotNull PsiBuilder builder) {
        final PsiBuilder.Marker rootMarker = builder.mark();

        this.getParsing(builder).parse();

        rootMarker.done(root);

        return builder.getTreeBuilt();
    }

    protected EdgeParsing getParsing(PsiBuilder builder) {
        return new EdgeParsing(builder);
    }
}
