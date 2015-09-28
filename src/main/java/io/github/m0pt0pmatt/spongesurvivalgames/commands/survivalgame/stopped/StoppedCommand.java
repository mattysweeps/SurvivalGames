package io.github.m0pt0pmatt.spongesurvivalgames.commands.survivalgame.stopped;

import io.github.m0pt0pmatt.spongesurvivalgames.commands.survivalgame.SurvivalGameCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.SpongeSurvivalGamesPlugin;
import io.github.m0pt0pmatt.spongesurvivalgames.SurvivalGameState;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;

/**
 * Created by matthew on 9/27/15.
 */
public class StoppedCommand extends SurvivalGameCommand {

    public StoppedCommand(SpongeSurvivalGamesPlugin plugin) {
        super(plugin);
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if (!super.execute(src, args).equals(CommandResult.success())){
            return CommandResult.empty();
        }

        if (!plugin.getSurvivalGameMap().get(id).getGameState().equals(SurvivalGameState.STOPPED)){
            plugin.getLogger().error("Survival Game \"" + id + "\" must be in a STOPPED state for this command.");
            return CommandResult.empty();
        }

        return CommandResult.success();
    }
}
