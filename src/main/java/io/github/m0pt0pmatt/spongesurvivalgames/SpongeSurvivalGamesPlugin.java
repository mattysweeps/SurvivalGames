/*
 * This file is part of SpongeSurvivalGamesPlugin, licensed under the MIT License (MIT).
 *
 * Copyright (c) Matthew Broomfield <m0pt0pmatt17@gmail.com>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package io.github.m0pt0pmatt.spongesurvivalgames;

import io.github.m0pt0pmatt.spongesurvivalgames.commands.*;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.game.print.*;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.game.ready.AddPlayerToGame;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.game.ready.RemovePlayerFromGameCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.game.ready.StartGameCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.game.ready.StopGameCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.game.running.ForceStopGameCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.game.stopped.*;
import org.spongepowered.api.Game;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.command.args.GenericArguments;
import org.spongepowered.api.util.command.spec.CommandSpec;

import java.util.*;
import java.util.stream.Collectors;

/**
 * SpongeSurvivalGames Sponge Plugin
 */
@Plugin(id = "spongesurvivalgames", name = "Sponge Survival Games", version = "1.0")
public class SpongeSurvivalGamesPlugin {

    private static Game game;
    private final Logger logger; // Plugin Logger
    private final Map<String, SurvivalGame> survivalGameMap;

    @Inject
    public SpongeSurvivalGamesPlugin(Logger logger) {
        this.logger = logger;
        survivalGameMap = new HashMap<>();
    }

    public static Set<Player> getPlayers(Set<UUID> playerUUIDs) {
        return playerUUIDs.stream()
                .map(uuid -> game.getServer().getPlayer(uuid))
                .filter(player -> player.isPresent())
                .map(player -> player.get())
                .collect(Collectors.toSet());
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        logger.info("Sponge Survival Games Plugin Enabled");
        game = event.getGame();
        registerCommands();
        survivalGameMap.clear();
    }

    private void registerCommands() {

        CommandSpec createSurvivalGameCommand = CommandSpec.builder()
                .description(Texts.of("<id>"))
                .permission("spongesurvivalgames.game.create")
                .arguments(GenericArguments.onlyOne(GenericArguments.string(Texts.of("id"))))
                .executor(new CreateGameCommand(this))
                .build();
        game.getCommandDispatcher().register(this, createSurvivalGameCommand, "ssg-create");

        CommandSpec listSurvivalGamesCommand = CommandSpec.builder()
                .description(Texts.of(""))
                .permission("spongesurvivalgames.game.list")
                .executor(new ListGamesCommand(this))
                .build();
        game.getCommandDispatcher().register(this, listSurvivalGamesCommand, "ssg-list");

        registerGameCommands();
    }

    private void registerGameCommands() {

        registerPrintCommands();
        registerStoppedCommands();
        registerReadyCommands();
        registerRunningCommands();
    }

