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

}

