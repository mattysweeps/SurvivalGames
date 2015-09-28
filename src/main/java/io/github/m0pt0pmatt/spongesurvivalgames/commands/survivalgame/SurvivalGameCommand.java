package io.github.m0pt0pmatt.spongesurvivalgames.commands.survivalgame;

import com.google.common.base.Optional;
import io.github.m0pt0pmatt.spongesurvivalgames.SpongeSurvivalGamesPlugin;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

/**
 * Created by matthew on 9/27/15.
 */
public class SurvivalGameCommand implements CommandExecutor {

    protected final SpongeSurvivalGamesPlugin plugin;
    protected String id;

    public SurvivalGameCommand(SpongeSurvivalGamesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        Optional<String> idOptional = args.getOne("id");
        if (!idOptional.isPresent()) {
            plugin.getLogger().error("Survival Game ID is not present.");
            return CommandResult.empty();
        }

        id = idOptional.get();

        if (!plugin.getSurvivalGameMap().containsKey(id)) {
            plugin.getLogger().error("No Survival Game has specified ID \"" + id + "\".");
            return CommandResult.empty();
        }

        return CommandResult.success();
    }
}
