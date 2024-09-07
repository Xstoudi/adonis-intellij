package io.stouder.adonis.cli.json.ace

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.annotations.SerializedName
import java.lang.reflect.Type

open class Command(
    @SerializedName("commandName") open val commandName: String,
    @SerializedName("description") open val description: String,
    @SerializedName("namespace") open val namespace: String,
    @SerializedName("aliases") open val aliases: List<String>,
    @SerializedName("flags") open val flags: List<CommandFlag<*>>,
    @SerializedName("args") open val args: List<CommandArgument>,
    @SerializedName("options") open val options: CommandOptions,
    @SerializedName("filePath") open val filePath: String,
    @SerializedName("absolutePath") open val absolutePath: String
) {
    class Deserializer : JsonDeserializer<Command> {
        @Throws(JsonParseException::class)
        override fun deserialize(jsonElement: JsonElement, type: Type, jsonDeserializationContext: JsonDeserializationContext): Command {
            val jsonObject = jsonElement.asJsonObject
            val jsonType = jsonObject.get("help")
            return if (jsonType.isJsonArray) {
                jsonDeserializationContext.deserialize(jsonElement, CommandComplexHelp::class.java)
            } else {
                jsonDeserializationContext.deserialize(jsonElement, CommandSimpleHelp::class.java)
            }
        }
    }
}