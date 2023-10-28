package io.stouder.adonis.edge.psi;

import com.intellij.lang.*;
import com.intellij.lang.javascript.JSLanguageUtil;
import com.intellij.lang.javascript.parsing.JavaScriptParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.ILazyParseableElementType;
import io.stouder.adonis.edge.EdgeLanguage;
import io.stouder.adonis.edge.lexer.EdgeJSExpressionLexer;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public abstract class EdgeJSLazyElementType extends ILazyParseableElementType {
    public EdgeJSLazyElementType(@NotNull @NonNls String debugName) {
        super(debugName, EdgeLanguage.INSTANCE);
    }

    @Override
    public ASTNode createNode(CharSequence text) {
        if(text == null) {
            return null;
        }
        return new EdgeJSLazyPsiElement(this, text);
    }

    @Override
    protected ASTNode doParseContents(@NotNull ASTNode chameleon, @NotNull PsiElement psi) {
        Project project = psi.getProject();
        Lexer lexer = new EdgeJSExpressionLexer();
        Language language = this.getLanguage();
        PsiBuilder builder = PsiBuilderFactory.getInstance().createBuilder(project, chameleon, lexer, language, chameleon.getChars());
        JavaScriptParser<?, ?, ?, ?> parser = JSLanguageUtil.createJSParser(language, builder);

        PsiBuilder.Marker rootMarker = builder.mark();

        if(builder.eof()) {
            builder.error("Unexpected token");
        } else {
            this.parseTokens(builder, parser);
        }

        rootMarker.done(this);

        return builder.getTreeBuilt().getFirstChildNode();
    }

    protected abstract void parseTokens(PsiBuilder builder, JavaScriptParser<?, ?, ?, ?> parser);
}
