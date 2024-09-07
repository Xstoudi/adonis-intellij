package io.stouder.adonis.edge.editor.templates

import com.intellij.codeInsight.template.emmet.generators.XmlZenCodingGeneratorImpl
import com.intellij.lang.Language
import io.stouder.adonis.edge.EdgeLanguage
import com.intellij.openapi.options.Configurable
import org.jetbrains.annotations.Nullable

class EdgeEmmetGenerator : XmlZenCodingGeneratorImpl() {

    override fun isMyLanguage(language: Language): Boolean {
        return language.isKindOf(EdgeLanguage.INSTANCE)
    }
}
