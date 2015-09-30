package io.github.m0pt0pmatt.spongesurvivalgames.commands.game.running;

import io.github.m0pt0pmatt.spongesurvivalgames.commands.game.GameCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.SpongeSurvivalGamesPlugin;
import io.github.m0pt0pmatt.spongesurvivalgames.SurvivalGameState;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;

/**
 * Created by matthew on 9/27/15.
 */
public abstract class RunningCommand extends GameCommand {

    public RunningCommand(SpongeSurvivalGamesPlugin plugin) {
        super(plugin);
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if (!super.execute(src, args).equals(CommandResult.success())) {
            return CommandResult.empty();
        }

        if (!plugin.getSurvivalGameMap().get(id).getGameState().equals(SurvivalGameState.RUNNING)) {
            plugin.getLogger().error("Survival Game \"" + id + "\" must be in a RUNNING state for this command.");
            return CommandResult.empty();
        }

        return CommandResult.success();
    }
}
