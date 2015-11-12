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
import io.github.m0pt0pmatt.spongesurvivalgames.commands.game.AddSpectatorCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.game.BackupCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.game.LoadCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.game.RemoveSpectatorCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.game.SaveCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.game.print.*;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.game.ready.AddPlayerCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.game.ready.RemovePlayerCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.game.ready.StartGameCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.game.ready.StopGameCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.game.running.ForceDeathmatchCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.game.running.ForceStopGameCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.game.running.RefillChestsCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.game.running.SponsorCommand;
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
        registerCommands();
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

    private void registerCommands() {
        commandTrie.add(
                new String[]{CommandKeywords.CREATE},
                new String[]{CommandArgs.ID},
                new CreateGameCommand());
        commandTrie.add(
                new String[]{CommandKeywords.LIST},
                new ListGamesCommand());
        commandTrie.add(
                new String[]{CommandKeywords.DELETE},
                new String[]{CommandArgs.ID},
                new DeleteGameCommand());
        commandTrie.add(
                new String[]{CommandKeywords.PRINT, CommandKeywords.CENTER},
                new String[]{CommandArgs.ID},
                new PrintCenterCommand());
        commandTrie.add(
                new String[]{CommandKeywords.PRINT, CommandKeywords.CHEST, CommandKeywords.MIDPOINT},
                new String[]{CommandArgs.ID},
                new PrintChestMidpointCommand());
        commandTrie.add(
                new String[]{CommandKeywords.PRINT, CommandKeywords.CHEST, CommandKeywords.RANGE},
                new String[]{CommandArgs.ID},
                new PrintChestRangeCommand());
        commandTrie.add(
                new String[]{CommandKeywords.PRINT, CommandKeywords.COUNTDOWN},
                new String[]{CommandArgs.ID},
                new PrintCountdownCommand());
        commandTrie.add(
                new String[]{CommandKeywords.PRINT, CommandKeywords.EXIT},
                new String[]{CommandArgs.ID},
                new PrintExitCommand());
        commandTrie.add(
                new String[]{CommandKeywords.PRINT, CommandKeywords.PLAYER_LIMIT},
                new String[]{CommandArgs.ID},
                new PrintPlayerLimitCommand());
        commandTrie.add(
                new String[]{CommandKeywords.PRINT, CommandKeywords.SPAWNS},
                new String[]{CommandArgs.ID},
                new PrintSpawnsCommand());
        commandTrie.add(
                new String[]{CommandKeywords.PRINT, CommandKeywords.WORLD},
                new String[]{CommandArgs.ID},
                new PrintWorldCommand());
        commandTrie.add(
                new String[]{CommandKeywords.PRINT, CommandKeywords.BOUNDS},
                new String[]{CommandArgs.ID},
                new PrintBoundsCommand());
        commandTrie.add(
                new String[]{CommandKeywords.PRINT, CommandKeywords.LOOT},
                new String[]{CommandArgs.ID},
                new PrintLootCommand());
        commandTrie.add(
                new String[]{CommandKeywords.PRINT, CommandKeywords.DEATHMATCH, CommandKeywords.RADIUS},
                new String[]{CommandArgs.ID},
                new PrintDeathmatchRadiusCommand());
        commandTrie.add(
                new String[]{CommandKeywords.PRINT, CommandKeywords.DEATHMATCH, CommandKeywords.TIME},
                new String[]{CommandArgs.ID},
                new PrintDeathmatchTimeCommand());
        commandTrie.add(
                new String[]{CommandKeywords.PRINT, CommandKeywords.SPONSORS},
                new PrintSponsorsCommand()
        );
        commandTrie.add(
                new String[]{CommandKeywords.ADD, CommandKeywords.SPAWN},
                new String[]{CommandArgs.ID, CommandArgs.X, CommandArgs.Y, CommandArgs.Z},
                new AddSpawnCommand());
        commandTrie.add(
                new String[]{CommandKeywords.CLEAR, CommandKeywords.SPAWNS},
                new String[]{CommandArgs.ID},
                new ClearSpawnpointsCommand());
        commandTrie.add(
                new String[]{CommandKeywords.SET, CommandKeywords.CENTER},
                new String[]{CommandArgs.ID, CommandArgs.X, CommandArgs.Y, CommandArgs.Z},
                new SetCenterLocationCommand());
        commandTrie.add(
                new String[]{CommandKeywords.SET, CommandKeywords.CHEST, CommandKeywords.MIDPOINT},
                new String[]{CommandArgs.ID, CommandArgs.MIDPOINT,},
                new SetChestMidpointCommand());
        commandTrie.add(
                new String[]{CommandKeywords.SET, CommandKeywords.CHEST, CommandKeywords.RANGE},
                new String[]{CommandArgs.ID, CommandArgs.RANGE},
                new SetChestRangeCommand());
        commandTrie.add(
                new String[]{CommandKeywords.SET, CommandKeywords.COUNTDOWN},
                new String[]{CommandArgs.ID, CommandArgs.COUNTDOWN},
                new SetCountdownCommand());
        commandTrie.add(
                new String[]{CommandKeywords.SET, CommandKeywords.EXIT},
                new String[]{CommandArgs.ID, CommandArgs.WORLDNAME, CommandArgs.X, CommandArgs.Y, CommandArgs.Z},
                new SetExitCommand());
        commandTrie.add(
                new String[]{CommandKeywords.SET, CommandKeywords.PLAYER_LIMIT},
                new String[]{CommandArgs.ID, CommandArgs.PLAYER_LIMIT},
                new SetPlayerLimitCommand());
        commandTrie.add(
                new String[]{CommandKeywords.SET, CommandKeywords.WORLD},
                new String[]{CommandArgs.ID, CommandArgs.WORLDNAME},
                new SetWorldCommand());
        commandTrie.add(
                new String[]{CommandKeywords.SET, CommandKeywords.BOUNDS},
                new String[]{CommandArgs.ID, CommandArgs.XMIN, CommandArgs.XMAX, CommandArgs.ZMIN, CommandArgs.ZMAX},
                new SetBoundsCommand());
        commandTrie.add(
                new String[]{CommandKeywords.SET, CommandKeywords.DEATHMATCH, CommandKeywords.RADIUS},
                new String[]{CommandArgs.ID, CommandArgs.DEATHMATCHRADIUS},
                new SetDeathmatchRadiusCommand());
        commandTrie.add(
                new String[]{CommandKeywords.SET, CommandKeywords.DEATHMATCH, CommandKeywords.TIME},
                new String[]{CommandArgs.ID, CommandArgs.DEATHMATCHTIME},
                new SetDeathmatchTimeCommand());
        commandTrie.add(
                new String[]{CommandKeywords.ADD, CommandKeywords.HELD, CommandKeywords.LOOT},
                new String[]{CommandArgs.ID, CommandArgs.WEIGHT},
                new AddHeldLootCommand());
        commandTrie.add(
                new String[]{CommandKeywords.READY},
                new String[]{CommandArgs.ID},
                new ReadyGameCommand());
        commandTrie.add(
                new String[]{CommandKeywords.ADD, CommandKeywords.PLAYER},
                new String[]{CommandArgs.ID, CommandArgs.PLAYERNAME},
                new AddPlayerCommand());
        commandTrie.add(
                new String[]{CommandKeywords.REMOVE, CommandKeywords.PLAYER},
                new String[]{CommandArgs.ID, CommandArgs.PLAYERNAME},
                new RemovePlayerCommand());
        commandTrie.add(
                new String[]{CommandKeywords.ADD, CommandKeywords.SPECTATOR},
                new String[]{CommandArgs.ID, CommandArgs.PLAYERNAME},
                new AddSpectatorCommand());
        commandTrie.add(
                new String[]{CommandKeywords.REMOVE, CommandKeywords.SPECTATOR},
                new String[]{CommandArgs.ID, CommandArgs.PLAYERNAME},
                new RemoveSpectatorCommand());
        commandTrie.add(
                new String[]{CommandKeywords.START},
                new String[]{CommandArgs.ID},
                new StartGameCommand());
        commandTrie.add(
                new String[]{CommandKeywords.STOP},
                new String[]{CommandArgs.ID},
                new StopGameCommand());
        commandTrie.add(
                new String[]{CommandKeywords.FORCE, CommandKeywords.STOP},
                new String[]{CommandArgs.ID},
                new ForceStopGameCommand());
        commandTrie.add(
                new String[]{CommandKeywords.FORCE, CommandKeywords.DEATHMATCH},
                new String[]{CommandArgs.ID},
                new ForceDeathmatchCommand());
        commandTrie.add(
                new String[]{CommandKeywords.LOAD},
                new String[]{CommandArgs.ID, CommandArgs.FILENAME, CommandArgs.OVERWRITE},
                new LoadCommand());
        commandTrie.add(
                new String[]{CommandKeywords.CHEST},
                new String[]{CommandArgs.ID},
                new GetChestsCommand());
        commandTrie.add(
                new String[]{CommandKeywords.SAVE},
                new String[]{CommandArgs.ID, CommandArgs.FILENAME},
                new SaveCommand());
        commandTrie.add(
                new String[]{CommandKeywords.GIVE, CommandKeywords.SPONSOR},
                new String[]{CommandArgs.ID, CommandArgs.PLAYERNAME, CommandArgs.SPONSOR},
                new SponsorCommand()
        );
        commandTrie.add(
                new String[]{CommandKeywords.REFILL, CommandKeywords.CHESTS},
                new String[]{CommandArgs.ID},
                new RefillChestsCommand()
        );
        commandTrie.add(
                new String[]{CommandKeywords.BACKUP},
                new String[]{CommandArgs.ID, CommandArgs.FILENAME},
                new BackupCommand()
        );
    }

    @Override
    public void onLoad() {
        if (!getDataFolder().mkdirs()) {
            Bukkit.getLogger().warning("Unable to make plugin folder");
        }
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

