package io.github.m0pt0pmatt.spongesurvivalgames.commands;

import com.google.common.base.Optional;
import io.github.m0pt0pmatt.spongesurvivalgames.SpongeSurvivalGamesPlugin;
import io.github.m0pt0pmatt.spongesurvivalgames.SurvivalGame;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

/**
 * Created by matthew on 9/27/15.
 */
public class CreateSurvivalGameCommand implements CommandExecutor {

    private final SpongeSurvivalGamesPlugin plugin;

    public CreateSurvivalGameCommand(SpongeSurvivalGamesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        Optional<String> id = args.getOne("id");
        if (!id.isPresent()) {
            plugin.getLogger().error("Survival Game ID is not present.");
            return CommandResult.empty();
        }

        if (plugin.getSurvivalGameMap().containsKey(id.get())) {
            plugin.getLogger().error("Survival Game ID already exists.");
            return CommandResult.empty();
        }

        plugin.getSurvivalGameMap().put(id.get(), new SurvivalGame(plugin));
        plugin.getLogger().info("Survival Game \"" + id.get() + "\" created.");

        return CommandResult.success();
    }
}
