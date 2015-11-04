package io.github.m0pt0pmatt.spongesurvivalgames.commands.game.stopped;

import io.github.m0pt0pmatt.spongesurvivalgames.commands.CommandKeywords;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.SurvivalGamesCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Map;

/**
 * Created by Matt on 11/4/2015.
 */
public class SetBoundsCommand extends StoppedCommand {

    @Override
    public boolean execute(CommandSender sender, Map<String, String> arguments) {

        if (!super.execute(sender, arguments)) {
            return false;
        }

        if (
                !arguments.containsKey(CommandKeywords.XMIN) ||
                !arguments.containsKey(CommandKeywords.XMAX) ||
                !arguments.containsKey(CommandKeywords.YMIN) ||
                !arguments.containsKey(CommandKeywords.YMAX) ||
                !arguments.containsKey(CommandKeywords.ZMIN) ||
                !arguments.containsKey(CommandKeywords.ZMAX)
                ) {
            Bukkit.getLogger().warning("Missing one or more bounds.");
            return false;
        }

        String xString = arguments.get(CommandKeywords.X);
        String yString = arguments.get(CommandKeywords.Y);
        String zString = arguments.get(CommandKeywords.Z);
        String xString = arguments.get(CommandKeywords.X);
        String yString = arguments.get(CommandKeywords.Y);
        String zString = arguments.get(CommandKeywords.Z);

        //TODO: Add sanity check
        int x = Integer.parseInt(xString);
        int y = Integer.parseInt(yString);
        int z = Integer.parseInt(zString);
        int x = Integer.parseInt(xString);
        int y = Integer.parseInt(yString);
        int z = Integer.parseInt(zString);

        

        return false;
    }
}
