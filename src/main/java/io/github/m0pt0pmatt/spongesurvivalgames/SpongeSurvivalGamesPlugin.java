package io.github.m0pt0pmatt.spongesurvivalgames;

import io.github.m0pt0pmatt.spongesurvivalgames.commands.*;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.survivalgame.ready.AddPlayerToSurvivalGame;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.survivalgame.ready.RemovePlayerFromSurvivalGameCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.survivalgame.ready.StartSurvivalGameCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.survivalgame.ready.StopSurvivalGameCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.survivalgame.stopped.DeleteSurvivalGameCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.survivalgame.stopped.SetReadySurvivalGameCommand;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.command.args.GenericArguments;
import org.spongepowered.api.util.command.spec.CommandSpec;

import java.util.HashMap;
import java.util.Map;

@Plugin(id = "spongesurvivalgames", name = "Sponge Survival Games", version = "1.0")
public class SpongeSurvivalGamesPlugin {

    private final Logger logger; // Plugin Logger
    private Game game;

    private final Map<String, SurvivalGame> survivalGameMap;

    @Inject
    public SpongeSurvivalGamesPlugin(Logger logger) {
        this.logger = logger;
        survivalGameMap = new HashMap<String, SurvivalGame>();
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        logger.info("Sponge Survival Games Plugin Enabled");
        this.game = event.getGame();
        registerCommands();
        survivalGameMap.clear();
    }

    private void registerCommands() {

        CommandSpec createSurvivalGameCommand = CommandSpec.builder()
                .description(Texts.of("<id>"))
                .permission("spongesurvivalgames.survivalgame.create")
                .arguments(GenericArguments.onlyOne(GenericArguments.string(Texts.of("id"))))
                .executor(new CreateSurvivalGameCommand(this))
                .build();
        game.getCommandDispatcher().register(this, createSurvivalGameCommand, "ssg-create");

        CommandSpec deleteSurvivalGameCommand = CommandSpec.builder()
                .description(Texts.of("<id>"))
                .permission("spongesurvivalgames.survivalgame.delete")
                .arguments(GenericArguments.onlyOne(GenericArguments.string(Texts.of("id"))))
                .executor(new DeleteSurvivalGameCommand(this))
                .build();
        game.getCommandDispatcher().register(this, deleteSurvivalGameCommand, "ssg-delete");

        CommandSpec listSurvivalGamesCommand = CommandSpec.builder()
                .description(Texts.of(""))
                .permission("spongesurvivalgames.survivalgame.list")
                .executor(new ListSurvivalGameCommand(this))
                .build();
        game.getCommandDispatcher().register(this, listSurvivalGamesCommand, "ssg-list");

        CommandSpec setReadySurvivalGamesCommand = CommandSpec.builder()
                .description(Texts.of("<id>"))
                .permission("spongesurvivalgames.survivalgame.setready")
                .arguments(GenericArguments.onlyOne(GenericArguments.string(Texts.of("id"))))
                .executor(new SetReadySurvivalGameCommand(this))
                .build();
        game.getCommandDispatcher().register(this, setReadySurvivalGamesCommand, "ssg-setready");

        CommandSpec startSurvivalGameCommand = CommandSpec.builder()
                .description(Texts.of("<id>"))
                .permission("spongesurvivalgames.survivalgame.start")
                .arguments(GenericArguments.onlyOne(GenericArguments.string(Texts.of("id"))))
                .executor(new StartSurvivalGameCommand(this))
                .build();
        game.getCommandDispatcher().register(this, startSurvivalGameCommand, "ssg-start");

        CommandSpec stopSurvivalGameCommand = CommandSpec.builder()
                .description(Texts.of("<id>"))
                .permission("spongesurvivalgames.survivalgame.stop")
                .arguments(GenericArguments.onlyOne(GenericArguments.string(Texts.of("id"))))
                .executor(new StopSurvivalGameCommand(this))
                .build();
        game.getCommandDispatcher().register(this, stopSurvivalGameCommand, "ssg-stop");

        CommandSpec addPlayerToSurvivalGameCommand = CommandSpec.builder()
                .description(Texts.of("<id> <playerName>"))
                .permission("spongesurvivalgames.survivalgame.addplayer")
                .arguments(GenericArguments.string(Texts.of("id")), GenericArguments.string(Texts.of("playerName")))
                .executor(new AddPlayerToSurvivalGame(this))
                .build();
        game.getCommandDispatcher().register(this, addPlayerToSurvivalGameCommand, "ssg-add-player");

        CommandSpec removePlayerFromSurvivalGameCommand = CommandSpec.builder()
                .description(Texts.of("<id> <playerName>"))
                .permission("spongesurvivalgames.survivalgame.removeplayer")
                .arguments(GenericArguments.string(Texts.of("id")), GenericArguments.string(Texts.of("playerName")))
                .executor(new RemovePlayerFromSurvivalGameCommand(this))
                .build();
        game.getCommandDispatcher().register(this, removePlayerFromSurvivalGameCommand, "ssg-remove-player");
    }

    public Logger getLogger() {
        return logger;
    }

    public Map<String, SurvivalGame> getSurvivalGameMap() {
        return survivalGameMap;
    }

    public Game getGame() {
        return game;
    }
}
