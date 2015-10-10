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
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.WorldNotSetException;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;

import java.util.Optional;

/**
 * Command to add a spawn location to a game
 */
public class AddSpawnLocationCommand extends StoppedCommand {

    public AddSpawnLocationCommand(SpongeSurvivalGamesPlugin plugin) {
        super(plugin);
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if (!super.execute(src, args).equals(CommandResult.success())) {
            return CommandResult.empty();
        }

        Optional<Integer> x = args.getOne("x");
        Optional<Integer> y = args.getOne("y");
        Optional<Integer> z = args.getOne("z");
        if (!x.isPresent() || !y.isPresent() || !z.isPresent()) {
            plugin.getLogger().error("Missing one or more axis for coordinates.");
            return CommandResult.empty();
        }

        try {
            plugin.getSurvivalGameMap().get(id).addSpawnLocation(x.get(), y.get(), z.get());
        } catch (WorldNotSetException e) {
            plugin.getLogger().error("No world set. Assign the world first.");
            return CommandResult.empty();
        } catch (NoWorldException e) {
            plugin.getLogger().error("World does not exist.");
            return CommandResult.empty();
        }
        plugin.getLogger().info("Spawn location added for game \"" + id + "\".");

        return CommandResult.success();
    }
}
