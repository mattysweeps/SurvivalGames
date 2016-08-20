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
}
