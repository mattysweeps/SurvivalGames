package io.github.m0pt0pmatt.spongesurvivalgames.commands.survivalgame;

import com.google.common.base.Optional;
import io.github.m0pt0pmatt.spongesurvivalgames.SpongeSurvivalGamesPlugin;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

/**
 * Created by matthew on 9/27/15.
 */
public class PrintCenterLocationCommand extends SurvivalGameCommand {

    public PrintCenterLocationCommand(SpongeSurvivalGamesPlugin plugin) {
        super(plugin);
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if (!super.execute(src, args).equals(CommandResult.success())) {
            return CommandResult.empty();
        }

        Optional<Location<World>> centerLocation = plugin.getSurvivalGameMap().get(id).getCenterLocation();
        if (centerLocation.isPresent()) {
            plugin.getLogger().info("Game: \"" + id + "\", Center Location: \"" + centerLocation.get() + "\".");
        } else {
            plugin.getLogger().info("Game: \"" + id + "\", No Center Location.");
        }

        return CommandResult.success();
    }
}
