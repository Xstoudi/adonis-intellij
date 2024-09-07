package io.stouder.adonis.edge.parsing

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.stubs.PsiFileStub
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.IStubFileElementType
import com.intellij.psi.tree.TokenSet
import io.stouder.adonis.edge.EdgeLanguage
import io.stouder.adonis.edge.psi.EdgePsiFile
import io.stouder.adonis.edge.psi.impl.EdgeMustacheImpl
import io.stouder.adonis.edge.psi.impl.EdgePsiElementImpl

class EdgeParserDefinition : ParserDefinition {

    companion object {
        val FILE_ELEMENT_TYPE = IStubFileElementType<PsiFileStub<PsiFile>>("edge", EdgeLanguage.INSTANCE)
    }

    override fun createLexer(project: Project): Lexer {
        return EdgeLexer()
    }

    override fun createParser(project: Project): PsiParser {
        return EdgeParser()
    }

    override fun getFileNodeType(): IFileElementType {
        return FILE_ELEMENT_TYPE
    }

    override fun getCommentTokens(): TokenSet {
        return EdgeTokenTypes.COMMENTS
    }

    override fun getWhitespaceTokens(): TokenSet {
        return EdgeTokenTypes.WHITESPACES
    }

    override fun getStringLiteralElements(): TokenSet {
        return TokenSet.EMPTY
    }

    override fun createElement(node: ASTNode): PsiElement {
        val elementType = node.elementType

        return when (elementType) {
            EdgeTokenTypes.MUSTACHE -> EdgeMustacheImpl(node)
            else -> EdgePsiElementImpl(node)
        }
    }

    override fun createFile(viewProvider: FileViewProvider): PsiFile {
        return EdgePsiFile(viewProvider)
    }

    override fun spaceExistenceTypeBetweenTokens(left: ASTNode, right: ASTNode): ParserDefinition.SpaceRequirements {
        return ParserDefinition.SpaceRequirements.MAY
    }
}
