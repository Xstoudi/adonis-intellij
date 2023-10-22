package io.stouder.adonis.edge;

import com.intellij.psi.impl.source.html.HtmlFileImpl;
import com.intellij.psi.stubs.PsiFileStub;
import com.intellij.psi.tree.IStubFileElementType;

public class EdgeFileElementType extends IStubFileElementType<PsiFileStub<HtmlFileImpl>> {
    public static final IStubFileElementType<PsiFileStub<HtmlFileImpl>> INSTANCE  = new EdgeFileElementType();

    private EdgeFileElementType() {
        super("edge", EdgeLanguage.INSTANCE);
    }
}
