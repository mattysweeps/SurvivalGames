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
@SuppressWarnings("UnusedReturnValue")
public class SurvivalGameConfigBuilder {

    private final SurvivalGameConfig config;

    public SurvivalGameConfigBuilder(SurvivalGameConfig config) {
        this.config = config;
    }

    public SurvivalGameConfig build() {
        return config;
    }

    public SurvivalGameConfigBuilder worldName(String worldName) {
        if (worldName != null) config.setWorldName(worldName);
        return this;
    }

    public SurvivalGameConfigBuilder exitWorld(String exitWorld) {
        if (exitWorld != null) config.setExitWorld(exitWorld);
        return this;
    }

    public SurvivalGameConfigBuilder exitLocation(Vector exit) {
        if (exit != null) config.setExitVector(exit);
        return this;
    }

    public SurvivalGameConfigBuilder centerLocation(Vector center) {
        if (center != null) config.setCenterVector(center);
        return this;
    }

    public SurvivalGameConfigBuilder playerLimit(Integer playerLimit) {
        if (playerLimit != null) config.setPlayerLimit(playerLimit);
        return this;
    }

    public SurvivalGameConfigBuilder countdownTime(Integer countdownTime) {
        if (countdownTime != null) config.setCountdownTime(countdownTime);
        return this;
    }

    public SurvivalGameConfigBuilder addSpawn(Vector vector) {
        if (vector != null) config.getSpawns().add(vector);
        return this;
    }

    public SurvivalGameConfigBuilder chestMidpoint(Double chestMidpoint) {
        if (chestMidpoint != null) config.setChestMidpoint(chestMidpoint);
        return this;
    }

    public SurvivalGameConfigBuilder chestRange(Double chestRange) {
        if (chestRange != null) config.setChestRange(chestRange);
        return this;
    }

    public SurvivalGameConfigBuilder addLoot(Loot loot) {
        if (loot != null) config.getLoot().add(loot);
        return this;
    }

    public SurvivalGameConfigBuilder xMin(Integer xMin) {
        if (xMin != null) config.setXMin(xMin);
        return this;
    }

    public SurvivalGameConfigBuilder xMax(Integer xMax) {
        if (xMax != null) config.setXMax(xMax);
        return this;
    }

    public SurvivalGameConfigBuilder zMin(Integer zMin) {
        if (zMin != null) config.setZMin(zMin);
        return this;
    }

    public SurvivalGameConfigBuilder zMax(Integer zMax) {
        if (zMax != null) config.setZMax(zMax);
        return this;
    }

    public SurvivalGameConfigBuilder deathmatchRadius(Integer deathmatchRadius) {
        if (deathmatchRadius != null) config.setDeathmatchRadius(deathmatchRadius);
        return this;
    }

    public SurvivalGameConfigBuilder deathmatchTime(Integer deathmatchTime) {
        if (deathmatchTime != null) config.setDeathmatchTime(deathmatchTime);
        return this;
    }

    public SurvivalGameConfigBuilder addChestLocation(Vector v) {
        if (v != null) config.getChestLocations().add(v);
        return this;
    }

}
