package io.github.m0pt0pmatt.SpongeSurvivalGames.commands.survivalgame.stopped;

import io.github.m0pt0pmatt.SpongeSurvivalGames.SpongeSurvivalGamesPlugin;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;

/**
 * Created by matthew on 9/27/15.
 */
public class DeleteSurvivalGameCommand extends StoppedCommand {

    public DeleteSurvivalGameCommand(SpongeSurvivalGamesPlugin plugin) {
        super(plugin);
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if (!super.execute(src, args).equals(CommandResult.success())){
            return CommandResult.empty();
        }
        
        plugin.getSurvivalGameMap().remove(id);
        plugin.getLogger().info("Survival Game \"" + id + "\" deleted.");

        return CommandResult.success();
    }
}
