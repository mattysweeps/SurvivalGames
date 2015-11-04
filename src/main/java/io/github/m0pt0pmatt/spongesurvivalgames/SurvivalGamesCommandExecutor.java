package io.github.m0pt0pmatt.spongesurvivalgames;

import io.github.m0pt0pmatt.spongesurvivalgames.commands.CreateGameCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.ListGamesCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.game.stopped.DeleteGameCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Matt on 11/4/2015.
 */
public class SurvivalGamesCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
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

        if (!command.getName().equalsIgnoreCase("ssg")) {
            return true;
        }
        if (strings.length < 1) {
            return false;
        }

        if (strings[0].equalsIgnoreCase("create")) {
            //ssg create [id]
            if (strings.length == 1) return false;
            if (strings.length > 2) return false;
            arguments.put("id", strings[1]);
            return new CreateGameCommand(arguments).execute(commandSender);
        } else if (strings[0].equalsIgnoreCase("list")) {
            //ssg list
            if (strings.length > 1) return false;
            return new ListGamesCommand(arguments).execute(commandSender);
        } else if (strings[0].equalsIgnoreCase("delete")) {
            //ssg delete [id]
            if (strings.length == 1) return false;
            if (strings.length > 2) return false;
            arguments.put("id", strings[1]);
            return new DeleteGameCommand(arguments).execute(commandSender);
        } else if (strings[0].equalsIgnoreCase("print")) {

            if (strings.length < 3) return false;

            arguments.put("id", strings[2]);

            if (strings[1].equalsIgnoreCase("center")) {
                //ssg print center [id]
            } else if (strings[1].equalsIgnoreCase("chest")) {
                //ssg print chest midpoint [id]
                //ssg print chest range [id]
            } else if (strings[1].equalsIgnoreCase("countdown")) {
                //ssg print countdown [id]
            } else if (strings[1].equalsIgnoreCase("exit")) {
                //ssg print exit [id]
            } else if (strings[1].equalsIgnoreCase("player-limit")) {
                //ssg print player-limit [id]
            } else if (strings[1].equalsIgnoreCase("spawns")) {
                //ssg print spawns [id]
            } else if (strings[1].equalsIgnoreCase("world")) {
                //ssg print world [id]
            } else {
                return false;
            }
        } else if (strings[0].equalsIgnoreCase("add")) {
            //ssg add spawn [id] [x] [y] [z]
            //ssg add player [id]
        } else if (strings[0].equalsIgnoreCase("clear")) {
            //ssg clear spawns [id]
        } else if (strings[0].equalsIgnoreCase("set")) {
            //ssg set center [id] [x] [y] [z]
            //ssg set chest midpoint [id] [midpoint]
            //ssg set chest range [id] [range]
            //ssg set countdown [id] [countdown]
            //ssg set exit [id] [worldname] [x] [y] [z]
            //ssg set player-limit [id] [player-limit]
            //ssg set world [id] [worldname]
        } else if (strings[0].equalsIgnoreCase("remove")) {
            //ssg remove player [id]
        } else if (strings[0].equalsIgnoreCase("ready")) {
            //ssg ready [id]
        } else if (strings[0].equalsIgnoreCase("start")) {
            //ssg start [id]
        } else if (strings[0].equalsIgnoreCase("stop")) {
            //ssg stop [id]
        } else if (strings[0].equalsIgnoreCase("force-stop")) {
            //ssg force-stop [id]
        } else if (strings[0].equalsIgnoreCase("load")) {
            //ssg load [id] [filename]
        } else if (strings[0].equalsIgnoreCase("save")) {
            //ssg save [id] [filename]
        }

        return false;
    }
}
