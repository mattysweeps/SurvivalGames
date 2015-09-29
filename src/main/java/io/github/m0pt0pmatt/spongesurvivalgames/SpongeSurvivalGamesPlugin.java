package io.github.m0pt0pmatt.spongesurvivalgames;

import io.github.m0pt0pmatt.spongesurvivalgames.commands.*;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.survivalgame.PrintCenterLocationCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.survivalgame.PrintPlayerLimitCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.survivalgame.PrintSpawnLocationsCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.survivalgame.PrintWorldCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.survivalgame.ready.AddPlayerToSurvivalGame;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.survivalgame.ready.RemovePlayerFromSurvivalGameCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.survivalgame.ready.StartSurvivalGameCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.survivalgame.ready.StopSurvivalGameCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.survivalgame.stopped.*;
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
                .permission("spongesurvivalgames.survivalgame.set.ready")
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
                .permission("spongesurvivalgames.survivalgame.add.player")
                .arguments(GenericArguments.string(Texts.of("id")), GenericArguments.string(Texts.of("playerName")))
                .executor(new AddPlayerToSurvivalGame(this))
                .build();
        game.getCommandDispatcher().register(this, addPlayerToSurvivalGameCommand, "ssg-add-player");

        CommandSpec removePlayerFromSurvivalGameCommand = CommandSpec.builder()
                .description(Texts.of("<id> <playerName>"))
                .permission("spongesurvivalgames.survivalgame.remove.player")
                .arguments(GenericArguments.string(Texts.of("id")), GenericArguments.string(Texts.of("playerName")))
                .executor(new RemovePlayerFromSurvivalGameCommand(this))
                .build();
        game.getCommandDispatcher().register(this, removePlayerFromSurvivalGameCommand, "ssg-remove-player");

        CommandSpec setWorldCommand = CommandSpec.builder()
                .description(Texts.of("<id> <worldName>"))
                .permission("spongesurvivalgames.survivalgame.set.world")
                .arguments(GenericArguments.string(Texts.of("id")), GenericArguments.string(Texts.of("worldName")))
                .executor(new SetWorldCommand(this))
                .build();
        game.getCommandDispatcher().register(this, setWorldCommand, "ssg-set-world");

        CommandSpec setCenterLocationCommand = CommandSpec.builder()
                .description(Texts.of("<id> <worldName>"))
                .permission("spongesurvivalgames.survivalgame.set.center")
                .arguments(GenericArguments.string(Texts.of("id")), GenericArguments.string(Texts.of("worldName")), GenericArguments.integer(Texts.of("x")), GenericArguments.integer(Texts.of("y")), GenericArguments.integer(Texts.of("z")))
                .executor(new SetCenterLocationCommand(this))
                .build();
        game.getCommandDispatcher().register(this, setCenterLocationCommand, "ssg-set-center");

        CommandSpec addSpawnLocationCommand = CommandSpec.builder()
                .description(Texts.of("<id> <worldName>"))
                .permission("spongesurvivalgames.survivalgame.add.spawnpoint")
                .arguments(GenericArguments.string(Texts.of("id")), GenericArguments.string(Texts.of("worldName")), GenericArguments.integer(Texts.of("x")), GenericArguments.integer(Texts.of("y")), GenericArguments.integer(Texts.of("z")))
                .executor(new AddSpawnLocationCommand(this))
                .build();
        game.getCommandDispatcher().register(this, addSpawnLocationCommand, "ssg-add-spawnpoint");

        CommandSpec clearSpawnLocationsCommand = CommandSpec.builder()
                .description(Texts.of("<id>"))
                .permission("spongesurvivalgames.survivalgame.clear.spawnpoints")
                .arguments(GenericArguments.string(Texts.of("id")))
                .executor(new ClearSpawnLocationsCommand(this))
                .build();
        game.getCommandDispatcher().register(this, clearSpawnLocationsCommand, "ssg-clear-spawnpoints");

        CommandSpec printWorldCommand = CommandSpec.builder()
                .description(Texts.of("<id>"))
                .permission("spongesurvivalgames.survivalgame.print.world")
                .arguments(GenericArguments.string(Texts.of("id")))
                .executor(new PrintWorldCommand(this))
                .build();
        game.getCommandDispatcher().register(this, printWorldCommand, "ssg-print-world");

        CommandSpec printCenterLocationCommand = CommandSpec.builder()
                .description(Texts.of("<id>"))
                .permission("spongesurvivalgames.survivalgame.print.center")
                .arguments(GenericArguments.string(Texts.of("id")))
                .executor(new PrintCenterLocationCommand(this))
                .build();
        game.getCommandDispatcher().register(this, printCenterLocationCommand, "ssg-print-center");

        CommandSpec printSpawnLocationsCommand = CommandSpec.builder()
                .description(Texts.of("<id>"))
                .permission("spongesurvivalgames.survivalgame.print.spawnpoints")
                .arguments(GenericArguments.string(Texts.of("id")))
                .executor(new PrintSpawnLocationsCommand(this))
                .build();
        game.getCommandDispatcher().register(this, printSpawnLocationsCommand, "ssg-print-spawnpoints");

        CommandSpec setPlayerLimitCommand = CommandSpec.builder()
                .description(Texts.of("<id> <playerLimit>"))
                .permission("spongesurvivalgames.survivalgame.set.playerlimit")
                .arguments(GenericArguments.string(Texts.of("id")), GenericArguments.integer(Texts.of("playerLimit")))
                .executor(new SetPlayerLimitCommand(this))
                .build();
        game.getCommandDispatcher().register(this, setPlayerLimitCommand, "ssg-set-playerlimit");

        CommandSpec printPlayerLimitCommand = CommandSpec.builder()
                .description(Texts.of("<id>"))
                .permission("spongesurvivalgames.survivalgame.print.playerLimit")
                .arguments(GenericArguments.string(Texts.of("id")))
                .executor(new PrintPlayerLimitCommand(this))
                .build();
        game.getCommandDispatcher().register(this, printPlayerLimitCommand, "ssg-print-playerlimit");
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
