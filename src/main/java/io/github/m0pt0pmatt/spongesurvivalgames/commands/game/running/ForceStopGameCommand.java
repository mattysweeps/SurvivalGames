package io.github.m0pt0pmatt.spongesurvivalgames.commands.game.running;

import io.github.m0pt0pmatt.spongesurvivalgames.SpongeSurvivalGamesPlugin;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;

/**
 * Created by matthew on 9/27/15.
 */
public class ForceStopGameCommand extends RunningCommand {

    public ForceStopGameCommand(SpongeSurvivalGamesPlugin plugin) {
        super(plugin);
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if (!super.execute(src, args).equals(CommandResult.success())) {
            return CommandResult.empty();
        }

        plugin.getSurvivalGameMap().get(id).setStopped();
        plugin.getLogger().error("Survival Game \"" + id + "\" is now STOPPED.");

        return CommandResult.success();
    }
}
