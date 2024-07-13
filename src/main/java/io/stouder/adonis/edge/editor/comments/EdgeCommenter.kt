package io.stouder.adonis.edge.editor.comments

import com.intellij.lang.Commenter

class EdgeCommenter : Commenter {
    override fun getLineCommentPrefix(): String? {
        return null
    }

    override fun getBlockCommentPrefix(): String? {
        return "{{--"
    }

    override fun getBlockCommentSuffix(): String? {
        return "--}}"
    }

    override fun getCommentedBlockCommentPrefix(): String? {
        return null
    }

    override fun getCommentedBlockCommentSuffix(): String? {
        return null
    }
}
