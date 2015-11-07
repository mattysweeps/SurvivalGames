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

import io.github.m0pt0pmatt.spongesurvivalgames.BukkitSurvivalGamesPlugin;
import io.github.m0pt0pmatt.spongesurvivalgames.commands.CommandArgs;
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.NegativeNumberException;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Map;

public class SetDeathmatchTimeCommand extends StoppedCommand {
    @Override
    public boolean execute(CommandSender sender, Map<String, String> arguments) {

        if (!super.execute(sender, arguments)) {
            return false;
        }

        if (!arguments.containsKey(CommandArgs.DEATHMATCHTIME)) {
            Bukkit.getLogger().warning("No deathmatch time specified");
            return false;
        }
        String deathmatchTimeString = arguments.get(CommandArgs.DEATHMATCHTIME);

        int deathmatchTime;
        try {
            deathmatchTime = Integer.parseInt(deathmatchTimeString);
        } catch (NumberFormatException e) {
            Bukkit.getLogger().warning("Unable to convert String to Integer");
            return false;
        }

        try {
            BukkitSurvivalGamesPlugin.survivalGameMap.get(id).setDeathmatchTime(deathmatchTime);
        } catch (NegativeNumberException e) {
            Bukkit.getLogger().warning("Negative deathmatch times are not valid");
            return false;
        }

        Bukkit.getLogger().info("Game \"" + id + "\" now has a deathmatch time of \"" + deathmatchTime + "\" seconds.");
        return true;
    }
}