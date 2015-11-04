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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.m0pt0pmatt.spongesurvivalgames.commands.CreateGameCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.ListGamesCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.game.stopped.DeleteGameCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.events.PlayerDeathEventListener;

/**
 * SpongeSurvivalGames Sponge Plugin
 */
public class BukkitSurvivalGamesPlugin  extends JavaPlugin {

    public static Map<String, SurvivalGame> survivalGameMap;
    public static BukkitSurvivalGamesPlugin plugin;

    public BukkitSurvivalGamesPlugin() {
        BukkitSurvivalGamesPlugin.plugin = this;
        BukkitSurvivalGamesPlugin.survivalGameMap = new HashMap<>();
    }

    public static Set<Player> getPlayers(Set<UUID> playerUUIDs) {
        return playerUUIDs.stream()
                .map(uuid -> Bukkit.getServer().getPlayer(uuid))
                .filter( (player) -> player != null)
                .collect(Collectors.toSet());
    }

    public Map<String, SurvivalGame> getSurvivalGameMap() {
        return survivalGameMap;
    }

    @Override
    public void onEnable() {
        //registerCommands();
        survivalGameMap.clear();
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerDeathEventListener(), this);
        Bukkit.getLogger().info("Sponge Survival Games Plugin Enabled");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args){

        Map<String, String> arguments = new HashMap<>();

        //ssg create [id]
        //ssg list
        //ssg delete [id]
        //ssg print center [id]
        //ssg print chest midpoint [id]
        //ssg print chest range [id]
        //ssg print countdown [id]
        //ssg print exit [id]
        //ssg print player-limit [id]
        //ssg print spawns [id]
        //ssg print world [id]
        //ssg add spawn [id] [x] [y] [z]
        //ssg clear spawns [id]
        //ssg set center [id] [x] [y] [z]
        //ssg set chest midpoint [id] [midpoint]
        //ssg set chest range [id] [range]
        //ssg set countdown [id] [countdown]
        //ssg set exit [id] [worldname] [x] [y] [z]
        //ssg set player-limit [id] [player-limit]
        //ssg set world [id] [worldname]
        //ssg ready [id]
        //ssg add player [id]
        //ssg remove player [id]
        //ssg start [id]
        //ssg stop [id]
        //ssg force-stop [id]
        //ssg load [id] [filename]
        //ssg save [id] [filename]

        if (!cmd.getName().equalsIgnoreCase("ssg")){
            return true;
        }
        if (args.length < 1){
            return false;
        }

        if (args[0].equalsIgnoreCase("create")){
            //ssg create [id]
            if (args.length == 1) return false;
            if (args.length > 2) return false;
            arguments.put("id", args[1]);
            return new CreateGameCommand(arguments).execute(sender);
        }
        else if (args[0].equalsIgnoreCase("list")){
            //ssg list
            if (args.length > 1) return false;
            return new ListGamesCommand(arguments).execute(sender);
        }
        else if (args[0].equalsIgnoreCase("delete")){
            //ssg delete [id]
            if (args.length == 1) return false;
            if (args.length > 2) return false;
            arguments.put("id", args[1]);
            return new DeleteGameCommand(arguments).execute(sender);
        }
        else if (args[0].equalsIgnoreCase("print")){

            if (args.length < 3) return false;

            arguments.put("id", args[2]);

            if (args[1].equalsIgnoreCase("center")){
                //ssg print center [id]
            }
            else if (args[1].equalsIgnoreCase("chest")){
                //ssg print chest midpoint [id]
                //ssg print chest range [id]
            }
            else if (args[1].equalsIgnoreCase("countdown")){
                //ssg print countdown [id]
            }
            else if (args[1].equalsIgnoreCase("exit")){
                //ssg print exit [id]
            }
            else if (args[1].equalsIgnoreCase("player-limit")){
                //ssg print player-limit [id]
            }
            else if (args[1].equalsIgnoreCase("spawns")){
                //ssg print spawns [id]
            }
            else if (args[1].equalsIgnoreCase("world")){
                //ssg print world [id]
            }
            else{
                return false;
            }
        }
        else if (args[0].equalsIgnoreCase("add")){
            //ssg add spawn [id] [x] [y] [z]
            //ssg add player [id]
        }
        else if (args[0].equalsIgnoreCase("clear")){
            //ssg clear spawns [id]
        }
        else if (args[0].equalsIgnoreCase("set")){
            //ssg set center [id] [x] [y] [z]
            //ssg set chest midpoint [id] [midpoint]
            //ssg set chest range [id] [range]
            //ssg set countdown [id] [countdown]
            //ssg set exit [id] [worldname] [x] [y] [z]
            //ssg set player-limit [id] [player-limit]
            //ssg set world [id] [worldname]
        }
        else if (args[0].equalsIgnoreCase("remove")){
            //ssg remove player [id]
        }
        else if (args[0].equalsIgnoreCase("ready")){
            //ssg ready [id]
        }
        else if (args[0].equalsIgnoreCase("start")){
            //ssg start [id]
        }
        else if (args[0].equalsIgnoreCase("stop")){
            //ssg stop [id]
        }
        else if (args[0].equalsIgnoreCase("force-stop")){
            //ssg force-stop [id]
        }
        else if (args[0].equalsIgnoreCase("load")){
            //ssg load [id] [filename]
        }
        else if (args[0].equalsIgnoreCase("save")){
            //ssg save [id] [filename]
        }

        return false;
    }

    /*private void registerCommands() {

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
    }*/
    /*
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
    */

}

