package io.stouder.adonis

import com.intellij.DynamicBundle
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.PropertyKey

object AdonisBundle : DynamicBundle("messages.AdonisBundle") {
    @JvmStatic
    fun message(
        @PropertyKey(resourceBundle = "messages.AdonisBundle") key: @NotNull String, vararg params: @NotNull Any): String {
        return getMessage(key, *params)
    }
}