package io.github.m0pt0pmatt.spongesurvivalgames.commands.survivalgame;

import io.github.m0pt0pmatt.spongesurvivalgames.SpongeSurvivalGamesPlugin;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;

/**
 * Created by matthew on 9/28/15.
 */
public class PrintPlayerLimitCommand extends SurvivalGameCommand {

    public PrintPlayerLimitCommand(SpongeSurvivalGamesPlugin plugin) {
        super(plugin);
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if (!super.execute(src, args).equals(CommandResult.success())) {
            return CommandResult.empty();
        }

        plugin.getLogger().info("Game: \"" + id + "\", Player Limit: \"" + plugin.getSurvivalGameMap().get(id).getPlayerLimit() + "\".");
        return CommandResult.success();
    }
}
