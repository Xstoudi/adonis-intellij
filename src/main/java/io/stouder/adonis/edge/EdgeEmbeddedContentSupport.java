package io.stouder.adonis.edge;

import com.intellij.html.embedding.HtmlEmbeddedContentProvider;
import com.intellij.html.embedding.HtmlEmbeddedContentSupport;
import com.intellij.html.embedding.HtmlTokenEmbeddedContentProvider;
import com.intellij.lang.javascript.JavaScriptHighlightingLexer;
import com.intellij.lang.javascript.dialects.JSLanguageLevel;
import com.intellij.lexer.BaseHtmlLexer;
import io.stouder.adonis.edge.lexer.EdgeLexer;
import io.stouder.adonis.edge.psi.EdgeTokenTypes;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EdgeEmbeddedContentSupport implements HtmlEmbeddedContentSupport {
    @Override
    public boolean isEnabled(@NotNull BaseHtmlLexer lexer) {
        return lexer instanceof EdgeLexer;
    }

    @Override
    public @NotNull List<HtmlEmbeddedContentProvider> createEmbeddedContentProviders(@NotNull BaseHtmlLexer lexer) {
        return List.of(
                new HtmlTokenEmbeddedContentProvider(
                    lexer,
                    EdgeTokenTypes.MUSTACHE,
                    () -> new JavaScriptHighlightingLexer(JSLanguageLevel.ES6.getDialect().getOptionHolder())
                )
        );
    }

}
