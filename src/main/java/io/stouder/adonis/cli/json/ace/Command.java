package io.stouder.adonis.cli.json.ace;

import com.google.gson.*;
import io.stouder.adonis.cli.json.routes.ClosureRouteHandler;
import io.stouder.adonis.cli.json.routes.ControllerRouteHandler;
import io.stouder.adonis.cli.json.routes.RouteHandler;
import lombok.Getter;

import java.lang.reflect.Type;
import java.util.List;

@Getter
public class Command {
    private String commandName;
    private String description;
    private String namespace;
    private List<String> aliases;
    private List<CommandFlag> flags;
    private List<CommandArgument> args;
    private CommandOptions options;
    private String filePath;
    private String absolutePath;

    public static class Deserializer implements JsonDeserializer<Command> {
        @Override
        public Command deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonElement jsonType = jsonObject.get("help");
            if(jsonType.isJsonArray()) {
                return jsonDeserializationContext.deserialize(jsonElement, CommandComplexHelp.class);
            }
            return jsonDeserializationContext.deserialize(jsonElement, CommandSimpleHelp.class);
        }
    }
}
