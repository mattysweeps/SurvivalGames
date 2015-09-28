package io.github.m0pt0pmatt.SpongeSurvivalGames.commands.survivalgame.running;

import io.github.m0pt0pmatt.SpongeSurvivalGames.commands.survivalgame.SurvivalGameCommand;
import io.github.m0pt0pmatt.SpongeSurvivalGames.SpongeSurvivalGamesPlugin;
import io.github.m0pt0pmatt.SpongeSurvivalGames.SurvivalGameState;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;

/**
 * Created by matthew on 9/27/15.
 */
public class RunningCommand extends SurvivalGameCommand {

    public RunningCommand(SpongeSurvivalGamesPlugin plugin) {
        super(plugin);
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if (!super.execute(src, args).equals(CommandResult.success())){
            return CommandResult.empty();
        }

        if (!plugin.getSurvivalGameMap().get(id).getGameState().equals(SurvivalGameState.RUNNING)){
            plugin.getLogger().error("Survival Game \"" + id + "\" must be in a RUNNING state for this command.");
            return CommandResult.empty();
        }

        return CommandResult.success();
    }
}
