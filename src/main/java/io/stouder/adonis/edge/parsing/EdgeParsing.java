package io.stouder.adonis.edge.parsing;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;

public class EdgeParsing {
    private final PsiBuilder builder;

    public EdgeParsing(PsiBuilder builder) {
        this.builder = builder;
    }

    public void parse() {
        while(!this.builder.eof()) {
            this.parseRoot();

            if(this.builder.eof()) {
                break;
            }

            int problemOffset = builder.getCurrentOffset();

            if (this.builder.getCurrentOffset() == problemOffset) {
                // none of our error checks advanced the lexer, do it manually before we
                // try and resume parsing to avoid an infinite loop
                PsiBuilder.Marker problemMark = this.builder.mark();
                this.builder.advanceLexer();
                problemMark.error("Invalid");
            }
        }
    }

    public void parseRoot() {
        this.parseProgram();
    }

    public void parseProgram() {
        this.parseStatements();
    }

    public void parseStatements() {
        PsiBuilder.Marker statementsMarker = this.builder.mark();

        while(true) {
            PsiBuilder.Marker optionalStatementMarker = builder.mark();
            if(this.parseStatment()) {
                optionalStatementMarker.drop();
            } else {
                optionalStatementMarker.rollbackTo();
                break;
            }
        }

        statementsMarker.done(EdgeTokenTypes.STATEMENTS);
    }

    public boolean parseStatment() {
        IElementType tokenType = this.builder.getTokenType();
        if (tokenType == EdgeTokenTypes.MUSTACHE_OPEN) {
            this.parseMustache(EdgeTokenTypes.MUSTACHE_OPEN, EdgeTokenTypes.MUSTACHE_CLOSE);
            return true;
        } else if (tokenType == EdgeTokenTypes.SAFE_MUSTACHE_OPEN) {
            this.parseMustache(EdgeTokenTypes.SAFE_MUSTACHE_OPEN, EdgeTokenTypes.SAFE_MUSTACHE_CLOSE);
            return true;
        } else if (tokenType == EdgeTokenTypes.ESCAPED_MUSTACHE_OPEN) {
            this.parseMustache(EdgeTokenTypes.ESCAPED_MUSTACHE_OPEN, EdgeTokenTypes.ESCAPED_MUSTACHE_CLOSE);
            return true;
        } else if (tokenType == EdgeTokenTypes.ESCAPED_SAFE_MUSTACHE_OPEN) {
            this.parseMustache(EdgeTokenTypes.ESCAPED_SAFE_MUSTACHE_OPEN, EdgeTokenTypes.ESCAPED_SAFE_MUSTACHE_CLOSE);
            return true;
        } else if (tokenType == EdgeTokenTypes.COMMENT_MUSTACHE_OPEN) {
            this.parseMustacheComment();
            return true;
        } else if (tokenType == EdgeTokenTypes.TAG_NAME) {
            this.parseTag();
            return true;
        } else if (tokenType == EdgeTokenTypes.CONTENT) {
            this.builder.advanceLexer();
            return true;
        }

        return false;
    }

    protected void parseMustache(IElementType openMustache, IElementType closeMustache) {
        PsiBuilder.Marker mustacheMarker = this.builder.mark();

        this.parseLeafToken(openMustache);
        this.parseLeafToken(EdgeTokenTypes.MUSTACHE_CONTENT);
        this.parseLeafTokenGreedy(closeMustache);

        mustacheMarker.done(EdgeTokenTypes.MUSTACHE);
    }

    protected void parseMustacheComment() {
        PsiBuilder.Marker mustacheCommentMarker = this.builder.mark();

        this.parseLeafToken(EdgeTokenTypes.COMMENT_MUSTACHE_OPEN);
        this.parseLeafToken(EdgeTokenTypes.COMMENT_MUSTACHE_CONTENT);
        this.parseLeafTokenGreedy(EdgeTokenTypes.COMMENT_MUSTACHE_CLOSE);

        mustacheCommentMarker.done(EdgeTokenTypes.COMMENT_MUSTACHE);
    }

    protected void parseTag() {
        PsiBuilder.Marker tagMarker = this.builder.mark();

        this.parseLeafToken(EdgeTokenTypes.TAG_NAME);
        IElementType nextToken = this.builder.getTokenType();
        if(nextToken == EdgeTokenTypes.TAG_CONTENT_OPEN) {
            this.parseLeafToken(EdgeTokenTypes.TAG_CONTENT_OPEN);
            this.parseLeafTokenGreedy(EdgeTokenTypes.TAG_CONTENT);
            this.parseLeafTokenGreedy(EdgeTokenTypes.TAG_CONTENT_CLOSE);
        } else if(nextToken == EdgeTokenTypes.NEWLINE) {
            this.parseLeafToken(EdgeTokenTypes.NEWLINE);
        } else {
            tagMarker.error("Unexpected token: " + nextToken);
            return;
        }

        tagMarker.done(EdgeTokenTypes.TAG);
    }

    protected boolean parseLeafToken(IElementType leafTokenType) {
        PsiBuilder.Marker leafTokenMark = this.builder.mark();
        if(this.builder.getTokenType() == leafTokenType) {
            builder.advanceLexer();
            leafTokenMark.done(leafTokenType);
            return true;
        }

        leafTokenMark.error("Expected token: " + leafTokenType.toString());
        return false;
    }

    protected void parseLeafTokenGreedy(IElementType elementType) {
        if(this.builder.getTokenType() != elementType) {
            PsiBuilder.Marker unexpectedTokensMarker = builder.mark();
            while(!this.builder.eof() && this.builder.getTokenType() != elementType) {
                this.builder.advanceLexer();
            }
            unexpectedTokensMarker.error("Expected token: " + elementType.toString());
        }

        if(!this.builder.eof() && builder.getTokenType() == elementType) {
            this.parseLeafToken(elementType);
        }
    }


}
