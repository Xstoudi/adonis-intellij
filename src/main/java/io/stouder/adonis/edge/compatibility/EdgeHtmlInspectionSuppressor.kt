package io.stouder.adonis.edge.compatibility

import com.intellij.codeInspection.DefaultXmlSuppressionProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import io.stouder.adonis.edge.file.EdgeFileType

class EdgeHtmlInspectionSuppressor : DefaultXmlSuppressionProvider() {

    override fun isProviderAvailable(file: PsiFile): Boolean {
        return file.fileType is EdgeFileType
    }

    override fun isSuppressedFor(element: PsiElement, inspectionId: String): Boolean {
        return super.isSuppressedFor(element, inspectionId)
    }
}
