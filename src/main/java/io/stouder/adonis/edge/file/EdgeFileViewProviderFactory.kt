package io.stouder.adonis.edge.file

import com.intellij.lang.Language
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.FileViewProvider
import com.intellij.psi.FileViewProviderFactory
import com.intellij.psi.PsiManager
import org.jetbrains.annotations.NotNull

class EdgeFileViewProviderFactory : FileViewProviderFactory {
    override fun createFileViewProvider(
        @NotNull file: VirtualFile,
        language: Language,
        @NotNull manager: PsiManager,
        eventSystemEnabled: Boolean
    ): FileViewProvider {
        return EdgeFileViewProvider(manager, file, eventSystemEnabled, language)
    }
}
