package io.stouder.adonis.edge.psi;

import com.intellij.lang.javascript.psi.JSElementVisitor;
import com.intellij.lang.javascript.psi.JSEmbeddedContent;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.impl.source.tree.LazyParseablePsiElement;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EdgeJSLazyPsiElement extends LazyParseablePsiElement implements JSEmbeddedContent {
    public EdgeJSLazyPsiElement(@NotNull IElementType type, @Nullable CharSequence buffer)  {
        super(type, buffer);
    }

    public void accept(@NotNull PsiElementVisitor visitor) { //TODO: remove this method!!
        if(visitor instanceof JSElementVisitor jsVisitor) {
            jsVisitor.visitJSEmbeddedContent(this);
        } else { // TODO: Svelte as a SvelteVisitor here, check why
            super.accept(visitor);
        }
    }

    @Override
    public String toString() {
        return "Edge: " + this.getElementType();
    }

}
