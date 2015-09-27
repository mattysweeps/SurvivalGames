package io.github.m0pt0pmatt.SpongeSurvivalGames.Commands;

import com.google.common.base.Optional;
import io.github.m0pt0pmatt.SpongeSurvivalGames.SpongeSurvivalGamesPlugin;
import io.github.m0pt0pmatt.SpongeSurvivalGames.SurvivalGame;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

import java.util.Map;

/**
 * Created by matthew on 9/27/15.
 */
public class ListSurvivalGameCommand implements CommandExecutor {

    private final SpongeSurvivalGamesPlugin plugin;

    public ListSurvivalGameCommand(SpongeSurvivalGamesPlugin plugin){
        this.plugin = plugin;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        plugin.getLogger().info("There are " + plugin.getSurvivalGameMap().size() + " Survival Games:");
        for (Map.Entry<String, SurvivalGame> entry: plugin.getSurvivalGameMap().entrySet()){
            plugin.getLogger().info("ID: \"" + entry.getKey() + "\", State: " + entry.getValue().getGameState());
        }

        return CommandResult.success();
    }
}
