package io.github.m0pt0pmatt.SpongeSurvivalGames.Commands;

import com.google.common.base.Optional;
import io.github.m0pt0pmatt.SpongeSurvivalGames.SpongeSurvivalGamesPlugin;
import io.github.m0pt0pmatt.SpongeSurvivalGames.SurvivalGameState;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

/**
 * Created by matthew on 9/27/15.
 */
public class AddPlayerToSurvivalGame implements CommandExecutor {

    private final SpongeSurvivalGamesPlugin plugin;

    public AddPlayerToSurvivalGame(SpongeSurvivalGamesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        Optional<String> id = args.getOne("id");
        if (!id.isPresent()) {
            plugin.getLogger().error("Survival Game ID is not present.");
            return CommandResult.empty();
        }

        Optional<String> playerName = args.getOne("playerName");
        if (!id.isPresent()) {
            plugin.getLogger().error("Player name is not present.");
            return CommandResult.empty();
        }

        if (!plugin.getSurvivalGameMap().containsKey(id.get())) {
            plugin.getLogger().error("No Survival Game has specified ID \"" + id.get() + "\".");
            return CommandResult.empty();
        }

        if (!plugin.getSurvivalGameMap().get(id.get()).getGameState().equals(SurvivalGameState.READY)){
            plugin.getLogger().error("Survival Game \"" + id.get() + "\" must be READY to add a player.");
            return CommandResult.empty();
        }

        Optional<Player> player = plugin.getGame().getServer().getPlayer(playerName.get());
        if (!player.isPresent()){
            plugin.getLogger().error("No such player \"" + playerName.get() + "\".");
            return CommandResult.empty();
        }

        plugin.getSurvivalGameMap().get(id.get()).addPlayer(player.get().getUniqueId());
        plugin.getLogger().info("Player \"" + playerName.get() + "\" added to survival game \"" + id.get() + "\".");

        return CommandResult.success();
    }
}
