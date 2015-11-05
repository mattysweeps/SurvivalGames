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

import io.github.m0pt0pmatt.spongesurvivalgames.commands.CommandKeywords;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.CreateGameCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.ListGamesCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.SurvivalGamesCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.game.LoadCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.game.SaveCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.game.print.*;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.game.ready.AddPlayerCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.game.ready.RemovePlayerCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.game.ready.StartGameCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.game.ready.StopGameCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.game.running.ForceStopGameCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.game.stopped.*;
import io.github.m0pt0pmatt.spongesurvivalgames.events.PlayerEventListener;
import io.github.m0pt0pmatt.spongesurvivalgames.loot.Loot;
import io.github.m0pt0pmatt.spongesurvivalgames.util.LoadedTrie;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * SpongeSurvivalGames Sponge Plugin
 */
public class BukkitSurvivalGamesPlugin extends JavaPlugin {

    public static final LoadedTrie<String, SurvivalGamesCommand> commandTrie = new LoadedTrie<>();
    public static final Map<String, SurvivalGame> survivalGameMap = new HashMap<>();
    private static final CommandExecutor commandExecutor = new SurvivalGamesCommandExecutor();
    private static final TabCompleter tabCompleter = new SurvivalGamesTabCompleter();
    public static BukkitSurvivalGamesPlugin plugin;