    private void registerPrintCommands() {

        CommandSpec printCenterCommand = CommandSpec.builder()
                .description(Texts.of("<id>"))
                .permission("spongesurvivalgames.game.print.center")
                .arguments(GenericArguments.string(Texts.of("id")))
                .executor(new PrintCenterCommand(this))
                .build();
        game.getCommandDispatcher().register(this, printCenterCommand, "ssg-print-center");

        CommandSpec printCountdownCommand = CommandSpec.builder()
                .description(Texts.of("<id>"))
                .permission("spongesurvivalgames.game.print.countdown")
                .arguments(GenericArguments.string(Texts.of("id")))
                .executor(new PrintCountdownCommand(this))
                .build();
        game.getCommandDispatcher().register(this, printCountdownCommand, "ssg-print-countdown");

        CommandSpec printExitCommand = CommandSpec.builder()
                .description(Texts.of("<id>"))
                .permission("spongesurvivalgames.game.print.exit")
                .arguments(GenericArguments.string(Texts.of("id")))
                .executor(new PrintExitCommand(this))
                .build();
        game.getCommandDispatcher().register(this, printExitCommand, "ssg-print-exit");

        CommandSpec printPlayerLimitCommand = CommandSpec.builder()
                .description(Texts.of("<id>"))
                .permission("spongesurvivalgames.game.print.playerLimit")
                .arguments(GenericArguments.string(Texts.of("id")))
                .executor(new PrintPlayerLimitCommand(this))
                .build();
        game.getCommandDispatcher().register(this, printPlayerLimitCommand, "ssg-print-playerlimit");

        CommandSpec printSpawnsCommand = CommandSpec.builder()
                .description(Texts.of("<id>"))
                .permission("spongesurvivalgames.game.print.spawns")
                .arguments(GenericArguments.string(Texts.of("id")))
                .executor(new PrintSpawnsCommand(this))
                .build();
        game.getCommandDispatcher().register(this, printSpawnsCommand, "ssg-print-spawns");

        CommandSpec printWorldCommand = CommandSpec.builder()
                .description(Texts.of("<id>"))
                .permission("spongesurvivalgames.game.print.world")
                .arguments(GenericArguments.string(Texts.of("id")))
                .executor(new PrintWorldCommand(this))
                .build();
        game.getCommandDispatcher().register(this, printWorldCommand, "ssg-print-world");
    }

    private void registerStoppedCommands() {

        CommandSpec addSpawnLocationCommand = CommandSpec.builder()
                .description(Texts.of("<id> <x> <y> <z>"))
                .permission("spongesurvivalgames.game.add.spawnpoint")
                .arguments(GenericArguments.string(Texts.of("id")), GenericArguments.integer(Texts.of("x")), GenericArguments.integer(Texts.of("y")), GenericArguments.integer(Texts.of("z")))
                .executor(new AddSpawnLocationCommand(this))
                .build();
        game.getCommandDispatcher().register(this, addSpawnLocationCommand, "ssg-add-spawnpoint");

        CommandSpec clearSpawnLocationsCommand = CommandSpec.builder()
                .description(Texts.of("<id>"))
                .permission("spongesurvivalgames.game.clear.spawnpoints")
                .arguments(GenericArguments.string(Texts.of("id")))
                .executor(new ClearSpawnLocationsCommand(this))
                .build();
        game.getCommandDispatcher().register(this, clearSpawnLocationsCommand, "ssg-clear-spawnpoints");

        CommandSpec deleteSurvivalGameCommand = CommandSpec.builder()
                .description(Texts.of("<id>"))
                .permission("spongesurvivalgames.game.delete")
                .arguments(GenericArguments.onlyOne(GenericArguments.string(Texts.of("id"))))
                .executor(new DeleteGameCommand(this))
                .build();
        game.getCommandDispatcher().register(this, deleteSurvivalGameCommand, "ssg-delete");

        CommandSpec readyCommand = CommandSpec.builder()
                .description(Texts.of("<id>"))
                .permission("spongesurvivalgames.game.set.ready")
                .arguments(GenericArguments.onlyOne(GenericArguments.string(Texts.of("id"))))
                .executor(new ReadyGameCommand(this))
                .build();
        game.getCommandDispatcher().register(this, readyCommand, "ssg-ready");

        CommandSpec setCenterLocationCommand = CommandSpec.builder()
                .description(Texts.of("<id> <x> <y> <z>"))
                .permission("spongesurvivalgames.game.set.center")
                .arguments(GenericArguments.string(Texts.of("id")), GenericArguments.integer(Texts.of("x")), GenericArguments.integer(Texts.of("y")), GenericArguments.integer(Texts.of("z")))
                .executor(new SetCenterLocationCommand(this))
                .build();
        game.getCommandDispatcher().register(this, setCenterLocationCommand, "ssg-set-center");

        CommandSpec setCountdownCommand = CommandSpec.builder()
                .description(Texts.of("<id> <countdown>"))
                .permission("spongesurvivalgames.game.set.countdown")
                .arguments(GenericArguments.string(Texts.of("id")), GenericArguments.integer(Texts.of("countdown")))
                .executor(new SetCountdownCommand(this))
                .build();
        game.getCommandDispatcher().register(this, setCountdownCommand, "ssg-set-countdown");

        CommandSpec setExitLocationCommand = CommandSpec.builder()
                .description(Texts.of("<id> <worldName> <x> <y> <z>"))
                .permission("spongesurvivalgames.game.set.exit")
                .arguments(GenericArguments.string(Texts.of("id")), GenericArguments.string(Texts.of("worldName")), GenericArguments.integer(Texts.of("x")), GenericArguments.integer(Texts.of("y")), GenericArguments.integer(Texts.of("z")))
                .executor(new SetExitCommand(this))
                .build();
        game.getCommandDispatcher().register(this, setExitLocationCommand, "ssg-set-exit");

        CommandSpec setPlayerLimitCommand = CommandSpec.builder()
                .description(Texts.of("<id> <playerLimit>"))
                .permission("spongesurvivalgames.game.set.playerlimit")
                .arguments(GenericArguments.string(Texts.of("id")), GenericArguments.integer(Texts.of("playerLimit")))
                .executor(new SetPlayerLimitCommand(this))
                .build();
        game.getCommandDispatcher().register(this, setPlayerLimitCommand, "ssg-set-playerlimit");

        CommandSpec setWorldCommand = CommandSpec.builder()
                .description(Texts.of("<id> <worldName>"))
                .permission("spongesurvivalgames.game.set.world")
                .arguments(GenericArguments.string(Texts.of("id")), GenericArguments.string(Texts.of("worldName")))
                .executor(new SetWorldCommand(this))
                .build();
        game.getCommandDispatcher().register(this, setWorldCommand, "ssg-set-world");
    }

