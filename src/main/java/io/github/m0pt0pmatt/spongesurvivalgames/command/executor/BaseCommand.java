/**
 * Created by m0pt0 on 9/4/2016.
 */
package io.github.m0pt0pmatt.spongesurvivalgames.command.executor;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.text.Text;

import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

abstract class BaseCommand implements SurvivalGamesCommand {

    private final List<String> aliases;
    private final String permission;
    private final Text usage;
    private final CommandElement arguments;

    BaseCommand(List<String> aliases, String permission, Text usage, CommandElement arguments) {
        this.aliases = ImmutableList.copyOf(checkNotNull(aliases, "aliases"));
        this.permission = checkNotNull(permission, "permission");
        this.usage = checkNotNull(usage, "usage");
        this.arguments = checkNotNull(arguments, "arguments");
    }

    @Override
    public List<String> getAliases() {
        return aliases;
    }

    @Override
    public boolean testPermission(CommandSource source) {
        return Strings.isNullOrEmpty(permission) || source.hasPermission(permission);
    }

    @Override
    public Optional<Text> getShortDescription(CommandSource source) {
        return Optional.empty();
    }

    @Override
    public Optional<Text> getHelp(CommandSource source) {
        return Optional.empty();
    }

    @Override
    public Text getUsage(CommandSource source) {
        return usage;
    }

    @Override
    public CommandElement getArguments() {
        return arguments;
    }
}
