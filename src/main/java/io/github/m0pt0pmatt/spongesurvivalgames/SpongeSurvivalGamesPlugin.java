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
import io.github.m0pt0pmatt.spongesurvivalgames.backup.Backup;
import io.github.m0pt0pmatt.spongesurvivalgames.command.CommandArgs;
import io.github.m0pt0pmatt.spongesurvivalgames.command.CommandKeywords;
import io.github.m0pt0pmatt.spongesurvivalgames.command.CreateGameCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.ListGamesCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.RestoreGameCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.SurvivalGamesCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.game.AddSpectatorCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.game.BackupCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.game.LoadCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.game.RemoveSpectatorCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.game.SaveCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.game.print.PrintBoundsCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.game.print.PrintCenterCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.game.print.PrintChestMidpointCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.game.print.PrintChestRangeCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.game.print.PrintCountdownCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.game.print.PrintDeathmatchRadiusCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.game.print.PrintDeathmatchTimeCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.game.print.PrintExitCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.game.print.PrintLootCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.game.print.PrintPlayerLimitCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.game.print.PrintSpawnsCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.game.print.PrintSponsorsCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.game.print.PrintWorldCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.game.ready.AddPlayerCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.game.ready.RemovePlayerCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.game.ready.StartGameCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.game.ready.StopGameCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.game.running.ForceDeathmatchCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.game.running.ForceStopGameCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.game.running.RefillChestsCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.game.running.SponsorCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.game.stopped.AddHeldLootCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.game.stopped.AddSpawnCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.game.stopped.ClearSpawnpointsCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.game.stopped.DeleteGameCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.game.stopped.GetChestsCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.game.stopped.ReadyGameCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.game.stopped.SetBoundsCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.game.stopped.SetCenterLocationCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.game.stopped.SetChestMidpointCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.game.stopped.SetChestRangeCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.game.stopped.SetCountdownCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.game.stopped.SetDeathmatchRadiusCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.game.stopped.SetDeathmatchTimeCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.game.stopped.SetExitCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.game.stopped.SetPlayerLimitCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.game.stopped.SetWorldCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.event.PlayerEventListener;
import io.github.m0pt0pmatt.spongesurvivalgames.loot.Loot;
import io.github.m0pt0pmatt.spongesurvivalgames.util.LoadedTrie;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.scheduler.SpongeExecutorService;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * SpongeSurvivalGames Sponge Plugin
 */
@Plugin(id = "spongesurvivalgames", name = "Sponge Survival Games", version = "0.1")
public class SpongeSurvivalGamesPlugin {

    public static final LoadedTrie<String, SurvivalGamesCommand> commandTrie = new LoadedTrie<>();
    public static final Map<SurvivalGamesCommand, List<CommandArgs>> commandArgs = new HashMap<>();
    public static final Map<String, SurvivalGame> survivalGameMap = new HashMap<>();
    private static final CommandExecutor commandExecutor = new SurvivalGamesCommandExecutor();
    private static final TabCompleter tabCompleter = new SurvivalGamesTabCompleter();
    public static SpongeSurvivalGamesPlugin plugin;
    public static SpongeExecutorService executorService;

    @Inject
    private static Logger LOGGER;

    public SpongeSurvivalGamesPlugin() {
        SpongeSurvivalGamesPlugin.plugin = this;

        executorService = Sponge.getScheduler().createSyncExecutor(this);

        //Register all commands
        registerCommands();
    }

    public static SurvivalGame getGame(String id) {
        return survivalGameMap.get(id);
    }

