package io.stouder.adonis.edge.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiParser;
import com.intellij.lang.html.HTMLParser;
import com.intellij.lang.html.HTMLParserDefinition;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import io.stouder.adonis.edge.EdgeFile;
import io.stouder.adonis.edge.EdgeFileElementType;
import io.stouder.adonis.edge.lexer.EdgeLexer;
import org.jetbrains.annotations.NotNull;

public class EdgeParserDefinition extends HTMLParserDefinition {
    @Override
    @NotNull
    public Lexer createLexer(Project project) {
        return new EdgeLexer();
    }

    @Override
    @NotNull
    public PsiParser createParser(final Project project) {
        return new EdgeParser();
    }

    @Override
    @NotNull
    public IFileElementType getFileNodeType() {
        return EdgeFileElementType.INSTANCE;
    }

    @Override
    public @NotNull PsiFile createFile(@NotNull FileViewProvider viewProvider) {
        return new EdgeFile(viewProvider);
    }

    @Override
    @NotNull
    public PsiElement createElement(ASTNode node) {
        return super.createElement(node);
    }

}
