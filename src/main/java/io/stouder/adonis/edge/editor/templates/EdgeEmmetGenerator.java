package io.stouder.adonis.edge.editor.templates;

import com.intellij.codeInsight.template.emmet.generators.XmlZenCodingGeneratorImpl;
import com.intellij.lang.Language;
import io.stouder.adonis.edge.EdgeLanguage;
import org.jetbrains.annotations.Nullable;
import com.intellij.openapi.options.Configurable;

public class EdgeEmmetGenerator extends XmlZenCodingGeneratorImpl {
    @Nullable
    @Override
    public Configurable createConfigurable() {
        return super.createConfigurable();
    }

    @Override
    protected boolean isMyLanguage(Language language) {
        return language.isKindOf(EdgeLanguage.INSTANCE);
    }
}
