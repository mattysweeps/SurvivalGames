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
package io.github.m0pt0pmatt.spongesurvivalgames.command.executor.state;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.text.Text;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import io.github.m0pt0pmatt.spongesurvivalgames.command.CommandKeys;
import io.github.m0pt0pmatt.spongesurvivalgames.command.element.SurvivalGameCommandElement;
import io.github.m0pt0pmatt.spongesurvivalgames.command.executor.BaseCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.game.SurvivalGame;
import io.github.m0pt0pmatt.spongesurvivalgames.game.SurvivalGameState;

import static com.google.common.base.Preconditions.checkNotNull;

public class AbstractStateCommand extends BaseCommand {

    private final Set<SurvivalGameState> states;
    private final Consumer<SurvivalGame> action;

    public AbstractStateCommand(
            List<String> aliases,
            String permission,
            Set<SurvivalGameState> states,
            Consumer<SurvivalGame> action) {
        super(aliases, permission, SurvivalGameCommandElement.getInstance(), Collections.emptyMap());
        this.states = checkNotNull(states, "states");
        this.action = checkNotNull(action, "action");
    }

    @Nonnull
    @Override
    public CommandResult execute(@Nonnull CommandSource src, @Nonnull CommandContext args) throws CommandException {

        SurvivalGame survivalGame = (SurvivalGame) args.getOne(CommandKeys.SURVIVAL_GAME)
                .orElseThrow(() -> new CommandException(Text.of("No Survival Game")));

        SurvivalGameState oldState = survivalGame.getState();

        if (!states.contains(oldState)) {
            throw new CommandException(Text.of("Must be in one of the following states: ")
                    .concat(Text.joinWith(Text.of(':'), states.stream().map(Text::of).collect(Collectors.toList()))));
        }

        try {
            action.accept(survivalGame);
        } catch (RuntimeException e){
            throw new CommandException(Text.of("Error while switching states: " + e.getMessage()), e);
        }

        src.sendMessage(Text.of("State changed from " + oldState + " to " + survivalGame.getState()));
        return CommandResult.success();
    }
}
