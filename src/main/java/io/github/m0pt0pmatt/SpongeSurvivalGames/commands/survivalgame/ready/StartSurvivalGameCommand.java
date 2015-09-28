package io.github.m0pt0pmatt.SpongeSurvivalGames.commands.survivalgame.ready;

import com.google.common.base.Optional;
import io.github.m0pt0pmatt.SpongeSurvivalGames.SpongeSurvivalGamesPlugin;
import io.github.m0pt0pmatt.SpongeSurvivalGames.SurvivalGameState;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;

/**
 * Created by matthew on 9/27/15.
 */
public class StartSurvivalGameCommand extends ReadyCommand {

    public StartSurvivalGameCommand(SpongeSurvivalGamesPlugin plugin) {
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

        plugin.getSurvivalGameMap().get(id.get()).setRunning();
        plugin.getLogger().error("Survival Game \"" + id.get() + "\" is now RUNNING.");

        return CommandResult.success();
    }
}
