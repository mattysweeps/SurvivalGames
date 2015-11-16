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

package io.github.m0pt0pmatt.spongesurvivalgames.commands.game.stopped;

import io.github.m0pt0pmatt.spongesurvivalgames.commands.CommandArgs;
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.NoWorldException;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * Command to set the exit location for a game
 * The exit location is where players go when they leave the game (by quitting, dying, or winning)
 */
public class SetExitCommand extends StoppedCommand {

    @Override
    public boolean execute(CommandSender sender, Map<CommandArgs, String> arguments) {

        if (!super.execute(sender, arguments)) {
            return false;
        }

        String worldName;
        if (!arguments.containsKey(CommandArgs.WORLDNAME)) {
            if (sender instanceof Player) {
                worldName = ((Player) sender).getWorld().getName();
            } else {
                sender.sendMessage("World name was not present.");
                return false;
            }
        } else {
            worldName = arguments.get(CommandArgs.WORLDNAME);
        }

        String xString;
        String yString;
        String zString;

        if (!arguments.containsKey(CommandArgs.X) || !arguments.containsKey(CommandArgs.X) || !arguments.containsKey(CommandArgs.X)) {
            if (sender instanceof Player) {
                Block block = ((Player) sender).getLocation().getBlock();
                xString = String.valueOf(block.getX());
                yString = String.valueOf(block.getY());
                zString = String.valueOf(block.getZ());
            } else {
                sender.sendMessage("Missing coordinates");
                return false;
            }
        } else if (!arguments.containsKey(CommandArgs.X) || !arguments.containsKey(CommandArgs.X) || !arguments.containsKey(CommandArgs.X)) {
            sender.sendMessage("Missing one or more axis for coordinates.");
            return false;
        } else {
            xString = arguments.get(CommandArgs.X);
            yString = arguments.get(CommandArgs.Y);
            zString = arguments.get(CommandArgs.Z);
        }

        int x, y, z;
        try {
            x = Integer.parseInt(xString);
            y = Integer.parseInt(yString);
            z = Integer.parseInt(zString);
        } catch (NumberFormatException e) {
            sender.sendMessage("Unable to convert from String to Integer");
            return false;
        }

        try {
            game.setExitLocation(worldName, x, y, z);
        } catch (NoWorldException e) {
            sender.sendMessage("No such world \"" + worldName + "\".");
            return false;
        }

        sender.sendMessage("Exit location for game \"" + game.getID() + "\" set to world: " + worldName + " (" + x + "," + y + "," + z + ").");
        return true;
    }
}
