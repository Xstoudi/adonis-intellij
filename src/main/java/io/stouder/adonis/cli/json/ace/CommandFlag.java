package io.stouder.adonis.cli.json.ace;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class CommandFlag<T> {
    private String name;
    private String flagName;
    private boolean required;
    private String type;
    private String description;
    @SerializedName("default")
    private T defaultValue;
}
