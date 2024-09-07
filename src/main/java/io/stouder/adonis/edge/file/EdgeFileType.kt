package io.stouder.adonis.edge.file

import com.intellij.ide.highlighter.XmlLikeFileType
import com.intellij.openapi.fileTypes.TemplateLanguageFileType
import io.stouder.adonis.AdonisBundle
import io.stouder.adonis.AdonisIcons
import io.stouder.adonis.edge.EdgeLanguage
import org.jetbrains.annotations.NonNls
import org.jetbrains.annotations.NotNull
import javax.swing.Icon

class EdgeFileType private constructor() : XmlLikeFileType(EdgeLanguage.INSTANCE), TemplateLanguageFileType {

    companion object {
        @JvmField
        val INSTANCE: EdgeFileType = EdgeFileType()
    }

    override fun getName(): @NonNls @NotNull String {
        return "Edge"
    }

    override fun getDescription(): @NotNull String {
        return AdonisBundle.message("adonis.edge.description")
    }

    override fun getDefaultExtension(): @NotNull String {
        return "edge"
    }

    override fun getIcon(): Icon {
        return AdonisIcons.EDGE
    }
}