    public BukkitSurvivalGamesPlugin() {
        BukkitSurvivalGamesPlugin.plugin = this;

        //Register all commands
        commandTrie.add(new String[]{CommandKeywords.CREATE, CommandKeywords.ID}, new CreateGameCommand());
        commandTrie.add(new String[]{CommandKeywords.LIST}, new ListGamesCommand());
        commandTrie.add(new String[]{CommandKeywords.DELETE, CommandKeywords.ID}, new DeleteGameCommand());
        commandTrie.add(new String[]{CommandKeywords.PRINT, CommandKeywords.CENTER, CommandKeywords.ID}, new PrintCenterCommand());
        commandTrie.add(new String[]{CommandKeywords.PRINT, CommandKeywords.CHEST, CommandKeywords.MIDPOINT, CommandKeywords.ID}, new PrintChestMidpointCommand());
        commandTrie.add(new String[]{CommandKeywords.PRINT, CommandKeywords.CHEST, CommandKeywords.RANGE, CommandKeywords.ID}, new PrintChestRangeCommand());
        commandTrie.add(new String[]{CommandKeywords.PRINT, CommandKeywords.COUNTDOWN, CommandKeywords.ID}, new PrintCountdownCommand());
        commandTrie.add(new String[]{CommandKeywords.PRINT, CommandKeywords.EXIT, CommandKeywords.ID}, new PrintExitCommand());
        commandTrie.add(new String[]{CommandKeywords.PRINT, CommandKeywords.PLAYER_LIMIT, CommandKeywords.ID}, new PrintPlayerLimitCommand());
        commandTrie.add(new String[]{CommandKeywords.PRINT, CommandKeywords.SPAWNS, CommandKeywords.ID}, new PrintSpawnsCommand());
        commandTrie.add(new String[]{CommandKeywords.PRINT, CommandKeywords.WORLD, CommandKeywords.ID}, new PrintWorldCommand());
        commandTrie.add(new String[]{CommandKeywords.PRINT, CommandKeywords.BOUNDS, CommandKeywords.ID}, new PrintBoundsCommand());
        commandTrie.add(new String[]{CommandKeywords.PRINT, CommandKeywords.LOOT, CommandKeywords.ID}, new PrintLootCommand());
        commandTrie.add(new String[]{CommandKeywords.ADD, CommandKeywords.SPAWN, CommandKeywords.ID, CommandKeywords.X, CommandKeywords.Y, CommandKeywords.Z}, new AddSpawnCommand());
        commandTrie.add(new String[]{CommandKeywords.CLEAR, CommandKeywords.SPAWNS, CommandKeywords.ID}, new ClearSpawnpointsCommand());
        commandTrie.add(new String[]{CommandKeywords.SET, CommandKeywords.CENTER, CommandKeywords.ID, CommandKeywords.X, CommandKeywords.Y, CommandKeywords.Z}, new SetCenterLocationCommand());
        commandTrie.add(new String[]{CommandKeywords.SET, CommandKeywords.CHEST, CommandKeywords.MIDPOINT, CommandKeywords.ID, CommandKeywords.MIDPOINT,}, new SetChestMidpointCommand());
        commandTrie.add(new String[]{CommandKeywords.SET, CommandKeywords.CHEST, CommandKeywords.RANGE, CommandKeywords.ID, CommandKeywords.RANGE}, new SetChestRangeCommand());
        commandTrie.add(new String[]{CommandKeywords.SET, CommandKeywords.COUNTDOWN, CommandKeywords.ID, CommandKeywords.COUNTDOWN}, new SetCountdownCommand());
        commandTrie.add(new String[]{CommandKeywords.SET, CommandKeywords.EXIT, CommandKeywords.ID, CommandKeywords.WORLDNAME, CommandKeywords.X, CommandKeywords.Y, CommandKeywords.Z}, new SetExitCommand());
        commandTrie.add(new String[]{CommandKeywords.SET, CommandKeywords.PLAYER_LIMIT, CommandKeywords.ID, CommandKeywords.PLAYER_LIMIT}, new SetPlayerLimitCommand());
        commandTrie.add(new String[]{CommandKeywords.SET, CommandKeywords.WORLD, CommandKeywords.ID, CommandKeywords.WORLDNAME}, new SetWorldCommand());
        commandTrie.add(new String[]{CommandKeywords.SET, CommandKeywords.BOUNDS, CommandKeywords.ID, CommandKeywords.XMIN, CommandKeywords.XMAX, CommandKeywords.YMIN, CommandKeywords.YMAX, CommandKeywords.ZMIN, CommandKeywords.ZMAX}, new SetBoundsCommand());
        commandTrie.add(new String[]{CommandKeywords.ADD, CommandKeywords.HELD, CommandKeywords.LOOT, CommandKeywords.ID, CommandKeywords.WEIGHT}, new AddHeldLootCommand());
        commandTrie.add(new String[]{CommandKeywords.READY, CommandKeywords.ID}, new ReadyGameCommand());
        commandTrie.add(new String[]{CommandKeywords.ADD, CommandKeywords.PLAYER, CommandKeywords.ID, CommandKeywords.PLAYERNAME}, new AddPlayerCommand());
        commandTrie.add(new String[]{CommandKeywords.REMOVE, CommandKeywords.PLAYER, CommandKeywords.ID, CommandKeywords.PLAYERNAME}, new RemovePlayerCommand());
        commandTrie.add(new String[]{CommandKeywords.START, CommandKeywords.ID}, new StartGameCommand());
        commandTrie.add(new String[]{CommandKeywords.STOP, CommandKeywords.ID}, new StopGameCommand());
        commandTrie.add(new String[]{CommandKeywords.FORCE_STOP, CommandKeywords.ID}, new ForceStopGameCommand());
        commandTrie.add(new String[]{CommandKeywords.LOAD, CommandKeywords.ID, CommandKeywords.FILENAME}, new LoadCommand());
        commandTrie.add(new String[]{CommandKeywords.SAVE, CommandKeywords.ID, CommandKeywords.FILENAME}, new SaveCommand());
    }

    /**
     * Helper function to get a set of players from their UUIDs
     *
     * @param playerUUIDs the UUIDs of the players
     * @return All players who were logged in to the server
     */
    public static Set<Player> getPlayers(Set<UUID> playerUUIDs) {
        return playerUUIDs.stream()
                .map(uuid -> Bukkit.getServer().getPlayer(uuid))
                .filter((player) -> player != null)
                .collect(Collectors.toSet());
    }

    @Override
    public void onEnable() {
        survivalGameMap.clear();
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerEventListener(), this);
        Bukkit.getLogger().info("Sponge Survival Games Plugin Enabled");
        getCommand(CommandKeywords.SSG).setExecutor(commandExecutor);
        getCommand(CommandKeywords.SSG).setTabCompleter(tabCompleter);
        
        Loot.registerAliases();
    }

}