    /**
     * Helper function to get a set of players from their UUIDs
     *
     * @param playerUUIDs the UUIDs of the players
     * @return All players who were logged in to the server
     */
    public static Set<Player> getPlayers(Set<UUID> playerUUIDs) {
        return playerUUIDs.stream()
            .map(uuid -> Sponge.getServer().getPlayer(uuid))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toSet());
    }

    private void registerCommand(String[] words, CommandArgs[] args, SurvivalGamesCommand command) {
        commandTrie.add(words, command);
        List<CommandArgs> list = new LinkedList<>();
        Collections.addAll(list, args);
        commandArgs.put(command, list);
    }

    private void registerCommand(String[] words, SurvivalGamesCommand command) {
        commandTrie.add(words, command);
    }


    private void registerCommands() {
        registerCommand(
            new String[]{CommandKeywords.CREATE},
            new CommandArgs[]{CommandArgs.ID},
            new CreateGameCommand());
        registerCommand(
            new String[]{CommandKeywords.LIST},
            new ListGamesCommand());
        registerCommand(
            new String[]{CommandKeywords.DELETE},
            new CommandArgs[]{CommandArgs.ID},
            new DeleteGameCommand());
        registerCommand(
            new String[]{CommandKeywords.PRINT, CommandKeywords.CENTER},
            new CommandArgs[]{CommandArgs.ID},
            new PrintCenterCommand());
        registerCommand(
            new String[]{CommandKeywords.PRINT, CommandKeywords.CHEST, CommandKeywords.MIDPOINT},
            new CommandArgs[]{CommandArgs.ID},
            new PrintChestMidpointCommand());
        registerCommand(
            new String[]{CommandKeywords.PRINT, CommandKeywords.CHEST, CommandKeywords.RANGE},
            new CommandArgs[]{CommandArgs.ID},
            new PrintChestRangeCommand());
        registerCommand(
            new String[]{CommandKeywords.PRINT, CommandKeywords.COUNTDOWN},
            new CommandArgs[]{CommandArgs.ID},
            new PrintCountdownCommand());
        registerCommand(
            new String[]{CommandKeywords.PRINT, CommandKeywords.EXIT},
            new CommandArgs[]{CommandArgs.ID},
            new PrintExitCommand());
        registerCommand(
            new String[]{CommandKeywords.PRINT, CommandKeywords.PLAYER_LIMIT},
            new CommandArgs[]{CommandArgs.ID},
            new PrintPlayerLimitCommand());
        registerCommand(
            new String[]{CommandKeywords.PRINT, CommandKeywords.SPAWNS},
            new CommandArgs[]{CommandArgs.ID},
            new PrintSpawnsCommand());
        registerCommand(
            new String[]{CommandKeywords.PRINT, CommandKeywords.WORLD},
            new CommandArgs[]{CommandArgs.ID},
            new PrintWorldCommand());
        registerCommand(
            new String[]{CommandKeywords.PRINT, CommandKeywords.BOUNDS},
            new CommandArgs[]{CommandArgs.ID},
            new PrintBoundsCommand());
        registerCommand(
            new String[]{CommandKeywords.PRINT, CommandKeywords.LOOT},
            new CommandArgs[]{CommandArgs.ID},
            new PrintLootCommand());
        registerCommand(
            new String[]{CommandKeywords.PRINT, CommandKeywords.DEATHMATCH, CommandKeywords.RADIUS},
            new CommandArgs[]{CommandArgs.ID},
            new PrintDeathmatchRadiusCommand());
        registerCommand(
            new String[]{CommandKeywords.PRINT, CommandKeywords.DEATHMATCH, CommandKeywords.TIME},
            new CommandArgs[]{CommandArgs.ID},
            new PrintDeathmatchTimeCommand());
        registerCommand(
            new String[]{CommandKeywords.PRINT, CommandKeywords.SPONSORS},
            new PrintSponsorsCommand()
        );
        registerCommand(
            new String[]{CommandKeywords.ADD, CommandKeywords.SPAWN},
            new CommandArgs[]{CommandArgs.ID, CommandArgs.X, CommandArgs.Y, CommandArgs.Z},
            new AddSpawnCommand());
        registerCommand(
            new String[]{CommandKeywords.CLEAR, CommandKeywords.SPAWNS},
            new CommandArgs[]{CommandArgs.ID},
            new ClearSpawnpointsCommand());
        registerCommand(
            new String[]{CommandKeywords.SET, CommandKeywords.CENTER},
            new CommandArgs[]{CommandArgs.ID, CommandArgs.X, CommandArgs.Y, CommandArgs.Z},
            new SetCenterLocationCommand());
        registerCommand(
            new String[]{CommandKeywords.SET, CommandKeywords.CHEST, CommandKeywords.MIDPOINT},
            new CommandArgs[]{CommandArgs.ID, CommandArgs.MIDPOINT,},
            new SetChestMidpointCommand());
        registerCommand(
            new String[]{CommandKeywords.SET, CommandKeywords.CHEST, CommandKeywords.RANGE},
            new CommandArgs[]{CommandArgs.ID, CommandArgs.RANGE},
            new SetChestRangeCommand());
        registerCommand(
            new String[]{CommandKeywords.SET, CommandKeywords.COUNTDOWN},
            new CommandArgs[]{CommandArgs.ID, CommandArgs.COUNTDOWN},
            new SetCountdownCommand());
        registerCommand(
            new String[]{CommandKeywords.SET, CommandKeywords.EXIT},
            new CommandArgs[]{CommandArgs.ID, CommandArgs.WORLDNAME, CommandArgs.X, CommandArgs.Y, CommandArgs.Z},
            new SetExitCommand());
        registerCommand(
            new String[]{CommandKeywords.SET, CommandKeywords.PLAYER_LIMIT},
            new CommandArgs[]{CommandArgs.ID, CommandArgs.PLAYER_LIMIT},
            new SetPlayerLimitCommand());
        registerCommand(
            new String[]{CommandKeywords.SET, CommandKeywords.WORLD},
            new CommandArgs[]{CommandArgs.ID, CommandArgs.WORLDNAME},
            new SetWorldCommand());
        registerCommand(
            new String[]{CommandKeywords.SET, CommandKeywords.BOUNDS},
            new CommandArgs[]{CommandArgs.ID, CommandArgs.XMIN, CommandArgs.XMAX, CommandArgs.ZMIN, CommandArgs.ZMAX},
            new SetBoundsCommand());
        registerCommand(
            new String[]{CommandKeywords.SET, CommandKeywords.DEATHMATCH, CommandKeywords.RADIUS},
            new CommandArgs[]{CommandArgs.ID, CommandArgs.DEATHMATCHRADIUS},
            new SetDeathmatchRadiusCommand());
        registerCommand(
            new String[]{CommandKeywords.SET, CommandKeywords.DEATHMATCH, CommandKeywords.TIME},
            new CommandArgs[]{CommandArgs.ID, CommandArgs.DEATHMATCHTIME},
            new SetDeathmatchTimeCommand());
        registerCommand(
            new String[]{CommandKeywords.ADD, CommandKeywords.HELD, CommandKeywords.LOOT},
            new CommandArgs[]{CommandArgs.ID, CommandArgs.WEIGHT},
            new AddHeldLootCommand());
        registerCommand(
            new String[]{CommandKeywords.READY},
            new CommandArgs[]{CommandArgs.ID},
            new ReadyGameCommand());
        registerCommand(
            new String[]{CommandKeywords.ADD, CommandKeywords.PLAYER},
            new CommandArgs[]{CommandArgs.ID, CommandArgs.PLAYERNAME},
            new AddPlayerCommand());
        registerCommand(
            new String[]{CommandKeywords.REMOVE, CommandKeywords.PLAYER},
            new CommandArgs[]{CommandArgs.ID, CommandArgs.PLAYERNAME},
            new RemovePlayerCommand());
        registerCommand(
            new String[]{CommandKeywords.ADD, CommandKeywords.SPECTATOR},
            new CommandArgs[]{CommandArgs.ID, CommandArgs.PLAYERNAME},
            new AddSpectatorCommand());
        registerCommand(
            new String[]{CommandKeywords.REMOVE, CommandKeywords.SPECTATOR},
            new CommandArgs[]{CommandArgs.ID, CommandArgs.PLAYERNAME},
            new RemoveSpectatorCommand());
        registerCommand(
            new String[]{CommandKeywords.START},
            new CommandArgs[]{CommandArgs.ID},
            new StartGameCommand());
        registerCommand(
            new String[]{CommandKeywords.STOP},
            new CommandArgs[]{CommandArgs.ID},
            new StopGameCommand());
        registerCommand(
            new String[]{CommandKeywords.FORCE, CommandKeywords.STOP},
            new CommandArgs[]{CommandArgs.ID},
            new ForceStopGameCommand());
        registerCommand(
            new String[]{CommandKeywords.FORCE, CommandKeywords.DEATHMATCH},
            new CommandArgs[]{CommandArgs.ID},
            new ForceDeathmatchCommand());
        registerCommand(
            new String[]{CommandKeywords.LOAD},
            new CommandArgs[]{CommandArgs.ID, CommandArgs.FILENAME, CommandArgs.OVERWRITE},
            new LoadCommand());
        registerCommand(
            new String[]{CommandKeywords.CHEST},
            new CommandArgs[]{CommandArgs.ID},
            new GetChestsCommand());
        registerCommand(
            new String[]{CommandKeywords.SAVE},
            new CommandArgs[]{CommandArgs.ID, CommandArgs.FILENAME},
            new SaveCommand());
        registerCommand(
            new String[]{CommandKeywords.GIVE, CommandKeywords.SPONSOR},
            new CommandArgs[]{CommandArgs.ID, CommandArgs.PLAYERNAME, CommandArgs.SPONSOR},
            new SponsorCommand()
        );
        registerCommand(
            new String[]{CommandKeywords.REFILL, CommandKeywords.CHESTS},
            new CommandArgs[]{CommandArgs.ID},
            new RefillChestsCommand()
        );
        registerCommand(
            new String[]{CommandKeywords.BACKUP},
            new CommandArgs[]{CommandArgs.ID, CommandArgs.FILENAME},
            new BackupCommand()
        );
        registerCommand(
            new String[]{CommandKeywords.RESTORE},
            new CommandArgs[]{CommandArgs.ID, CommandArgs.BACKUPNAME},
            new RestoreGameCommand()
        );
    }

    @Override
    public void onLoad() {
        if (!getDataFolder().mkdirs()) {
            Sponge.getLogger().warning("Unable to make plugin folder");
        }
    }

    @Override
    public void onEnable() {
        survivalGameMap.clear();
        Sponge.getEventManager().registerListeners(PlayerEventListener.getInstance(), this);
        getLogger().info("Sponge Survival Games Plugin Enabled");

        getCommand(CommandKeywords.SSG).setExecutor(commandExecutor);
        getCommand(CommandKeywords.SSG).setTabCompleter(tabCompleter);
    }

    @Override
    public void onDisable() {
        File backupsFolder = new File(SpongeSurvivalGamesPlugin.plugin.getDataFolder(), "Backups");

        if (!backupsFolder.isDirectory()) {
            getLogger().warn("Found file named 'Backup', but need name "
                + "for backup folders!\nFile will be deleted!");
            if (!backupsFolder.delete()) {
                getLogger().warn("Unable to delete backup file!");
            }
        }

        if (!backupsFolder.exists()) {
            if (!backupsFolder.mkdirs()) {
                getLogger().warn("Unable to make backups directory!");
            }
        }

        survivalGameMap.values().stream().filter(game -> game.getState() == SurvivalGameState.RUNNING || game.getState() == SurvivalGameState.DEATHMATCH).forEach(game -> {
            getLogger().warn("Taking emergency backup of game: " + game.getID());
            File file = new File(backupsFolder, "EBACKUP[" + game.getID() + "].yml");

            Backup backup = new Backup(game);
            YamlConfiguration config = backup.asConfig();
            try {
                config.save(file);
            } catch (IOException e) {
                getLogger().warning(ChatColor.RED + "Error encountered when saving backup!");
            }
        });
    }

    public static Logger getLogger() {
        return LOGGER;
    }
}

