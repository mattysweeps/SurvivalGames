/*
 *  QuestManager: An RPG plugin for the Bukkit API.
 *  Copyright (C) 2015-2016 Github Contributors
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.github.m0pt0pmatt.spongesurvivalgames;

import org.spongepowered.api.command.spec.CommandExecutor;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.github.m0pt0pmatt.spongesurvivalgames.command.CommandArgs;
import io.github.m0pt0pmatt.spongesurvivalgames.command.CommandKeywords;
import io.github.m0pt0pmatt.spongesurvivalgames.command.CreateGameCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.ListGamesCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.RestoreGameCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.game.AddSpectatorCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.game.BackupCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.game.RemoveSpectatorCommand;
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
import io.github.m0pt0pmatt.spongesurvivalgames.util.LoadedTrie;

public class CommandManager {

    private static final LoadedTrie<String, CommandExecutor> COMMAND_TRIE = new LoadedTrie<>();
    private static final Map<CommandExecutor, List<CommandArgs>> COMMAND_ARGS = new HashMap<>();
    static {
        registerCommands();
    }

    private static void registerCommand(String[] words, CommandArgs[] args, CommandExecutor command) {
        COMMAND_TRIE.add(words, command);
        List<CommandArgs> list = new LinkedList<>();
        Collections.addAll(list, args);
        COMMAND_ARGS.put(command, list);
    }

    private static void registerCommand(String[] words, CommandExecutor command) {
        COMMAND_TRIE.add(words, command);
    }

    private static void registerCommands() {
        registerCommand(
                new String[]{CommandKeywords.CREATE},
                new CommandArgs[]{CommandArgs.ID},
                CreateGameCommand.get());
        registerCommand(
                new String[]{CommandKeywords.LIST},
                ListGamesCommand.get());
        registerCommand(
                new String[]{CommandKeywords.DELETE},
                new CommandArgs[]{CommandArgs.ID},
                DeleteGameCommand.get());
        registerCommand(
                new String[]{CommandKeywords.PRINT, CommandKeywords.CENTER},
                new CommandArgs[]{CommandArgs.ID},
                PrintCenterCommand.get());
        registerCommand(
                new String[]{CommandKeywords.PRINT, CommandKeywords.CHEST, CommandKeywords.MIDPOINT},
                new CommandArgs[]{CommandArgs.ID},
                PrintChestMidpointCommand.get());
        registerCommand(
                new String[]{CommandKeywords.PRINT, CommandKeywords.CHEST, CommandKeywords.RANGE},
                new CommandArgs[]{CommandArgs.ID},
                PrintChestRangeCommand.get());
        registerCommand(
                new String[]{CommandKeywords.PRINT, CommandKeywords.COUNTDOWN},
                new CommandArgs[]{CommandArgs.ID},
                PrintCountdownCommand.get());
        registerCommand(
                new String[]{CommandKeywords.PRINT, CommandKeywords.EXIT},
                new CommandArgs[]{CommandArgs.ID},
                PrintExitCommand.get());
        registerCommand(
                new String[]{CommandKeywords.PRINT, CommandKeywords.PLAYER_LIMIT},
                new CommandArgs[]{CommandArgs.ID},
                PrintPlayerLimitCommand.get());
        registerCommand(
                new String[]{CommandKeywords.PRINT, CommandKeywords.SPAWNS},
                new CommandArgs[]{CommandArgs.ID},
                PrintSpawnsCommand.get());
        registerCommand(
                new String[]{CommandKeywords.PRINT, CommandKeywords.WORLD},
                new CommandArgs[]{CommandArgs.ID},
                PrintWorldCommand.get());
        registerCommand(
                new String[]{CommandKeywords.PRINT, CommandKeywords.BOUNDS},
                new CommandArgs[]{CommandArgs.ID},
                PrintBoundsCommand.get());
        registerCommand(
                new String[]{CommandKeywords.PRINT, CommandKeywords.LOOT},
                new CommandArgs[]{CommandArgs.ID},
                PrintLootCommand.get());
        registerCommand(
                new String[]{CommandKeywords.PRINT, CommandKeywords.DEATHMATCH, CommandKeywords.RADIUS},
                new CommandArgs[]{CommandArgs.ID},
                PrintDeathmatchRadiusCommand.get());
        registerCommand(
                new String[]{CommandKeywords.PRINT, CommandKeywords.DEATHMATCH, CommandKeywords.TIME},
                new CommandArgs[]{CommandArgs.ID},
                PrintDeathmatchTimeCommand.get());
        registerCommand(
                new String[]{CommandKeywords.PRINT, CommandKeywords.SPONSORS},
                PrintSponsorsCommand.get()
        );
        registerCommand(
                new String[]{CommandKeywords.ADD, CommandKeywords.SPAWN},
                new CommandArgs[]{CommandArgs.ID, CommandArgs.X, CommandArgs.Y, CommandArgs.Z},
                AddSpawnCommand.get());
        registerCommand(
                new String[]{CommandKeywords.CLEAR, CommandKeywords.SPAWNS},
                new CommandArgs[]{CommandArgs.ID},
                ClearSpawnpointsCommand.get());
        registerCommand(
                new String[]{CommandKeywords.SET, CommandKeywords.CENTER},
                new CommandArgs[]{CommandArgs.ID, CommandArgs.X, CommandArgs.Y, CommandArgs.Z},
                SetCenterLocationCommand.get());
        registerCommand(
                new String[]{CommandKeywords.SET, CommandKeywords.CHEST, CommandKeywords.MIDPOINT},
                new CommandArgs[]{CommandArgs.ID, CommandArgs.MIDPOINT,},
                SetChestMidpointCommand.get());
        registerCommand(
                new String[]{CommandKeywords.SET, CommandKeywords.CHEST, CommandKeywords.RANGE},
                new CommandArgs[]{CommandArgs.ID, CommandArgs.RANGE},
                SetChestRangeCommand.get());
        registerCommand(
                new String[]{CommandKeywords.SET, CommandKeywords.COUNTDOWN},
                new CommandArgs[]{CommandArgs.ID, CommandArgs.COUNTDOWN},
                SetCountdownCommand.get());
        registerCommand(
                new String[]{CommandKeywords.SET, CommandKeywords.EXIT},
                new CommandArgs[]{CommandArgs.ID, CommandArgs.WORLDNAME, CommandArgs.X, CommandArgs.Y, CommandArgs.Z},
                SetExitCommand.get());
        registerCommand(
                new String[]{CommandKeywords.SET, CommandKeywords.PLAYER_LIMIT},
                new CommandArgs[]{CommandArgs.ID, CommandArgs.PLAYER_LIMIT},
                SetPlayerLimitCommand.get());
        registerCommand(
                new String[]{CommandKeywords.SET, CommandKeywords.WORLD},
                new CommandArgs[]{CommandArgs.ID, CommandArgs.WORLDNAME},
                SetWorldCommand.get());
        registerCommand(
                new String[]{CommandKeywords.SET, CommandKeywords.BOUNDS},
                new CommandArgs[]{CommandArgs.ID, CommandArgs.XMIN, CommandArgs.XMAX, CommandArgs.ZMIN, CommandArgs.ZMAX},
                SetBoundsCommand.get());
        registerCommand(
                new String[]{CommandKeywords.SET, CommandKeywords.DEATHMATCH, CommandKeywords.RADIUS},
                new CommandArgs[]{CommandArgs.ID, CommandArgs.DEATHMATCHRADIUS},
                SetDeathmatchRadiusCommand.get());
        registerCommand(
                new String[]{CommandKeywords.SET, CommandKeywords.DEATHMATCH, CommandKeywords.TIME},
                new CommandArgs[]{CommandArgs.ID, CommandArgs.DEATHMATCHTIME},
                SetDeathmatchTimeCommand.get());
        registerCommand(
                new String[]{CommandKeywords.ADD, CommandKeywords.HELD, CommandKeywords.LOOT},
                new CommandArgs[]{CommandArgs.ID, CommandArgs.WEIGHT},
                AddHeldLootCommand.get());
        registerCommand(
                new String[]{CommandKeywords.READY},
                new CommandArgs[]{CommandArgs.ID},
                ReadyGameCommand.get());
        registerCommand(
                new String[]{CommandKeywords.ADD, CommandKeywords.PLAYER},
                new CommandArgs[]{CommandArgs.ID, CommandArgs.PLAYERNAME},
                AddPlayerCommand.get());
        registerCommand(
                new String[]{CommandKeywords.REMOVE, CommandKeywords.PLAYER},
                new CommandArgs[]{CommandArgs.ID, CommandArgs.PLAYERNAME},
                RemovePlayerCommand.get());
        registerCommand(
                new String[]{CommandKeywords.ADD, CommandKeywords.SPECTATOR},
                new CommandArgs[]{CommandArgs.ID, CommandArgs.PLAYERNAME},
                AddSpectatorCommand.get());
        registerCommand(
                new String[]{CommandKeywords.REMOVE, CommandKeywords.SPECTATOR},
                new CommandArgs[]{CommandArgs.ID, CommandArgs.PLAYERNAME},
                RemoveSpectatorCommand.get());
        registerCommand(
                new String[]{CommandKeywords.START},
                new CommandArgs[]{CommandArgs.ID},
                StartGameCommand.get());
        registerCommand(
                new String[]{CommandKeywords.STOP},
                new CommandArgs[]{CommandArgs.ID},
                StopGameCommand.get());
        registerCommand(
                new String[]{CommandKeywords.FORCE, CommandKeywords.STOP},
                new CommandArgs[]{CommandArgs.ID},
                ForceStopGameCommand.get());
        registerCommand(
                new String[]{CommandKeywords.FORCE, CommandKeywords.DEATHMATCH},
                new CommandArgs[]{CommandArgs.ID},
                ForceDeathmatchCommand.get());
        registerCommand(
                new String[]{CommandKeywords.CHEST},
                new CommandArgs[]{CommandArgs.ID},
                GetChestsCommand.get());
        registerCommand(
                new String[]{CommandKeywords.GIVE, CommandKeywords.SPONSOR},
                new CommandArgs[]{CommandArgs.ID, CommandArgs.PLAYERNAME, CommandArgs.SPONSOR},
                SponsorCommand.get()
        );
        registerCommand(
                new String[]{CommandKeywords.REFILL, CommandKeywords.CHESTS},
                new CommandArgs[]{CommandArgs.ID},
                RefillChestsCommand.get()
        );
        registerCommand(
                new String[]{CommandKeywords.BACKUP},
                new CommandArgs[]{CommandArgs.ID, CommandArgs.FILENAME},
                BackupCommand.get()
        );
        registerCommand(
                new String[]{CommandKeywords.RESTORE},
                new CommandArgs[]{CommandArgs.ID, CommandArgs.BACKUPNAME},
                RestoreGameCommand.get()
        );
    }
}
