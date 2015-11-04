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

import java.util.Map;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import io.github.m0pt0pmatt.spongesurvivalgames.BukkitSurvivalGamesPlugin;
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.NoWorldException;

/**
 * Command to set the exit location for a game
 * The exit location is where players go when they leave the game (by quitting, dying, or winning)
 */
public class SetExitCommand extends StoppedCommand {

    public SetExitCommand(Map<String, String> arguments){
        super(arguments);
    }

    @Override
    public boolean execute(CommandSender sender){

        if (!super.execute(sender)) {
            return false;
        }

        Optional<String> worldName = getArgument("worldName");
        if (!worldName.isPresent()) {
            Bukkit.getLogger().warning("World name was not present.");
            return false;
        }

        Optional<String> xString = getArgument("x");
        Optional<String> yString = getArgument("y");
        Optional<String> zString = getArgument("z");
        if (!xString.isPresent() || !yString.isPresent() || !zString.isPresent()) {
            Bukkit.getLogger().warning("Missing one or more axis for coordinates.");
            return false;
        }

        //TODO: Add sanity check
        int x = Integer.parseInt(xString.get());
        int y = Integer.parseInt(yString.get());
        int z = Integer.parseInt(zString.get());

        try {
            BukkitSurvivalGamesPlugin.survivalGameMap.get(id).setExitLocation(worldName.get(), x, y, z);
        } catch (NoWorldException e) {
            Bukkit.getLogger().warning("No such world \"" + worldName.get() + "\".");
            return false;
        }

        Bukkit.getLogger().info("Exit location for game \"" + id + "\" set to world: " + worldName + " (" + x + "," + y + "," + z + ").");
        return true;
    }
}
