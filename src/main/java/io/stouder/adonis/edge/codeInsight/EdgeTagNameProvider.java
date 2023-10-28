package io.stouder.adonis.edge.codeInsight;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.psi.xml.XmlTag;
import com.intellij.xml.XmlTagNameProvider;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EdgeTagNameProvider implements XmlTagNameProvider {
    @Override
    public void addTagNameVariants(List<LookupElement> elements, @NotNull XmlTag tag, String prefix) {

    }
}
