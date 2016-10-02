/*
 * This file is part of SurvivalGames, licensed under the MIT License (MIT).
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
package io.github.m0pt0pmatt.survivalgames.command.executor.state;

import static com.google.common.base.Preconditions.checkNotNull;
import static io.github.m0pt0pmatt.survivalgames.Util.getOrThrow;
import static io.github.m0pt0pmatt.survivalgames.Util.sendSuccess;

import io.github.m0pt0pmatt.survivalgames.command.CommandKeys;
import io.github.m0pt0pmatt.survivalgames.command.element.SurvivalGameCommandElement;
import io.github.m0pt0pmatt.survivalgames.command.executor.BaseCommand;
import io.github.m0pt0pmatt.survivalgames.command.executor.LeafCommand;
import io.github.m0pt0pmatt.survivalgames.command.executor.SurvivalGamesCommand;
import io.github.m0pt0pmatt.survivalgames.game.SurvivalGame;
import io.github.m0pt0pmatt.survivalgames.game.SurvivalGameState;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Collections;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

class AbstractStateCommand extends LeafCommand {

    private final Set<SurvivalGameState> states;
    private final Consumer<SurvivalGame> action;

    AbstractStateCommand(
            SurvivalGamesCommand parentCommand,
            String name,
            Set<SurvivalGameState> states,
            Consumer<SurvivalGame> action) {
        super(parentCommand, name, SurvivalGameCommandElement.getInstance());
        this.states = checkNotNull(states, "states");
        this.action = checkNotNull(action, "action");
    }

    @Nonnull
    @Override
    public CommandResult execute(@Nonnull CommandSource src, @Nonnull CommandContext args) throws CommandException {

        SurvivalGame survivalGame = (SurvivalGame) getOrThrow(args, CommandKeys.SURVIVAL_GAME);
        SurvivalGameState oldState = survivalGame.getState();

        if (!states.contains(oldState)) {
            throw new CommandException(Text.of("Must be in one of the following states: ")
                    .concat(Text.joinWith(Text.of(':'), states.stream().map(Text::of).collect(Collectors.toList()))));
        }

        try {
            action.accept(survivalGame);
        } catch (RuntimeException e) {
            throw new CommandException(Text.of("Error while switching states: " + e.getMessage()), e);
        }

        sendSuccess(src, Text.of("State changed from ", TextColors.BLUE, oldState, TextColors.BLUE,
                " to ", TextColors.BLUE, survivalGame.getState()));
        return CommandResult.success();
    }
}
