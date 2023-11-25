package io.stouder.adonis.edge.parsing;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.IStubFileElementType;
import com.intellij.psi.tree.TokenSet;
import io.stouder.adonis.edge.EdgeLanguage;
import io.stouder.adonis.edge.psi.EdgePsiFile;
import io.stouder.adonis.edge.psi.impl.EdgeMustacheImpl;
import io.stouder.adonis.edge.psi.impl.EdgePsiElementImpl;
import org.jetbrains.annotations.NotNull;

public class EdgeParserDefinition implements ParserDefinition {
    public static final IFileElementType FILE_ELEMENT_TYPE = new IStubFileElementType<>("edge", EdgeLanguage.INSTANCE);

    @Override
    public @NotNull Lexer createLexer(Project project) {
        return new EdgeLexer();
    }

    @Override
    public @NotNull PsiParser createParser(Project project) {
        return new EdgeParser();
    }

    @Override
    public @NotNull IFileElementType getFileNodeType() {
        return FILE_ELEMENT_TYPE;
    }

    @Override
    public @NotNull TokenSet getCommentTokens() {
        return EdgeTokenTypes.COMMENTS;
    }

    @Override
    public @NotNull TokenSet getWhitespaceTokens() {
        return EdgeTokenTypes.WHITESPACES;
    }

    @Override
    public @NotNull TokenSet getStringLiteralElements() {
        return TokenSet.EMPTY;
    }

    @Override
    public @NotNull PsiElement createElement(ASTNode node) {
        final IElementType elementType = node.getElementType();

        if(elementType == EdgeTokenTypes.MUSTACHE) {
            return new EdgeMustacheImpl(node);
        }

        return new EdgePsiElementImpl(node);
    }

    @Override
    public @NotNull PsiFile createFile(@NotNull FileViewProvider viewProvider) {
        return new EdgePsiFile(viewProvider);
    }

    @Override
    public @NotNull SpaceRequirements spaceExistenceTypeBetweenTokens(ASTNode left, ASTNode right) {
        return SpaceRequirements.MAY;
    }
}
