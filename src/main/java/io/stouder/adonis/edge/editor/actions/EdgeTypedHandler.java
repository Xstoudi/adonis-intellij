package io.stouder.adonis.edge.editor.actions;

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import io.stouder.adonis.edge.EdgeLanguage;
import io.stouder.adonis.edge.parsing.EdgeTokenTypes;
import org.jetbrains.annotations.NotNull;

public class EdgeTypedHandler extends TypedHandlerDelegate {
    @Override
    @NotNull
    public Result charTyped(char c, @NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
        Document document = editor.getDocument();
        CaretModel caretModel = editor.getCaretModel();
        FileViewProvider viewProvider = file.getViewProvider();
        int offset = caretModel.getOffset();

        if(!this.isEdgeContext(file)) {
            return Result.CONTINUE;
        }

        if(offset < 2 || offset > document.getTextLength()) {
            return Result.CONTINUE;
        }

        String chars = editor.getDocument().getText();
        // handle {{
        if(c == '{' && chars.charAt(offset - 2) == '{') {
            // handle {{{
            if(chars.charAt(offset - 3) == '{') {
                insertMatchingPart(editor, offset, chars, "}}}", 0);
                return Result.CONTINUE;
            }

            insertMatchingPart(editor, offset, chars, "}}", 0);
            return Result.CONTINUE;
        }

        // handle {{--
        if(
                c == '-'
                && chars.charAt(offset - 1) == '-'
                && offset >= 3
                && chars.charAt(offset - 2) == '{'
                && chars.charAt(offset - 3) == '{'
        ) {
            insertMatchingPart(editor, offset, chars, "--", 0);
            return Result.CONTINUE;
        }

        // handle ( in tag
        if(c == '(') {
            PsiElement element = viewProvider.findElementAt(offset - 2, EdgeLanguage.INSTANCE);
            if(element != null && element.getNode().getElementType() == EdgeTokenTypes.TAG_NAME) {
                insertMatchingPart(editor, offset, chars, ")", 0);
            }
            return Result.CONTINUE;
        }

        // handle ' and " in tag
        if(c == '\'' || c == '"') {
            PsiElement element = viewProvider.findElementAt(offset - 1, EdgeLanguage.INSTANCE);
            if(element != null && element.getNode().getElementType() == EdgeTokenTypes.TAG_CONTENT) {
                insertMatchingPart(editor, offset, chars, Character.toString(c), 0);
            }
            return Result.CONTINUE;
        }

        return Result.CONTINUE;
    }


    private static void insertMatchingPart(@NotNull Editor editor, int offset, String chars, String match, int shift) {
        String toInsert = getMatchingSubstring(match, chars, offset);
        if (!toInsert.isEmpty())
            EditorModificationUtil.insertStringAtCaret(editor, toInsert, true, shift);
    }

    private static String getMatchingSubstring(@NotNull String toMatch, String chars, int offset) {
        int matchingLength = Math.min(toMatch.length(), chars.length() - offset);
        while (matchingLength > 0 &&
                !toMatch.endsWith(chars.substring(offset, offset + matchingLength)))
            matchingLength--;
        return toMatch.substring(0, toMatch.length() - matchingLength);
    }

    private boolean isEdgeContext(PsiFile file ){
        return file.getViewProvider().getBaseLanguage().isKindOf(EdgeLanguage.INSTANCE);
    }
}
