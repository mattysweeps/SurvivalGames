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

import io.github.m0pt0pmatt.survivalgames.command.CommandKeys;
import io.github.m0pt0pmatt.survivalgames.data.GameConfig;
import io.github.m0pt0pmatt.survivalgames.game.SurvivalGame;
import io.github.m0pt0pmatt.survivalgames.game.SurvivalGameRepository;
import java.util.Collections;
import java.util.Map;
import javax.annotation.Nonnull;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.args.SelectorCommandElement;

public class EventIntervalCommandElement extends SelectorCommandElement {

    private static final CommandElement INSTANCE = new EventIntervalCommandElement();

    private EventIntervalCommandElement() {
        super(CommandKeys.INTERVAL_NAME);
    }

    @Nonnull
    @Override
    protected Iterable<String> getChoices(@Nonnull CommandSource source) {
        return SurvivalGameCommandElement.getInstance()
                .getCurrentSurvivalGameName()
                .map(SurvivalGameRepository::get)
                .map(
                        o ->
                                o.map(SurvivalGame::getConfig)
                                        .map(GameConfig::getEventIntervals)
                                        .orElse(Collections.emptyMap()))
                .map(Map::keySet)
                .orElse(Collections.emptySet());
    }

    @Nonnull
    @Override
    protected Object getValue(@Nonnull String choice) throws IllegalArgumentException {
        return choice;
    }

    public static CommandElement getInstance() {
        return INSTANCE;
    }
}
