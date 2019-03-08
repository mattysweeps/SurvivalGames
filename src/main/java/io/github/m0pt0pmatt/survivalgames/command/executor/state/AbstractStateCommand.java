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
import static io.github.m0pt0pmatt.survivalgames.Util.*;

import io.github.m0pt0pmatt.survivalgames.SurvivalGamesPlugin;
import io.github.m0pt0pmatt.survivalgames.command.CommandKeys;
import io.github.m0pt0pmatt.survivalgames.command.element.SurvivalGameCommandElement;
import io.github.m0pt0pmatt.survivalgames.command.executor.LeafCommand;
import io.github.m0pt0pmatt.survivalgames.command.executor.SurvivalGamesCommand;
import io.github.m0pt0pmatt.survivalgames.game.SurvivalGame;
import io.github.m0pt0pmatt.survivalgames.game.SurvivalGameState;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;

import io.github.m0pt0pmatt.survivalgames.thread.BlankProgressable;
import io.github.m0pt0pmatt.survivalgames.thread.DotProgressable;
import io.github.m0pt0pmatt.survivalgames.thread.ProgressBuilder;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

class AbstractStateCommand extends LeafCommand {

    private final Set<SurvivalGameState> states;
    private final SurvivalGameState targetState;
    private final Consumer<SurvivalGame> action;

    AbstractStateCommand(
            SurvivalGamesCommand parentCommand,
            String name,
            Set<SurvivalGameState> states,
            SurvivalGameState targetState,
            Consumer<SurvivalGame> action) {
        super(parentCommand, name, SurvivalGameCommandElement.getInstance());
        this.states = checkNotNull(states, "states");
        this.targetState = checkNotNull(targetState, "targetState");
        this.action = checkNotNull(action, "action");
    }

    @Nonnull
    @Override
    public CommandResult execute(@Nonnull CommandSource src, @Nonnull CommandContext args)
            throws CommandException {

        SurvivalGame survivalGame = (SurvivalGame) getOrThrow(args, CommandKeys.SURVIVAL_GAME);
        SurvivalGameState oldState = survivalGame.getState();

        if (!states.contains(oldState)) {
            throw new CommandException(
                    Text.of("Must be in one of the following states: ")
                            .concat(
                                    Text.joinWith(
                                            Text.of(':'),
                                            states.stream()
                                                    .map(Text::of)
                                                    .collect(Collectors.toList()))));
        }

        TemporalAmount timeout = Duration.of(600, ChronoUnit.SECONDS);

        ProgressBuilder.builder(src, SurvivalGamesPlugin.SYNC_EXECUTOR, SurvivalGamesPlugin.ASYNC_EXECUTOR)
                .runSync(new DotProgressable(() -> {
                    try {
                        action.accept(survivalGame);
                    } catch (RuntimeException e) {
                        throw new RuntimeException("Error while switching states: " + e.getMessage(), e);
                    }
                }), formatTargetState(survivalGame), timeout)
                .runSync(new BlankProgressable(() -> {

                    if (survivalGame.getState() == targetState) {
                        sendSuccess(
                                src,
                                Text.of(
                                        "State changed from ",
                                        TextColors.BLUE,
                                        oldState,
                                        TextColors.BLUE,
                                        " to ",
                                        TextColors.BLUE,
                                        survivalGame.getState()));
                    } else {
                        sendError(src,
                                "Error changing state from " +
                                oldState +
                                " to ",
                                survivalGame.getState());
                    }


                }), "", timeout)
                .start();

        return CommandResult.success();
    }

    private String formatTargetState(SurvivalGame survivalGame) {
        return targetState.getAdjective().substring(0, 1).toUpperCase() + targetState.getAdjective().substring(1).toLowerCase() + " Game " + survivalGame.getName();
    }
}
