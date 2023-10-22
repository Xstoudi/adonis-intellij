package io.stouder.adonis.edge.highlighter;

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;

public interface EdgeSyntaxHighlighterColors {
    TextAttributesKey EDGE_EXPRESSION = TextAttributesKey.createTextAttributesKey("EDGE_EXPRESSION", DefaultLanguageHighlighterColors.TEMPLATE_LANGUAGE_COLOR);
    TextAttributesKey EDGE_MUSTACHES = TextAttributesKey.createTextAttributesKey("EDGE_MUSTACHES", DefaultLanguageHighlighterColors.BRACKETS);
}
