package io.stouder.adonis.edge.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.html.HTMLParser;
import com.intellij.lang.html.HtmlParsing;
import org.jetbrains.annotations.NotNull;

public class EdgeParser extends HTMLParser {
    @Override
    @NotNull
    protected HtmlParsing createHtmlParsing(@NotNull PsiBuilder builder) {
        return new EdgeParsing(builder);
    }
}
