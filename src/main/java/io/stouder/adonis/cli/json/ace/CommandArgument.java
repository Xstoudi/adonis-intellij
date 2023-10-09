package io.stouder.adonis.cli.json.ace;

import lombok.Getter;

@Getter
public class CommandArgument {
    private String name;
    private String argumentName;
    private boolean required;
    private String description;
    private String type;
}
