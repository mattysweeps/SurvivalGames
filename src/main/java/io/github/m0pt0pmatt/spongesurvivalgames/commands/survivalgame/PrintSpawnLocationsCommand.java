package io.github.m0pt0pmatt.spongesurvivalgames.commands.survivalgame;

import io.github.m0pt0pmatt.spongesurvivalgames.SpongeSurvivalGamesPlugin;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Set;

/**
 * Created by matthew on 9/27/15.
 */
public class PrintSpawnLocationsCommand extends SurvivalGameCommand{

    public PrintSpawnLocationsCommand(SpongeSurvivalGamesPlugin plugin) {
        super(plugin);
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if (!super.execute(src, args).equals(CommandResult.success())) {
            return CommandResult.empty();
        }

        Set<Location<World>> spawnLocations = plugin.getSurvivalGameMap().get(id).getSpawnLocations();
        plugin.getLogger().info("Game: \"" + id + "\", " + spawnLocations.size() + " Spawn Locations.");
        for (Location<World> spawnLocation: spawnLocations){
            plugin.getLogger().info(spawnLocation.toString());
        }

        return CommandResult.success();
    }
}
