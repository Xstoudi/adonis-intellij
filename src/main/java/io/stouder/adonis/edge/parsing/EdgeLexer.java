package io.stouder.adonis.edge.parsing;

import com.intellij.lexer.MergingLexerAdapter;
import com.intellij.psi.tree.TokenSet;

public class EdgeLexer extends MergingLexerAdapter {
    public EdgeLexer() {
        super(new EdgeRawLexer(), TOKENS_TO_MERGE);
    }

    private final static TokenSet TOKENS_TO_MERGE = TokenSet.create(
            EdgeTokenTypes.CONTENT,
            EdgeTokenTypes.MUSTACHE_CONTENT,
            EdgeTokenTypes.WHITE_SPACE
    );

}
