package io.stouder.adonis.edge.codeInsight;

import com.intellij.psi.impl.source.xml.XmlElementDescriptorProvider;
import com.intellij.psi.xml.XmlTag;
import com.intellij.xml.XmlElementDescriptor;
import org.jetbrains.annotations.Nullable;

public class EdgeElementDescriptorProvider implements XmlElementDescriptorProvider {
    @Override
    public @Nullable XmlElementDescriptor getDescriptor(XmlTag tag) {
        return null;
    }
}
