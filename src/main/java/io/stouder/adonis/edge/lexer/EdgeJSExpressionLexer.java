package io.stouder.adonis.edge.lexer;

import com.intellij.lang.javascript.DialectOptionHolder;
import com.intellij.lang.javascript.JSFlexAdapter;
import com.intellij.lexer.LayeredLexer;
import io.stouder.adonis.edge.psi.EdgeTokenTypes;

public class EdgeJSExpressionLexer extends LayeredLexer {
    public EdgeJSExpressionLexer() {
        super(new EdgeLexer());
        this.registerLayer(new JSFlexAdapter(DialectOptionHolder.TS), EdgeTokenTypes.MUSTACHE);
    }
}
