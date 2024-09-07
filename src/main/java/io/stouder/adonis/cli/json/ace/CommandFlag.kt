package io.stouder.adonis.cli.json.ace

import com.google.gson.annotations.SerializedName

data class CommandFlag<T>(
    val name: String,
    val flagName: String,
    val required: Boolean,
    val type: String,
    val description: String,
    @SerializedName("default") val defaultValue: T
)