    private void registerReadyCommands() {

        CommandSpec addPlayerToSurvivalGameCommand = CommandSpec.builder()
                .description(Texts.of("<id> <playerName>"))
                .permission("spongesurvivalgames.game.add.player")
                .arguments(GenericArguments.string(Texts.of("id")), GenericArguments.string(Texts.of("playerName")))
                .executor(new AddPlayerToGame(this))
                .build();
        game.getCommandDispatcher().register(this, addPlayerToSurvivalGameCommand, "ssg-add-player");

        CommandSpec removePlayerFromSurvivalGameCommand = CommandSpec.builder()
                .description(Texts.of("<id> <playerName>"))
                .permission("spongesurvivalgames.game.remove.player")
                .arguments(GenericArguments.string(Texts.of("id")), GenericArguments.string(Texts.of("playerName")))
                .executor(new RemovePlayerFromGameCommand(this))
                .build();
        game.getCommandDispatcher().register(this, removePlayerFromSurvivalGameCommand, "ssg-remove-player");

        CommandSpec startSurvivalGameCommand = CommandSpec.builder()
                .description(Texts.of("<id>"))
                .permission("spongesurvivalgames.game.start")
                .arguments(GenericArguments.onlyOne(GenericArguments.string(Texts.of("id"))))
                .executor(new StartGameCommand(this))
                .build();
        game.getCommandDispatcher().register(this, startSurvivalGameCommand, "ssg-start");

        CommandSpec stopSurvivalGameCommand = CommandSpec.builder()
                .description(Texts.of("<id>"))
                .permission("spongesurvivalgames.game.stop")
                .arguments(GenericArguments.onlyOne(GenericArguments.string(Texts.of("id"))))
                .executor(new StopGameCommand(this))
                .build();
        game.getCommandDispatcher().register(this, stopSurvivalGameCommand, "ssg-stop");

    }

    private void registerRunningCommands() {

        CommandSpec forceStopSurvivalGameCommand = CommandSpec.builder()
                .description(Texts.of("<id>"))
                .permission("spongesurvivalgames.game.forcestop")
                .arguments(GenericArguments.onlyOne(GenericArguments.string(Texts.of("id"))))
                .executor(new ForceStopGameCommand(this))
                .build();
        game.getCommandDispatcher().register(this, forceStopSurvivalGameCommand, "ssg-forcestop");
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
