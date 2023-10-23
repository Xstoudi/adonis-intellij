package io.stouder.adonis.edge.psi;

import com.intellij.psi.tree.TokenSet;

public class EdgeTokenTypes {
    public static final EdgeMustacheElementType MUSTACHE_OPEN = new EdgeMustacheElementType("MUSTACHE_OPEN", "{{");
    public static final EdgeMustacheElementType MUSTACHE_CLOSE = new EdgeMustacheElementType("MUSTACHE_CLOSE", "}}");
    public static final EdgeElementType MUSTACHE = new EdgeElementType("MUSTACHE");

    public static final EdgeMustacheElementType SAFE_MUSTACHE_OPEN = new EdgeMustacheElementType("SAFE_MUSTACHE_OPEN", "{{{");
    public static final EdgeMustacheElementType SAFE_MUSTACHE_CLOSE = new EdgeMustacheElementType("SAFE_MUSTACHE_CLOSE", "}}}");
    public static final EdgeElementType SAFE_MUSTACHE = new EdgeElementType("SAFE_MUSTACHE");

    public static final EdgeMustacheElementType ESCAPED_MUSTACHE_OPEN = new EdgeMustacheElementType("ESCAPED_MUSTACHE_OPEN", "@{{");
    public static final EdgeMustacheElementType ESCAPED_MUSTACHE_CLOSE = new EdgeMustacheElementType("ESCAPED_MUSTACHE_CLOSE", "}}");
    public static final EdgeElementType ESCAPED_MUSTACHE = new EdgeElementType("ESCAPED_MUSTACHE");

    public static final EdgeMustacheElementType ESCAPED_SAFE_MUSTACHE_OPEN = new EdgeMustacheElementType("ESCAPED_SAFE_MUSTACHE_OPEN", "@{{{");
    public static final EdgeMustacheElementType ESCAPED_SAFE_MUSTACHE_CLOSE = new EdgeMustacheElementType("ESCAPED_SAFE_MUSTACHE_CLOSE", "}}}");
    public static final EdgeElementType ESCAPED_SAFE_MUSTACHE = new EdgeElementType("ESCAPED_SAFE_MUSTACHE");

    public static final EdgeMustacheElementType MUSTACHE_COMMENT_OPEN = new EdgeMustacheElementType("MUSTACHE_COMMENT_OPEN", "{{--");
    public static final EdgeMustacheElementType MUSTACHE_COMMENT_CLOSE = new EdgeMustacheElementType("MUSTACHE_COMMENT_CLOSE", "--}}");
    public static final EdgeElementType MUSTACHE_COMMENT = new EdgeElementType("MUSTACHE_COMMENT");

    public static final TokenSet MUSTACHE_TOKENS = TokenSet.create(
            MUSTACHE_OPEN,
            MUSTACHE_CLOSE,
            SAFE_MUSTACHE_OPEN,
            SAFE_MUSTACHE_CLOSE,
            ESCAPED_MUSTACHE_OPEN,
            ESCAPED_MUSTACHE_CLOSE,
            ESCAPED_SAFE_MUSTACHE_OPEN,
            ESCAPED_SAFE_MUSTACHE_CLOSE
    );
    public static final TokenSet COMMENT_TOKENS = TokenSet.create(
            MUSTACHE_COMMENT_OPEN,
            MUSTACHE_COMMENT_CLOSE,
            MUSTACHE_COMMENT
    );
}
