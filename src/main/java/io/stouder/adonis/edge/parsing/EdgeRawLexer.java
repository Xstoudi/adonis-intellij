package io.stouder.adonis.edge.parsing;

import com.intellij.lexer.FlexAdapter;
public class EdgeRawLexer extends FlexAdapter {
    public EdgeRawLexer() {
        super(new _EdgeLexer(null));
    }
}
