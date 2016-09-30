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
package io.github.m0pt0pmatt.spongesurvivalgames.event;

import static com.google.common.base.Preconditions.checkNotNull;

import io.github.m0pt0pmatt.spongesurvivalgames.SpongeSurvivalGamesPlugin;
import io.github.m0pt0pmatt.spongesurvivalgames.game.SurvivalGame;
import io.github.m0pt0pmatt.spongesurvivalgames.game.SurvivalGameState;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;

public class GameStateChangedEvent extends SurvivalGameEvent {

    private final SurvivalGameState oldState;
    private final SurvivalGameState newState;

    public GameStateChangedEvent(SurvivalGame survivalGame, SurvivalGameState oldState, SurvivalGameState newState) {
        super(Cause.of(NamedCause.of("Survival Game State Changed", SpongeSurvivalGamesPlugin.PLUGIN)), survivalGame);
        this.oldState = checkNotNull(oldState, "oldState");
        this.newState = checkNotNull(newState, "newState");
    }

    public SurvivalGameState getOldState() {
        return oldState;
    }

    public SurvivalGameState getNewState() {
        return newState;
    }
}
