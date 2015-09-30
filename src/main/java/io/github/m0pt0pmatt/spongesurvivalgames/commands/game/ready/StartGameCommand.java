package io.github.m0pt0pmatt.spongesurvivalgames.commands.game.ready;

import com.google.common.base.Optional;
import io.github.m0pt0pmatt.spongesurvivalgames.SpongeSurvivalGamesPlugin;
import io.github.m0pt0pmatt.spongesurvivalgames.SurvivalGameState;
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.NoExitLocationException;
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.NoWorldException;
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.NoWorldNameException;
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.NotEnoughSpawnPointsException;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;

/**
 * Created by matthew on 9/27/15.
 */
public class StartGameCommand extends ReadyCommand {

    public StartGameCommand(SpongeSurvivalGamesPlugin plugin) {
        super(plugin);
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        Optional<String> id = args.getOne("id");
        if (!id.isPresent()) {
            plugin.getLogger().error("Survival Game ID is not present.");
            return CommandResult.empty();
        }

        if (!plugin.getSurvivalGameMap().containsKey(id.get())) {
            plugin.getLogger().error("No Survival Game has specified ID \"" + id.get() + "\".");
            return CommandResult.empty();
        }

        if (!plugin.getSurvivalGameMap().get(id.get()).getGameState().equals(SurvivalGameState.READY)) {
            plugin.getLogger().error("Survival Game \"" + id.get() + "\" must be READY before it can be set to RUNNING.");
            return CommandResult.empty();
        }

        try {
            plugin.getSurvivalGameMap().get(id.get()).setRunning();
        } catch (NotEnoughSpawnPointsException e) {
            plugin.getLogger().error("Survival Game \"" + id.get() + "\" does not have enough spawn points.");
            return CommandResult.empty();
        } catch (NoExitLocationException e) {
            plugin.getLogger().error("Survival Game \"" + id.get() + "\" does not have an exit location.");
            return CommandResult.empty();
        } catch (NoWorldException e) {
            plugin.getLogger().error("Survival Game \"" + id.get() + "\" does not have a world assigned to it.");
            return CommandResult.empty();
        } catch (NoWorldNameException e) {
            plugin.getLogger().error("Survival Game \"" + id.get() + "\" does not have a world assigned to it.");
            return CommandResult.empty();
        }

        plugin.getLogger().info("Survival Game \"" + id.get() + "\" is now RUNNING.");
        return CommandResult.success();
    }
}
