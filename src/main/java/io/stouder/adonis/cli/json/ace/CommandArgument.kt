package io.stouder.adonis.cli.json.ace

data class CommandArgument(
    val name: String,
    val argumentName: String,
    val description: String?,
    val required: Boolean,
    val type: String
)