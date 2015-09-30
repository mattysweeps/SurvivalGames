package io.github.m0pt0pmatt.spongesurvivalgames.commands.game.stopped;

import com.google.common.base.Optional;
import io.github.m0pt0pmatt.spongesurvivalgames.SpongeSurvivalGamesPlugin;
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.NoWorldException;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;

/**
 * Created by matthew on 9/27/15.
 */
public class AddSpawnLocationCommand extends StoppedCommand {

    public AddSpawnLocationCommand(SpongeSurvivalGamesPlugin plugin) {
        super(plugin);
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if (!super.execute(src, args).equals(CommandResult.success())) {
            return CommandResult.empty();
        }

        Optional<Integer> x = args.getOne("x");
        Optional<Integer> y = args.getOne("y");
        Optional<Integer> z = args.getOne("z");
        if (!x.isPresent() || !y.isPresent() || !z.isPresent()) {
            plugin.getLogger().error("Missing one or more axis for coordinates.");
            return CommandResult.empty();
        }

        try {
            plugin.getSurvivalGameMap().get(id).addSpawnLocation(x.get(), y.get(), z.get());
        } catch (NoWorldException e) {
            plugin.getLogger().error("No world assigned. Assign the world first.");
            return CommandResult.empty();
        }
        plugin.getLogger().info("Spawn location added for game \"" + id + "\".");

        return CommandResult.success();
    }
}
