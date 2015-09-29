package io.github.m0pt0pmatt.spongesurvivalgames.commands.survivalgame.ready;

import com.google.common.base.Optional;
import io.github.m0pt0pmatt.spongesurvivalgames.SpongeSurvivalGamesPlugin;
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.PlayerLimitReachedException;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;

/**
 * Created by matthew on 9/27/15.
 */
public class AddPlayerToSurvivalGame extends ReadyCommand {

    public AddPlayerToSurvivalGame(SpongeSurvivalGamesPlugin plugin) {
        super(plugin);
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if (!super.execute(src, args).equals(CommandResult.success())) {
            return CommandResult.empty();
        }

        Optional<String> playerName = args.getOne("playerName");
        if (!playerName.isPresent()) {
            plugin.getLogger().error("Player name is not present.");
            return CommandResult.empty();
        }

        Optional<Player> player = plugin.getGame().getServer().getPlayer(playerName.get());
        if (!player.isPresent()) {
            plugin.getLogger().error("No such player \"" + playerName.get() + "\".");
            return CommandResult.empty();
        }

        try {
            plugin.getSurvivalGameMap().get(id).addPlayer(player.get().getUniqueId());
        } catch (PlayerLimitReachedException e) {
            plugin.getLogger().error("Player limit reached for game \"" + id + "\".");
            return CommandResult.empty();
        }

        plugin.getLogger().info("Player \"" + playerName.get() + "\" added to survival game \"" + id + "\".");
        return CommandResult.success();
    }
}
