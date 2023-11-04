package io.stouder.adonis.edge.editor;

import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import io.stouder.adonis.edge.parsing.EdgeTokenTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EdgeBraceMatcher implements PairedBraceMatcher {
    private static final BracePair[] BRACE_PAIRS = new BracePair[]{
            new BracePair(EdgeTokenTypes.MUSTACHE_OPEN, EdgeTokenTypes.MUSTACHE_CLOSE, true),
            new BracePair(EdgeTokenTypes.SAFE_MUSTACHE_OPEN, EdgeTokenTypes.SAFE_MUSTACHE_CLOSE, true),
    };
    @Override
    public BracePair @NotNull []  getPairs() {
        return BRACE_PAIRS;
    }

    @Override
    public boolean isPairedBracesAllowedBeforeType(@NotNull IElementType lbraceType, @Nullable IElementType contextType) {
        return true;
    }

    @Override
    public int getCodeConstructStart(PsiFile file, int openingBraceOffset) {
        return openingBraceOffset;
    }
}
