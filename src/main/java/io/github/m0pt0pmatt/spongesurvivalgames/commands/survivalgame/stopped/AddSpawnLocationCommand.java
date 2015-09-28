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
public class AddSpawnLocationCommand extends StoppedCommand {

    public AddSpawnLocationCommand(SpongeSurvivalGamesPlugin plugin) {
        super(plugin);
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if (!super.execute(src, args).equals(CommandResult.success())){
            return CommandResult.empty();
        }

        Optional<String> worldName = args.getOne("worldName");
        if (!worldName.isPresent()){
            plugin.getLogger().error("World name was not present.");
            return CommandResult.empty();
        }

        Optional<World> world = plugin.getGame().getServer().getWorld(worldName.get());
        if (!world.isPresent()){
            plugin.getLogger().error("World \"" + worldName.get() + "\" does not exist.");
            return CommandResult.empty();
        }

        Optional<Integer> x = args.getOne("x");
        Optional<Integer> y = args.getOne("x");
        Optional<Integer> z = args.getOne("x");
        if (!x.isPresent() || !y.isPresent() || !z.isPresent()){
            plugin.getLogger().error("Missing one or more axis for coordinates.");
            return CommandResult.empty();
        }

        Location<World> location = new Location<World>(world.get(), x.get(), y.get(), z.get());
        plugin.getSurvivalGameMap().get(id).addSpawnLocation(location);
        plugin.getLogger().info("Spawn location " + location.toString() + " added for game \"" + id + "\".");

        return CommandResult.success();
    }
}
