package io.github.m0pt0pmatt.spongesurvivalgames.commands.survivalgame.stopped;

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
public class ClearSpawnLocationsCommand extends StoppedCommand {

    public ClearSpawnLocationsCommand(SpongeSurvivalGamesPlugin plugin) {
        super(plugin);
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if (!super.execute(src, args).equals(CommandResult.success())){
            return CommandResult.empty();
        }

        plugin.getSurvivalGameMap().get(id).clearSpawnLocations();
        plugin.getLogger().info("Spawn locations cleared for game \"" + id + "\".");

        return CommandResult.success();
    }
}
