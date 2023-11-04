package io.stouder.adonis.edge.parsing;

import com.intellij.lang.ASTFactory;
import com.intellij.psi.impl.source.tree.LeafElement;
import com.intellij.psi.templateLanguages.OuterLanguageElementImpl;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EdgeAstFactory extends ASTFactory {
    public @Nullable LeafElement createLeaf(@NotNull IElementType type, @NotNull CharSequence text) {
        if(type == EdgeTokenTypes.OUTER_ELEMENT_TYPE || type == EdgeTokenTypes.MUSTACHE) {
            return new OuterLanguageElementImpl(type, text);
        }
        return super.createLeaf(type, text);
    }

}
