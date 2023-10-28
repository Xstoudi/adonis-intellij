package io.stouder.adonis.edge.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.html.HtmlParsing;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.xml.XmlTokenType;
import io.stouder.adonis.edge.psi.EdgeElementType;
import io.stouder.adonis.edge.psi.EdgeMustacheElementType;
import io.stouder.adonis.edge.psi.EdgeTokenTypes;
import kotlin.Triple;

import java.util.List;
import java.util.Optional;

import static io.stouder.adonis.edge.psi.EdgeTokenTypes.MUSTACHES_TROUPLES;

class EdgeParsing extends HtmlParsing {

    private static final TokenSet CUSTOM_CONTENT = TokenSet.create(
            EdgeTokenTypes.MUSTACHE_OPEN,
            EdgeTokenTypes.ESCAPED_MUSTACHE_OPEN,
            EdgeTokenTypes.SAFE_MUSTACHE_OPEN,
            EdgeTokenTypes.ESCAPED_SAFE_MUSTACHE_OPEN,
            XmlTokenType.XML_DATA_CHARACTERS,
            XmlTokenType.XML_COMMA
    );

    private static final TokenSet DATA_TOKENS = TokenSet.create(
            XmlTokenType.XML_COMMA,
            XmlTokenType.XML_DATA_CHARACTERS
    );

    public EdgeParsing(PsiBuilder builder) {
        super(builder);
        builder.setDebugMode(true);
    }

    @Override
    protected boolean hasCustomTopLevelContent() {
        return CUSTOM_CONTENT.contains(this.token());
    }

    @Override
    protected boolean hasCustomTagContent() {
        return CUSTOM_CONTENT.contains(this.token());
    }

    @Override
    protected PsiBuilder.Marker parseCustomTagContent(PsiBuilder.Marker xmlText) {
        PsiBuilder.Marker result = xmlText;

        Optional<Triple<EdgeMustacheElementType, EdgeElementType, EdgeMustacheElementType>> maybeTriple = MUSTACHES_TROUPLES
                .stream()
                .filter(mustacheTriple -> mustacheTriple.getFirst().equals(this.token()))
                .findFirst();

        if(maybeTriple.isPresent()) {
            Triple<EdgeMustacheElementType, EdgeElementType, EdgeMustacheElementType> mustacheTriple = maybeTriple.get();
            EdgeElementType expressionElement = mustacheTriple.getSecond();
            EdgeMustacheElementType closingElement = mustacheTriple.getThird();

            result = startText(result);
            PsiBuilder.Marker mustache = this.mark();
            this.advance();
            if(!expressionElement.equals(this.token())) {
                mustache.error("Empty mustache");
            }
            this.advance();

            if(closingElement.equals(this.token())) {
                this.advance();
                mustache.drop();
            } else {
                mustache.error("Expected '" + closingElement.getMatchingChars() + "'");
            }
        } else if(XmlTokenType.XML_COMMA.equals(this.token())) {
            result = startText(result);
            this.getBuilder().remapCurrentToken(XmlTokenType.XML_DATA_CHARACTERS);
            this.advance();
        } else if(XmlTokenType.XML_DATA_CHARACTERS.equals(this.token())) {
            result = startText(result);
            PsiBuilder.Marker dataStart = this.mark();
            while(DATA_TOKENS.contains(this.token())) {
                this.advance();
            }
            dataStart.collapse(XmlTokenType.XML_DATA_CHARACTERS);
        }

        return result;
    }

    @Override
    protected PsiBuilder.Marker parseCustomTopLevelContent(PsiBuilder.Marker error) {
        PsiBuilder.Marker result = flushError(error);
        terminateText(parseCustomTagContent(null));
        return result;
    }
}