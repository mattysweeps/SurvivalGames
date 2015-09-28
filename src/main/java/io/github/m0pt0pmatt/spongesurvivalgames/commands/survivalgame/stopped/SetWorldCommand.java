package io.github.m0pt0pmatt.spongesurvivalgames.commands.survivalgame.stopped;

import com.google.common.base.Optional;
import io.github.m0pt0pmatt.spongesurvivalgames.SpongeSurvivalGamesPlugin;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.world.World;

/**
 * Created by matthew on 9/27/15.
 */
public class SetWorldCommand extends StoppedCommand {

    public SetWorldCommand(SpongeSurvivalGamesPlugin plugin) {
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

        Optional<World> world = plugin.getGame().getServer().getWorld(worldName.get());
        if (!world.isPresent()) {
            plugin.getLogger().error("World \"" + worldName.get() + "\" does not exist.");
            return CommandResult.empty();
        }

        plugin.getSurvivalGameMap().get(id).setWorld(worldName.get());
        plugin.getLogger().info("World for game \"" + id + "\" is set to \"" + worldName.get() + "\".");

        return CommandResult.success();
    }
}
