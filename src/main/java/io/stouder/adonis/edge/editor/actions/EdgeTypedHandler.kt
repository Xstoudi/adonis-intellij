package io.stouder.adonis.edge.editor.actions

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate
import com.intellij.openapi.editor.CaretModel
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorModificationUtil
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import io.stouder.adonis.edge.EdgeLanguage
import io.stouder.adonis.edge.parsing.EdgeTokenTypes

class EdgeTypedHandler : TypedHandlerDelegate() {
    override fun charTyped(c: Char, project: Project, editor: Editor, file: PsiFile): Result {
        val document: Document = editor.document
        val caretModel: CaretModel = editor.caretModel
        val viewProvider: FileViewProvider = file.viewProvider
        val offset: Int = caretModel.offset

        if (!isEdgeContext(file)) {
            return Result.CONTINUE
        }

        if (offset < 2 || offset > document.textLength) {
            return Result.CONTINUE
        }

        val chars: String = editor.document.text

        // handle {{
        if (c == '{' && chars[offset - 2] == '{') {
            // handle {{{
            if (chars[offset - 3] == '{') {
                insertMatchingPart(editor, offset, chars, "}}}", 0)
                return Result.CONTINUE
            }

            insertMatchingPart(editor, offset, chars, "}}", 0)
            return Result.CONTINUE
        }

        // handle {{--
        if (
            c == '-'
            && chars[offset - 1] == '-'
            && offset >= 3
            && chars[offset - 2] == '{'
            && chars[offset - 3] == '{'
        ) {
            insertMatchingPart(editor, offset, chars, "--", 0)
            return Result.CONTINUE
        }

        // handle ( in tag
        if (c == '(') {
            val element: PsiElement? = viewProvider.findElementAt(offset - 2, EdgeLanguage.INSTANCE)
            if (element != null && element.node.elementType == EdgeTokenTypes.TAG_NAME) {
                insertMatchingPart(editor, offset, chars, ")", 0)
            }
            return Result.CONTINUE
        }

        // handle ' and " in tag
        if (c == '\'' || c == '"') {
            val element: PsiElement? = viewProvider.findElementAt(offset - 1, EdgeLanguage.INSTANCE)
            if (element != null && element.node.elementType == EdgeTokenTypes.TAG_CONTENT) {
                insertMatchingPart(editor, offset, chars, c.toString(), 0)
            }
            return Result.CONTINUE
        }

        return Result.CONTINUE
    }

    private fun insertMatchingPart(editor: Editor, offset: Int, chars: String, match: String, shift: Int) {
        val toInsert = getMatchingSubstring(match, chars, offset)
        if (toInsert.isNotEmpty())
            EditorModificationUtil.insertStringAtCaret(editor, toInsert, true, shift)
    }

    private fun getMatchingSubstring(toMatch: String, chars: String, offset: Int): String {
        var matchingLength = toMatch.length.coerceAtMost(chars.length - offset)
        while (matchingLength > 0 && !toMatch.endsWith(chars.substring(offset, offset + matchingLength)))
            matchingLength--
        return toMatch.substring(0, toMatch.length - matchingLength)
    }

    private fun isEdgeContext(file: PsiFile): Boolean {
        return file.viewProvider.baseLanguage.isKindOf(EdgeLanguage.INSTANCE)
    }
}
