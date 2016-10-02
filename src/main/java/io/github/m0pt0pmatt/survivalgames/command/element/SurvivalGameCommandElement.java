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
package io.github.m0pt0pmatt.survivalgames.command.element;

import com.google.common.collect.ImmutableList;
import io.github.m0pt0pmatt.survivalgames.command.CommandKeys;
import io.github.m0pt0pmatt.survivalgames.game.SurvivalGame;
import io.github.m0pt0pmatt.survivalgames.game.SurvivalGameRepository;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.SelectorCommandElement;
import org.spongepowered.api.text.selector.Selector;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

public class SurvivalGameCommandElement extends SelectorCommandElement {

    private static final SurvivalGameCommandElement INSTANCE = new SurvivalGameCommandElement();
    private static String currentSurvivalGame;

    private SurvivalGameCommandElement() {
        super(CommandKeys.SURVIVAL_GAME);
    }

    @Nonnull
    @Override
    protected Iterable<String> getChoices(@Nonnull CommandSource source) {
        return SurvivalGameRepository.values().stream()
                .map(SurvivalGame::getName)
                .collect(Collectors.toList());
    }

    @Nonnull
    @Override
    public List<String> complete(@Nonnull CommandSource src, CommandArgs args, CommandContext context) {
        Object state = args.getState();
        final Optional<String> nextArg = args.nextIfPresent();
        args.setState(state);
        List<String> choices = nextArg.isPresent() ? Selector.complete(nextArg.get()) : ImmutableList.of();

        if (choices.isEmpty()) {
            choices = super.complete(src, args, context);
        }

        if (choices.size() == 1) {
            currentSurvivalGame = choices.get(0);
        } else {
            currentSurvivalGame = null;
        }

        return choices;
    }

    @Nonnull
    @Override
    protected Object getValue(@Nonnull String choice) throws IllegalArgumentException {
        Optional<SurvivalGame> survivalGame = SurvivalGameRepository.get(choice);
        if (survivalGame.isPresent()) {
            return survivalGame.get();
        }
        throw new IllegalArgumentException();
    }

    Optional<String> getCurrentSurvivalGameName() {
        return Optional.ofNullable(currentSurvivalGame);
    }

    public static SurvivalGameCommandElement getInstance() {
        return INSTANCE;
    }
}
