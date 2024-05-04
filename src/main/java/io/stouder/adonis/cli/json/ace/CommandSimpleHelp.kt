package io.stouder.adonis.cli.json.ace

class CommandSimpleHelp(
    commandName: String,
    description: String,
    namespace: String,
    aliases: List<String>,
    flags: List<CommandFlag<*>>,
    args: List<CommandArgument>,
    options: CommandOptions,
    filePath: String,
    absolutePath: String,
    val help: String
) : Command(
    commandName,
    description,
    namespace,
    aliases,
    flags,
    args,
    options,
    filePath,
    absolutePath
)