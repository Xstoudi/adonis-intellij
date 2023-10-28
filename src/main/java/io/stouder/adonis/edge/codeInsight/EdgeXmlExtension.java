package io.stouder.adonis.edge.codeInsight;

import com.intellij.ide.highlighter.HtmlFileType;
import com.intellij.psi.PsiFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.xml.HtmlXmlExtension;
import io.stouder.adonis.edge.EdgeFileType;
import org.jetbrains.annotations.NotNull;

public class EdgeXmlExtension extends HtmlXmlExtension {
    @Override
    public boolean isAvailable(PsiFile file) {
        return file.getFileType() == EdgeFileType.INSTANCE;
    }

    @Override
    public boolean isSelfClosingTagAllowed(@NotNull XmlTag tag) {
        XmlTag parent = tag;
        while (parent != null) {
            if ("svg".equals(parent.getName()) || "math".equals(parent.getName())) {
                return true;
            }
            parent = parent.getParentTag();
        }
        return super.isSelfClosingTagAllowed(tag);
    }
}
