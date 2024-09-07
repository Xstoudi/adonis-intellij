package io.stouder.adonis.cli.json.routes

import com.google.gson.*
import java.lang.reflect.Type

open class RouteHandler(
    val type: String
) {
    class Deserializer : JsonDeserializer<RouteHandler> {
        override fun deserialize(jsonElement: JsonElement, type: Type, jsonDeserializationContext: JsonDeserializationContext): RouteHandler {
            val jsonObject = jsonElement.asJsonObject
            val jsonType = jsonObject.get("type")
            val handlerClass: Class<out RouteHandler> = when (val typeString = jsonType.asString) {
                "controller" -> ControllerRouteHandler::class.java
                "closure" -> ClosureRouteHandler::class.java
                else -> throw JsonParseException("Unknown type: $typeString")
            }

            return jsonDeserializationContext.deserialize(jsonElement, handlerClass)
        }
    }
}
