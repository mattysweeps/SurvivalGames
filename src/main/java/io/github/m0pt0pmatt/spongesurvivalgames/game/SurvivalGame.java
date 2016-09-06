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

package io.github.m0pt0pmatt.spongesurvivalgames.game;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import io.github.m0pt0pmatt.spongesurvivalgames.config.SurvivalGameConfig;

/** represents a Survival Game. */
public class SurvivalGame {

    SurvivalGameState state;
    SurvivalGameRunningState runningState;

    private final String name;
    private final SurvivalGameConfig config;
    private final Set<UUID> playerUUIDs;

    public SurvivalGame(String name) {
        this.name = name;
        state = SurvivalGameState.STOPPED;
        runningState = SurvivalGameRunningState.STOPPED;
        config = new SurvivalGameConfig();
        playerUUIDs = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public SurvivalGameState getState() {
        return state;
    }

    public SurvivalGameRunningState getRunningState() {
        return runningState;
    }

    public SurvivalGameConfig getConfig() {
        return config;
    }

    public Set<UUID> getPlayerUUIDs() {
        return playerUUIDs;
    }
}
