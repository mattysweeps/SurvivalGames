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

import com.flowpowered.math.vector.Vector3d;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.util.Tuple;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@ConfigSerializable
public class SurvivalGameConfig {

    @Setting(value = "world", comment = "World where the game takes place") @Nullable
    private String worldName;

    @Setting(comment = "World for the exit location") @Nullable
    private String exitWorld;

    @Setting(value = "exitLocation", comment = "Exit location") @Nullable
    private Vector3d exit;

    @Setting(value = "centerLocation", comment = "Center Location") @Nullable
    private Vector3d center;

    @Setting(comment = "Max number of players") @Nullable
    private Integer playerLimit;

    @Setting(comment = "Number of seconds for the countdown") @Nullable
    private Integer countdownTime;

    @Setting(comment = "Spawn points")
    private Set<Vector3d> spawns = new HashSet<>();

    public Optional<String> getWorldName() {
        return worldName == null ? Optional.empty() : Optional.of(worldName);
    }

    public void setWorldName(@Nullable String worldName) {
        this.worldName = worldName;
    }

    public Optional<String> getExitWorld() {
        return exitWorld == null ? Optional.empty() : Optional.of(exitWorld);
    }

    public void setExitWorld(@Nullable String exitWorld) {
        this.exitWorld = exitWorld;
    }

    public Optional<Vector3d> getExit() {
        return exit == null ? Optional.empty() : Optional.of(exit);
    }

    public void setExit(@Nullable Vector3d exit) {
        this.exit = exit;
    }

    public Optional<Vector3d> getCenter() {
        return center == null ? Optional.empty() : Optional.of(center);
    }

    public void setCenter(@Nullable Vector3d center) {
        this.center = center;
    }

    public Optional<Integer> getPlayerLimit() {
        return playerLimit == null ? Optional.empty() : Optional.of(playerLimit);
    }

    public void setPlayerLimit(@Nullable Integer playerLimit) {
        this.playerLimit = playerLimit;
    }

    public Optional<Integer> getCountdownTime() {
        return countdownTime == null ? Optional.empty() : Optional.of(countdownTime);
    }

    public void setCountdownTime(@Nullable Integer countdownTime) {
        this.countdownTime = countdownTime;
    }

    public Set<Vector3d> getSpawns() {
        return spawns;
    }

    public void setSpawns(Set<Vector3d> spawns) {
        this.spawns = spawns;
    }
}
