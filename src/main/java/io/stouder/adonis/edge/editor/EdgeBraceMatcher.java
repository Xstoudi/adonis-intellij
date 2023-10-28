package io.stouder.adonis.edge.editor;

import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import io.stouder.adonis.edge.psi.EdgeTokenTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EdgeBraceMatcher implements PairedBraceMatcher {
    @Override
    public BracePair @NotNull [] getPairs() {
        return List.of(
                new BracePair(EdgeTokenTypes.MUSTACHE_OPEN, EdgeTokenTypes.MUSTACHE_CLOSE, false),
                new BracePair(EdgeTokenTypes.SAFE_MUSTACHE_OPEN, EdgeTokenTypes.SAFE_MUSTACHE_CLOSE, false),
                new BracePair(EdgeTokenTypes.ESCAPED_MUSTACHE_OPEN, EdgeTokenTypes.ESCAPED_MUSTACHE_CLOSE, false),
                new BracePair(EdgeTokenTypes.ESCAPED_SAFE_MUSTACHE_OPEN, EdgeTokenTypes.ESCAPED_SAFE_MUSTACHE_CLOSE, false),
                new BracePair(EdgeTokenTypes.MUSTACHE_COMMENT_OPEN, EdgeTokenTypes.MUSTACHE_COMMENT_CLOSE, false)
        ).toArray(new BracePair[0]);
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
