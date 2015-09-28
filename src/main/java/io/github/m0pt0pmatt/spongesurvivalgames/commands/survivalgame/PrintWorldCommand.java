package io.github.m0pt0pmatt.spongesurvivalgames.commands.survivalgame;

import com.google.common.base.Optional;
import io.github.m0pt0pmatt.spongesurvivalgames.SpongeSurvivalGamesPlugin;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;

/**
 * Created by matthew on 9/27/15.
 */
public class PrintWorldCommand extends SurvivalGameCommand {

    public PrintWorldCommand(SpongeSurvivalGamesPlugin plugin) {
        super(plugin);
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if (!super.execute(src, args).equals(CommandResult.success())) {
            return CommandResult.empty();
        }

        Optional<String> worldName = plugin.getSurvivalGameMap().get(id).getWorldName();
        if (worldName.isPresent()){
            plugin.getLogger().info("Game: \"" + id + "\", World: \"" + worldName.get() + "\".");
        }
        else{
            plugin.getLogger().info("Game: \"" + id + "\", No World.");
        }

        return CommandResult.success();
    }
}
