package io.stouder.adonis.edge

import com.intellij.psi.impl.source.html.HtmlFileImpl
import com.intellij.psi.stubs.PsiFileStub
import com.intellij.psi.tree.IStubFileElementType

class EdgeFileElementType private constructor() : IStubFileElementType<PsiFileStub<HtmlFileImpl>>("edge", EdgeLanguage.INSTANCE) {
    companion object {
        val INSTANCE: IStubFileElementType<PsiFileStub<HtmlFileImpl>> = EdgeFileElementType()
    }
}