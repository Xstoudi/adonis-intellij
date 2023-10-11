package io.stouder.adonis.execution;

import com.intellij.ide.actions.runAnything.RunAnythingUtil;
import com.intellij.ide.actions.runAnything.activity.RunAnythingCommandLineProvider;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.project.Project;
import io.stouder.adonis.cli.json.ace.Command;
import io.stouder.adonis.cli.json.ace.CommandFlag;
import io.stouder.adonis.notifier.AdonisRcUpdateNotifier;
import io.stouder.adonis.service.AdonisAceService;
import io.stouder.adonis.AdonisIcons;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AdonisRunAnythingProvider extends RunAnythingCommandLineProvider implements AdonisRcUpdateNotifier {

    private List<Command> commands = List.of();

    public AdonisRunAnythingProvider() {
        super();
    }

    @Override
    @Nullable
    public Icon getIcon(@NotNull String value) {
        return AdonisIcons.ADONIS;
    }

    @Nullable
    @Nls(capitalization = Nls.Capitalization.Title)
    public String getHelpGroupTitle() {
        return "Adonis";
    }

    @Override
    @Nullable
    public String getCompletionGroupTitle() {
        return "Adonis";
    }

    @Nullable
    public String getHelpCommandPlaceholder() {
        return "ace <command>";
    }

    @NotNull
    @Override
    public String getHelpCommand() {
        return "ace";
    }

    @Override
    protected boolean run(@NotNull DataContext dataContext, @NotNull RunAnythingCommandLineProvider.CommandLine commandLine) {
         return false;
    }

    @NotNull
    @Override
    protected Sequence<String> suggestCompletionVariants(@NotNull DataContext dataContext, @NotNull RunAnythingCommandLineProvider.CommandLine commandLine) {
        Project project = RunAnythingUtil.fetchProject(dataContext);

        System.out.printf("getCommand: %s%n", commandLine.getCommand());
        System.out.printf("getHelpCommand: %s%n", commandLine.getHelpCommand());
        System.out.printf("getToComplete: %s%n", commandLine.getToComplete());
        System.out.printf("getCompletedParameters: %s%n", commandLine.getCompletedParameters());
        System.out.printf("getParameters: %s%n", commandLine.getParameters());
        System.out.printf("getPrefix: %s%n", commandLine.getPrefix());

        AdonisAceService adonisAceService = AdonisAceService.getInstance(project);
        switch(commandLine.getParameters().size()) {
            case 0:
                return SequencesKt.asSequence(this.commands.stream().map(Command::getCommandName).iterator());
            case 1:
                System.out.println(autocompleteCommand(commandLine, this.commands));
                return SequencesKt.asSequence(autocompleteCommand(commandLine, this.commands).iterator());
            case 2:
                Command command = this.commands.stream().filter(c -> c.getCommandName().equals(commandLine.getParameters().get(0))).findFirst().orElse(null);
                if(command == null) return SequencesKt.emptySequence();
                return SequencesKt.asSequence(autocompleteArgsAndFlags(commandLine, command).iterator());
        }


        return SequencesKt.asSequence(List.of("ace", "xxx").iterator());
    }

    private List<String> autocompleteCommand(CommandLine commandLine, List<Command> commands) {
        String firstParameter = commandLine.getParameters().get(0);
        return commands
                .stream()
                .map(Command::getCommandName)
                .filter(commandName -> commandName.startsWith(firstParameter))
                .collect(Collectors.toList());
    }

    private List<String> autocompleteArgsAndFlags(CommandLine commandLine, Command command) {
        List<String> flags = command.getFlags().stream().map(CommandFlag::getFlagName).toList();
        return List.of("xx");
    }

    @Override
    public void commands(Command[] commands) {
        this.commands = List.of(commands);
        System.out.println("commands: " + Arrays.toString(commands));
    }
}
