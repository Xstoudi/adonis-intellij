package io.stouder.adonis

import com.intellij.openapi.util.IconLoader
import javax.swing.Icon

object AdonisIcons {
    @JvmField val EDGE: Icon = IconLoader.getIcon("/icons/edge.svg", AdonisIcons::class.java)
    @JvmField val ADONIS: Icon = IconLoader.getIcon("/icons/adonis.svg", AdonisIcons::class.java)
    @JvmField val ADONIS_TOOL_WINDOW: Icon = IconLoader.getIcon("/icons/tool_window/adonisToolWindow.svg", AdonisIcons::class.java)
}