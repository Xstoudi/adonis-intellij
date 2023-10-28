package io.stouder.adonis.edge.psi;

import com.intellij.psi.html.HtmlTag;
import com.intellij.psi.impl.source.xml.XmlTagImpl;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlTag;
import com.intellij.xml.util.XmlUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EdgeHtmlTag extends XmlTagImpl implements HtmlTag {
    @Override
    @Nullable
    public String getRealNs(@Nullable String value) {
        return XmlUtil.XHTML_URI.equals(value) ? XmlUtil.HTML_URI : value;
    }

    @Override
    public @NotNull String getNamespaceByPrefix(@NotNull String prefix) {
        String xmlNamespace = super.getNamespaceByPrefix(prefix);
        if(!prefix.isEmpty()) return xmlNamespace;
        return (xmlNamespace.isEmpty() || xmlNamespace.equals(XmlUtil.XHTML_URI)) ? XmlUtil.HTML_URI : xmlNamespace;
    }

    @Override
    public XmlTag getParentTag() {
        return PsiTreeUtil.getParentOfType(this, XmlTag.class);
    }

    @Override
    public String toString() {
        return "EdgeHtmlTag: " + this.getName();
    }
}
