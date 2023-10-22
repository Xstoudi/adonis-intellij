package io.stouder.adonis.edge.lexer;

import com.intellij.lexer.*;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.xml.XmlTokenType;
import io.stouder.adonis.edge.psi.EdgeTokenTypes;

public class EdgeLexer extends HtmlLexer {

    public EdgeLexer() {
        super(new MergingLexerAdapter(new FlexAdapter(new _EdgeLexer()), TOKENS_TO_MERGE), true);
    }

    private static final TokenSet TOKENS_TO_MERGE = TokenSet.create(
            XmlTokenType.XML_COMMENT_CHARACTERS,
            XmlTokenType.XML_WHITE_SPACE,
            XmlTokenType.XML_REAL_WHITE_SPACE,
            XmlTokenType.XML_ATTRIBUTE_VALUE_TOKEN,
            XmlTokenType.XML_DATA_CHARACTERS,
            XmlTokenType.XML_TAG_CHARACTERS,
            EdgeTokenTypes.MUSTACHE
    );
}
