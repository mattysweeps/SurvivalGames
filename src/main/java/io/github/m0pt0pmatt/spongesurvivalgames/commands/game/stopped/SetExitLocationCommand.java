package io.github.m0pt0pmatt.spongesurvivalgames.commands.game.stopped;

import com.google.common.base.Optional;
import io.github.m0pt0pmatt.spongesurvivalgames.SpongeSurvivalGamesPlugin;
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.NoWorldException;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

/**
 * Created by matthew on 9/29/15.
 */
public class SetExitLocationCommand extends StoppedCommand {

    public SetExitLocationCommand(SpongeSurvivalGamesPlugin plugin) {
        super(plugin);
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if (!super.execute(src, args).equals(CommandResult.success())) {
            return CommandResult.empty();
        }

        Optional<String> worldName = args.getOne("worldName");
        if (!worldName.isPresent()) {
            plugin.getLogger().error("World name was not present.");
            return CommandResult.empty();
        }

        Optional<Integer> x = args.getOne("x");
        Optional<Integer> y = args.getOne("y");
        Optional<Integer> z = args.getOne("z");
        if (!x.isPresent() || !y.isPresent() || !z.isPresent()) {
            plugin.getLogger().error("Missing one or more axis for coordinates.");
            return CommandResult.empty();
        }

        try{
            plugin.getSurvivalGameMap().get(id).setExitLocation(worldName.get(), x.get(), y.get(), z.get());
        } catch (NoWorldException e){
            plugin.getLogger().error("No such world \"" + worldName.get() + "\".");
            return CommandResult.empty();
        }

        plugin.getLogger().info("Exit location for game \"" + id + "\" set to world: " + worldName + " (" + x.get() + "," + y.get() + "," + z.get() + ").");
        return CommandResult.success();
    }
}
