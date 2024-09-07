@file:Suppress("SameReturnValue")

package io.stouder.adonis.edge

import com.intellij.ide.highlighter.HtmlFileType
import com.intellij.lang.Language
import com.intellij.openapi.fileTypes.LanguageFileType
import io.stouder.adonis.AdonisBundle
import io.stouder.adonis.edge.file.EdgeFileType

class EdgeLanguage : Language {
    constructor() : super("Edge")

    constructor(baseLanguage: Language?, id: String, vararg mimeTypes: String) : super(baseLanguage, id, *mimeTypes)

    companion object {
        @JvmField
        var INSTANCE: EdgeLanguage = EdgeLanguage()

        @JvmStatic
        fun getDefaultTemplateLang(): LanguageFileType {
            return HtmlFileType.INSTANCE
        }
    }

    override fun getDisplayName(): String {
        return AdonisBundle.message("adonis.edge.name")
    }

    override fun getAssociatedFileType(): LanguageFileType {
        return EdgeFileType.INSTANCE
    }
}