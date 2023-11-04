package io.stouder.adonis.edge.compatibility;

import com.intellij.codeInspection.DefaultXmlSuppressionProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import io.stouder.adonis.edge.file.EdgeFileType;
import org.jetbrains.annotations.NotNull;

public class EdgeHtmlInspectionSuppressor extends DefaultXmlSuppressionProvider {
    @Override
    public boolean isProviderAvailable(@NotNull PsiFile file) {
        return file.getFileType() instanceof EdgeFileType;
    }

    @Override
    public boolean isSuppressedFor(@NotNull PsiElement element, @NotNull String inspectionId) {
        return super.isSuppressedFor(element, inspectionId);
    }
}
