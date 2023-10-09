package io.stouder.adonis.cli.json.ace;

import lombok.Getter;

import java.util.List;

@Getter
public class CommandComplexHelp extends Command {
    private List<String> help;
}
