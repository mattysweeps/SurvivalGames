package io.github.m0pt0pmatt.spongesurvivalgames.commands;

import io.github.m0pt0pmatt.spongesurvivalgames.SpongeSurvivalGamesPlugin;
import io.github.m0pt0pmatt.spongesurvivalgames.SurvivalGame;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

import java.util.Map;

/**
 * Created by matthew on 9/27/15.
 */
public class ListGamesCommand implements CommandExecutor {

    private final SpongeSurvivalGamesPlugin plugin;

    public ListGamesCommand(SpongeSurvivalGamesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        plugin.getLogger().info("There are " + plugin.getSurvivalGameMap().size() + " Survival Games:");
        for (Map.Entry<String, SurvivalGame> entry : plugin.getSurvivalGameMap().entrySet()) {
            plugin.getLogger().info("ID: \"" + entry.getKey() + "\", State: " + entry.getValue().getGameState());
        }

        return CommandResult.success();
    }
}
