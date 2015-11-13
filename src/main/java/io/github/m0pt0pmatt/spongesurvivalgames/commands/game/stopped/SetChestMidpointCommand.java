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
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.NegativeNumberException;
import org.bukkit.command.CommandSender;

import java.util.Map;

/**
 * Command to set the chest midpoint value for a game
 */
public class SetChestMidpointCommand extends StoppedCommand {

    @Override
    public boolean execute(CommandSender sender, Map<CommandArgs, String> arguments) {

        if (!super.execute(sender, arguments)) {
            return false;
        }

        if (!arguments.containsKey(CommandArgs.MIDPOINT)) {
            sender.sendMessage("Chest midpoint was not present.");
            return false;
        }

        String chestMidpoint = arguments.get(CommandArgs.MIDPOINT);

        Double midpoint;
        try {
            midpoint = Double.parseDouble(chestMidpoint);
        } catch (NumberFormatException e) {
            sender.sendMessage("Unable to convert String to Double");
            return false;
        }

        try {
            game.setChestMidpoint(midpoint);
        } catch (NegativeNumberException e) {
            sender.sendMessage("Chest midpoint cannot be negative.");
            return false;
        }

        sender.sendMessage("Chest midpoint for game \"" + game.getID() + "\" set to " + midpoint + ".");
        return true;
    }
}
