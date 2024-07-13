package io.stouder.adonis.edge.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.lang.Language
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import io.stouder.adonis.edge.file.EdgeFileType
import io.stouder.adonis.edge.EdgeLanguage

class EdgePsiFile : PsiFileBase {

    constructor(viewProvider: FileViewProvider) : this(viewProvider, EdgeLanguage.INSTANCE)

    constructor(viewProvider: FileViewProvider, language: Language) : super(viewProvider, language)

    override fun getFileType(): FileType {
        return EdgeFileType.INSTANCE
    }

    override fun toString(): String {
        return "EdgeFile:$name"
    }
}
