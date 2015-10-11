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

import com.google.inject.Inject;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.CreateGameCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.ListGamesCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.game.LoadCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.game.SaveCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.game.print.*;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.game.ready.AddPlayerCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.game.ready.RemovePlayerCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.game.ready.StartGameCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.game.ready.StopGameCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.game.running.ForceStopGameCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.game.stopped.*;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
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

    public static Game game;
    public static Logger logger;
    public static Map<String, SurvivalGame> survivalGameMap;
    public static SpongeSurvivalGamesPlugin plugin;

    @Inject
    public SpongeSurvivalGamesPlugin(Logger logger) {
        SpongeSurvivalGamesPlugin.plugin = this;
        SpongeSurvivalGamesPlugin.logger = logger;
        SpongeSurvivalGamesPlugin.survivalGameMap = new HashMap<>();
    }

    public static Set<Player> getPlayers(Set<UUID> playerUUIDs) {
        return playerUUIDs.stream()
                .map(uuid -> game.getServer().getPlayer(uuid))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        SpongeSurvivalGamesPlugin.game = event.getGame();
        registerCommands();
        survivalGameMap.clear();
        logger.info("Sponge Survival Games Plugin Enabled");
    }

    private void registerCommands() {

        CommandSpec createSurvivalGameCommand = CommandSpec.builder()
                .description(Texts.of("<id>"))
                .permission("spongesurvivalgames.game.create")
                .arguments(GenericArguments.onlyOne(GenericArguments.string(Texts.of("id"))))
                .executor(new CreateGameCommand())
                .build();
        game.getCommandDispatcher().register(this, createSurvivalGameCommand, "ssg-create");

        CommandSpec listSurvivalGamesCommand = CommandSpec.builder()
                .description(Texts.of(""))
                .permission("spongesurvivalgames.game.list")
                .executor(new ListGamesCommand())
                .build();
        game.getCommandDispatcher().register(this, listSurvivalGamesCommand, "ssg-list");

        registerGameCommands();
    }

    private void registerGameCommands() {
        CommandSpec printCommands = registerPrintCommands();
        CommandSpec addCommands = registerAddCommands();
        CommandSpec removeCommands = registerRemoveCommands();
        CommandSpec setCommands = registerSetCommands();
        CommandSpec clearCommands = registerClearCommands();

        CommandSpec createCommand = CommandSpec.builder()
                .description(Texts.of("<id>"))
                .permission("spongesurvivalgames.game.create")
                .arguments(GenericArguments.onlyOne(GenericArguments.string(Texts.of("id"))))
                .executor(new CreateGameCommand())
                .build();

        CommandSpec deleteCommand = CommandSpec.builder()
                .description(Texts.of("<id>"))
                .permission("spongesurvivalgames.game.delete")
                .arguments(GenericArguments.onlyOne(GenericArguments.string(Texts.of("id"))))
                .executor(new DeleteGameCommand())
                .build();

        CommandSpec readyCommand = CommandSpec.builder()
                .description(Texts.of("<id>"))
                .permission("spongesurvivalgames.game.set.ready")
                .arguments(GenericArguments.onlyOne(GenericArguments.string(Texts.of("id"))))
                .executor(new ReadyGameCommand())
                .build();

        CommandSpec startCommand = CommandSpec.builder()
                .description(Texts.of("<id>"))
                .permission("spongesurvivalgames.game.start")
                .arguments(GenericArguments.onlyOne(GenericArguments.string(Texts.of("id"))))
                .executor(new StartGameCommand())
                .build();

        CommandSpec stopCommand = CommandSpec.builder()
                .description(Texts.of("<id>"))
                .permission("spongesurvivalgames.game.stop")
                .arguments(GenericArguments.onlyOne(GenericArguments.string(Texts.of("id"))))
                .executor(new StopGameCommand())
                .build();

        CommandSpec forceStopCommand = CommandSpec.builder()
                .description(Texts.of("<id>"))
                .permission("spongesurvivalgames.game.forcestop")
                .arguments(GenericArguments.onlyOne(GenericArguments.string(Texts.of("id"))))
                .executor(new ForceStopGameCommand())
                .build();

        CommandSpec loadCommand = CommandSpec.builder()
                .description(Texts.of("<id> <fileName>"))
                .permission("spongesurvivalgames.game.save")
                .arguments(GenericArguments.string(Texts.of("id")), GenericArguments.string(Texts.of("fileName")))
                .executor(new LoadCommand())
                .build();

        CommandSpec saveCommand = CommandSpec.builder()
                .description(Texts.of("<id> <fileName>"))
                .permission("spongesurvivalgames.game.load")
                .arguments(GenericArguments.string(Texts.of("id")), GenericArguments.string(Texts.of("fileName")))
                .executor(new SaveCommand())
                .build();

        CommandSpec ssgCommand = CommandSpec.builder()
                .permission("spongesurvivalgames")
                .child(printCommands, "print")
                .child(addCommands, "add")
                .child(removeCommands, "remove")
                .child(setCommands, "set")
                .child(clearCommands, "clear")
                .child(createCommand, "create")
                .child(deleteCommand, "delete")
                .child(readyCommand, "ready")
                .child(startCommand, "start")
                .child(stopCommand, "stop")
                .child(forceStopCommand, "forcestop")
                .child(loadCommand, "load")
                .child(saveCommand, "save")
                .build();
        game.getCommandDispatcher().register(this, ssgCommand, "ssg");
    }

    private CommandSpec registerPrintCommands() {
        CommandSpec printCenterCommand = CommandSpec.builder()
                .description(Texts.of("<id>"))
                .permission("spongesurvivalgames.game.print.center")
                .arguments(GenericArguments.string(Texts.of("id")))
                .executor(new PrintCenterCommand())
                .build();

        CommandSpec printCountdownCommand = CommandSpec.builder()
                .description(Texts.of("<id>"))
                .permission("spongesurvivalgames.game.print.countdown")
                .arguments(GenericArguments.string(Texts.of("id")))
                .executor(new PrintCountdownCommand())
                .build();

        CommandSpec printExitCommand = CommandSpec.builder()
                .description(Texts.of("<id>"))
                .permission("spongesurvivalgames.game.print.exit")
                .arguments(GenericArguments.string(Texts.of("id")))
                .executor(new PrintExitCommand())
                .build();

        CommandSpec printPlayerLimitCommand = CommandSpec.builder()
                .description(Texts.of("<id>"))
                .permission("spongesurvivalgames.game.print.playerLimit")
                .arguments(GenericArguments.string(Texts.of("id")))
                .executor(new PrintPlayerLimitCommand())
                .build();

        CommandSpec printSpawnsCommand = CommandSpec.builder()
                .description(Texts.of("<id>"))
                .permission("spongesurvivalgames.game.print.spawns")
                .arguments(GenericArguments.string(Texts.of("id")))
                .executor(new PrintSpawnsCommand())
                .build();

        CommandSpec printWorldCommand = CommandSpec.builder()
                .description(Texts.of("<id>"))
                .permission("spongesurvivalgames.game.print.world")
                .arguments(GenericArguments.string(Texts.of("id")))
                .executor(new PrintWorldCommand())
                .build();

        CommandSpec printChestMidpointCommand = CommandSpec.builder()
                .description(Texts.of("<id>"))
                .permission("spongesurvivalgames.game.print.chestMidpoint")
                .arguments(GenericArguments.string(Texts.of("id")))
                .executor(new PrintChestMidpointCommand())
                .build();

        CommandSpec printChestRangeCommand = CommandSpec.builder()
                .description(Texts.of("<id>"))
                .permission("spongesurvivalgames.game.print.chestRange")
                .arguments(GenericArguments.string(Texts.of("id")))
                .executor(new PrintChestRangeCommand())
                .build();

        return CommandSpec.builder()
                .permission("spongesurvivalgames.game.print")
                .child(printCenterCommand, "center")
                .child(printCountdownCommand, "countdown")
                .child(printExitCommand, "exit")
                .child(printPlayerLimitCommand, "playerLimit")
                .child(printSpawnsCommand, "spawns")
                .child(printWorldCommand, "world")
                .child(CommandSpec.builder()
                                .child(printChestMidpointCommand, "midpoint")
                                .child(printChestRangeCommand, "range")
                                .build(), "chest"
                )
                .build();
    }

    private CommandSpec registerAddCommands() {
        CommandSpec addSpawnCommand = CommandSpec.builder()
                .description(Texts.of("<id> <x> <y> <z>"))
                .permission("spongesurvivalgames.game.add.spawn")
                .arguments(GenericArguments.string(Texts.of("id")), GenericArguments.integer(Texts.of("x")), GenericArguments.integer(Texts.of("y")), GenericArguments.integer(Texts.of("z")))
                .executor(new AddSpawnCommand())
                .build();

        CommandSpec addPlayerCommand = CommandSpec.builder()
                .description(Texts.of("<id> <playerName>"))
                .permission("spongesurvivalgames.game.add.player")
                .arguments(GenericArguments.string(Texts.of("id")), GenericArguments.string(Texts.of("playerName")))
                .executor(new AddPlayerCommand())
                .build();

        return CommandSpec.builder()
                .permission("spongesurvivalgames.game.add")
                .child(addSpawnCommand, "spawn")
                .child(addPlayerCommand, "player")
                .build();
    }

    private CommandSpec registerRemoveCommands() {
        CommandSpec removePlayerFromSurvivalGameCommand = CommandSpec.builder()
                .description(Texts.of("<id> <playerName>"))
                .permission("spongesurvivalgames.game.remove.player")
                .arguments(GenericArguments.string(Texts.of("id")), GenericArguments.string(Texts.of("playerName")))
                .executor(new RemovePlayerCommand())
                .build();

        return CommandSpec.builder()
                .permission("spongesurvivalgames.game.add")
                .child(removePlayerFromSurvivalGameCommand, "player")
                .build();
    }

    private CommandSpec registerSetCommands() {
        CommandSpec setCenterLocationCommand = CommandSpec.builder()
                .description(Texts.of("<id> <x> <y> <z>"))
                .permission("spongesurvivalgames.game.set.center")
                .arguments(GenericArguments.string(Texts.of("id")), GenericArguments.integer(Texts.of("x")), GenericArguments.integer(Texts.of("y")), GenericArguments.integer(Texts.of("z")))
                .executor(new SetCenterLocationCommand())
                .build();

        CommandSpec setCountdownCommand = CommandSpec.builder()
                .description(Texts.of("<id> <countdown>"))
                .permission("spongesurvivalgames.game.set.countdown")
                .arguments(GenericArguments.string(Texts.of("id")), GenericArguments.integer(Texts.of("countdown")))
                .executor(new SetCountdownCommand())
                .build();

        CommandSpec setExitLocationCommand = CommandSpec.builder()
                .description(Texts.of("<id> <worldName> <x> <y> <z>"))
                .permission("spongesurvivalgames.game.set.exit")
                .arguments(GenericArguments.string(Texts.of("id")), GenericArguments.string(Texts.of("worldName")), GenericArguments.integer(Texts.of("x")), GenericArguments.integer(Texts.of("y")), GenericArguments.integer(Texts.of("z")))
                .executor(new SetExitCommand())
                .build();

        CommandSpec setPlayerLimitCommand = CommandSpec.builder()
                .description(Texts.of("<id> <playerLimit>"))
                .permission("spongesurvivalgames.game.set.playerlimit")
                .arguments(GenericArguments.string(Texts.of("id")), GenericArguments.integer(Texts.of("playerLimit")))
                .executor(new SetPlayerLimitCommand())
                .build();

        CommandSpec setWorldCommand = CommandSpec.builder()
                .description(Texts.of("<id> <worldName>"))
                .permission("spongesurvivalgames.game.set.world")
                .arguments(GenericArguments.string(Texts.of("id")), GenericArguments.string(Texts.of("worldName")))
                .executor(new SetWorldCommand())
                .build();

        CommandSpec setChestMidpointCommand = CommandSpec.builder()
                .description(Texts.of("<id> <chestMidpoint>"))
                .permission("spongesurvivalgames.game.set.chestMidpoint")
                .arguments(GenericArguments.string(Texts.of("id")), GenericArguments.string(Texts.of("chestMidpoint")))
                .executor(new SetChestMidpointCommand())
                .build();

        CommandSpec setChestRangeCommand = CommandSpec.builder()
                .description(Texts.of("<id> <chestRange>"))
                .permission("spongesurvivalgames.game.set.chestMidpoint")
                .arguments(GenericArguments.string(Texts.of("id")), GenericArguments.string(Texts.of("chestRange")))
                .executor(new SetChestRangeCommand())
                .build();

        return CommandSpec.builder()
                .permission("spongesurvivalgames.game.add")
                .child(setCenterLocationCommand, "center")
                .child(setCountdownCommand, "countdown")
                .child(setExitLocationCommand, "exit")
                .child(setPlayerLimitCommand, "playerLimit")
                .child(setWorldCommand, "world")
                .child(CommandSpec.builder()
                        .child(setChestMidpointCommand, "midpoint")
                        .child(setChestRangeCommand, "range")
                        .build(), "chest"
                )
                .build();
    }

    private CommandSpec registerClearCommands() {
        CommandSpec clearSpawnsCommand = CommandSpec.builder()
                .description(Texts.of("<id>"))
                .permission("spongesurvivalgames.game.clear.spawns")
                .arguments(GenericArguments.string(Texts.of("id")))
                .executor(new ClearSpawnpointsCommand())
                .build();

        return CommandSpec.builder()
                .permission("spongesurvivalgames.game.clear")
                .child(clearSpawnsCommand, "spawns")
                .build();
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
