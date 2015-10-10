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

import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Optional;
import java.util.UUID;

public class SurvivalGameConfig {

    private Optional<UUID> worldUUID = Optional.empty();
    private Optional<Location<World>> exit = Optional.empty();
    private Optional<Location<World>> center = Optional.empty();
    private int playerLimit = 25; //Default player limit
    private int countdownTime = 10; //Default countdown time

    public Optional<UUID> getWorldUUID() {
        return worldUUID;
    }

    public void setWorldUUID(Optional<UUID> worldUUID) {
        this.worldUUID = worldUUID;
    }

    public Optional<Location<World>> getExit() {
        return exit;
    }

    public void setExit(Optional<Location<World>> exit) {
        this.exit = exit;
    }

    public Optional<Location<World>> getCenter() {
        return center;
    }

    public void setCenter(Optional<Location<World>> center) {
        this.center = center;
    }

    public int getPlayerLimit() {
        return playerLimit;
    }

    public void setPlayerLimit(int playerLimit) {
        this.playerLimit = playerLimit;
    }

    public int getCountdownTime() {
        return countdownTime;
    }

    public void setCountdownTime(int countdownTime) {
        this.countdownTime = countdownTime;
    }
}
