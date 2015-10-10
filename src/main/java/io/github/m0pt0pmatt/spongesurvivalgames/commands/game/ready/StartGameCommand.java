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

package io.github.m0pt0pmatt.spongesurvivalgames.commands.game.ready;

import java.util.Optional;

import io.github.m0pt0pmatt.spongesurvivalgames.SpongeSurvivalGamesPlugin;
import io.github.m0pt0pmatt.spongesurvivalgames.SurvivalGameState;
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.*;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;

/**
 * Command to set a game to the RUNNING state (start the game)
 */
public class StartGameCommand extends ReadyCommand {

    public StartGameCommand(SpongeSurvivalGamesPlugin plugin) {
        super(plugin);
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        Optional<String> id = args.getOne("id");
        if (!id.isPresent()) {
            plugin.getLogger().error("Survival Game ID is not present.");
            return CommandResult.empty();
        }

        if (!plugin.getSurvivalGameMap().containsKey(id.get())) {
            plugin.getLogger().error("No Survival Game has specified ID \"" + id.get() + "\".");
            return CommandResult.empty();
        }

        if (!plugin.getSurvivalGameMap().get(id.get()).getState().equals(SurvivalGameState.READY)) {
            plugin.getLogger().error("Survival Game \"" + id.get() + "\" must be READY before it can be set to RUNNING.");
            return CommandResult.empty();
        }

        try {
            plugin.getSurvivalGameMap().get(id.get()).start();
        } catch (NotEnoughSpawnPointsException e) {
            plugin.getLogger().error("Survival Game \"" + id.get() + "\" does not have enough spawn points.");
            return CommandResult.empty();
        } catch (NoExitLocationException e) {
            plugin.getLogger().error("Survival Game \"" + id.get() + "\" does not have an exit location.");
            return CommandResult.empty();
        } catch (WorldNotSetException e) {
            plugin.getLogger().error("Survival Game \"" + id.get() + "\" does not have a world assigned to it.");
            return CommandResult.empty();
        } catch (NoWorldException e) {
            plugin.getLogger().error("World does not exist.");
            return CommandResult.empty();
        } catch (TaskException e) {
            plugin.getLogger().error(e.getMessage());
            return CommandResult.empty();
        }

        plugin.getLogger().info("Survival Game \"" + id.get() + "\" is now RUNNING.");
        return CommandResult.success();
    }
}
