package io.stouder.adonis.edge.parsing;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

public final class EdgeTokenTypes {

    public static final IElementType STATEMENTS = new EdgeCompositeElementType("STATEMENTS");
    public static final IElementType MUSTACHE = new EdgeCompositeElementType("MUSTACHE");

    public static final IElementType MUSTACHE_CONTENT = new EdgeElementType("MUSTACHE_CONTENT");

    public static final IElementType MUSTACHE_OPEN = new EdgeElementType("MUSTACHE_OPEN");
    public static final IElementType MUSTACHE_CLOSE = new EdgeElementType("MUSTACHE_CLOSE");

    public static final IElementType SAFE_MUSTACHE_OPEN = new EdgeElementType("SAFE_MUSTACHE_OPEN");
    public static final IElementType SAFE_MUSTACHE_CLOSE = new EdgeElementType("SAFE_MUSTACHE_CLOSE");


    public static final IElementType WHITE_SPACE = new EdgeElementType("WHITE_SPACE");
    public static final IElementType CONTENT = new EdgeElementType("CONTENT");
    public static final IElementType OUTER_ELEMENT_TYPE = new EdgeElementType("OUTER_ELEMENT_TYPE");

    public static final TokenSet WHITESPACES = TokenSet.create(WHITE_SPACE);
    public static final TokenSet COMMENTS = TokenSet.create();
}
