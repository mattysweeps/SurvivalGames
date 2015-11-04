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

package io.github.m0pt0pmatt.spongesurvivalgames.config;

import io.github.m0pt0pmatt.spongesurvivalgames.loot.Loot;
import org.bukkit.util.Vector;

/**
 * Builder Pattern for SurvivalGameConfigs
 */
public class SurvivalGameConfigBuilder {

    private final SurvivalGameConfig config;

    public SurvivalGameConfigBuilder() {
        config = new SurvivalGameConfig();
    }

    public SurvivalGameConfig build() {
        return config;
    }

    public SurvivalGameConfigBuilder worldName(String worldName) {
        config.setWorldName(worldName);
        return this;
    }

    public SurvivalGameConfigBuilder exitWorld(String exitWorld) {
        config.setExitWorld(exitWorld);
        return this;
    }

    public SurvivalGameConfigBuilder exitLocation(Vector exit) {
        config.setExit(exit);
        return this;
    }

    public SurvivalGameConfigBuilder centerLocation(Vector center) {
        config.setCenter(center);
        return this;
    }

    public SurvivalGameConfigBuilder playerLimit(Integer playerLimit) {
        config.setPlayerLimit(playerLimit);
        return this;
    }

    public SurvivalGameConfigBuilder countdownTime(Integer countdownTime) {
        config.setCountdownTime(countdownTime);
        return this;
    }

    public SurvivalGameConfigBuilder addSpawn(Vector vector) {
        config.getSpawns().add(vector);
        return this;
    }

    public SurvivalGameConfigBuilder chestMidpoint(Double chestMidpoint) {
        config.setChestMidpoint(chestMidpoint);
        return this;
    }

    public SurvivalGameConfigBuilder chestRange(Double chestRange) {
        config.setChestRange(chestRange);
        return this;
    }

    public SurvivalGameConfigBuilder addLoot(Loot loot) {
        config.getLoot().add(loot);
        return this;
    }

    public SurvivalGameConfigBuilder xMin(Integer xMin) {
        config.setXMin(xMin);
        return this;
    }

    public SurvivalGameConfigBuilder xMax(Integer xMax) {
        config.setXMax(xMax);
        return this;
    }

    public SurvivalGameConfigBuilder yMin(Integer yMin) {
        config.setYMin(yMin);
        return this;
    }

    public SurvivalGameConfigBuilder yMax(Integer yMax) {
        config.setYMax(yMax);
        return this;
    }

    public SurvivalGameConfigBuilder zMin(Integer zMin) {
        config.setZMin(zMin);
        return this;
    }

    public SurvivalGameConfigBuilder zMax(Integer zMax) {
        config.setZMax(zMax);
        return this;
    }
}
