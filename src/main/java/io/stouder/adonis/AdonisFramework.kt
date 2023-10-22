package io.stouder.adonis

import com.intellij.javascript.web.WebFramework
import com.intellij.javascript.web.html.WebFrameworkHtmlFileType
import io.stouder.adonis.edge.EdgeFileType

class AdonisFramework : WebFramework() {
    override val displayName: String
        get() = "Edge"

    override val htmlFileType: WebFrameworkHtmlFileType?
        get() = EdgeFileType.INSTANCE
}
