package io.stouder.adonis.edge.editor.actions;

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import io.stouder.adonis.edge.EdgeLanguage;
import io.stouder.adonis.edge.parsing.EdgeTokenTypes;
import org.jetbrains.annotations.NotNull;

public class EdgeTypedHandler extends TypedHandlerDelegate {
    @Override
    @NotNull
    public Result beforeCharTyped(char c, @NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file, @NotNull FileType fileType) {
        if(!isEdgeContext(file)) {
            return Result.CONTINUE;
        }

        Document document = editor.getDocument();
        CaretModel caretModel = editor.getCaretModel();
        int offset = caretModel.getOffset();
        if(offset == 0 || offset > document.getTextLength()) {
            return Result.CONTINUE;
        }

        char previousChar = document.getText(new TextRange(offset - 1, offset)).charAt(0);
        PsiDocumentManager.getInstance(project).commitAllDocuments();
        if(c == '{' && previousChar == '{') {
            document.deleteString(offset, offset + 1);
        }

        return Result.CONTINUE;
    }

    @Override
    @NotNull
    public Result charTyped(char c, @NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
        Document document = editor.getDocument();
        CaretModel caretModel = editor.getCaretModel();
        int offset = caretModel.getOffset();

        if(!isEdgeContext(file)) {
            return Result.CONTINUE;
        }

        if(offset < 1 || offset > document.getTextLength()) {
            return Result.CONTINUE;
        }

        if(c == '{') {
            document.insertString(offset, "}");
            return Result.STOP;
        }

        PsiElement element = file.findElementAt(offset - 2);
        if(
                c == '(' &&
                element instanceof LeafPsiElement leafElement &&
                leafElement.getElementType() == EdgeTokenTypes.TAG_NAME
        ) {
                document.insertString(offset, ")");
                return Result.STOP;
        }


        return Result.CONTINUE;
    }

    private boolean isEdgeContext(PsiFile file ){
        return file.getViewProvider().getBaseLanguage().isKindOf(EdgeLanguage.INSTANCE);
    }
}
