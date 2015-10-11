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

import io.github.m0pt0pmatt.spongesurvivalgames.SpongeSurvivalGamesPlugin;
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.NoWorldException;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;

import java.util.Optional;

/**
 * Command to set the exit location for a game
 * The exit location is where players go when they leave the game (by quitting, dying, or winning)
 */
public class SetExitCommand extends StoppedCommand {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if (!super.execute(src, args).equals(CommandResult.success())) {
            return CommandResult.empty();
        }

        Optional<String> worldName = args.getOne("worldName");
        if (!worldName.isPresent()) {
            SpongeSurvivalGamesPlugin.logger.error("World name was not present.");
            return CommandResult.empty();
        }

        Optional<Integer> x = args.getOne("x");
        Optional<Integer> y = args.getOne("y");
        Optional<Integer> z = args.getOne("z");
        if (!x.isPresent() || !y.isPresent() || !z.isPresent()) {
            SpongeSurvivalGamesPlugin.logger.error("Missing one or more axis for coordinates.");
            return CommandResult.empty();
        }

        try {
            SpongeSurvivalGamesPlugin.survivalGameMap.get(id).setExitLocation(worldName.get(), x.get(), y.get(), z.get());
        } catch (NoWorldException e) {
            SpongeSurvivalGamesPlugin.logger.error("No such world \"" + worldName.get() + "\".");
            return CommandResult.empty();
        }

        SpongeSurvivalGamesPlugin.logger.info("Exit location for game \"" + id + "\" set to world: " + worldName + " (" + x.get() + "," + y.get() + "," + z.get() + ").");
        return CommandResult.success();
    }
}
