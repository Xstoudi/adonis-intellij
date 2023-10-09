package io.stouder.adonis.cli.json.routes;

import com.google.gson.*;
import lombok.Getter;

import java.lang.reflect.Type;

@Getter
public class RouteHandler {
    private String type;

    public static class Deserializer implements JsonDeserializer<RouteHandler> {
        @Override
        public RouteHandler deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonElement jsonType = jsonObject.get("type");
            String typeString = jsonType.getAsString();
            Class<? extends RouteHandler> handlerClass = switch (typeString) {
                case "controller" -> ControllerRouteHandler.class;
                case "closure" -> ClosureRouteHandler.class;
                default -> throw new JsonParseException("Unknown type: " + typeString);
            };

            return jsonDeserializationContext.deserialize(jsonElement, handlerClass);
        }
    }
}
