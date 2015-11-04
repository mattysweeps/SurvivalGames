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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class SurvivalGameConfig {


    private String worldName;

    private String exitWorld;

    private Vector exit;

    private Vector center;

    private Integer playerLimit;

    private Integer countdownTime;

    private Set<Vector> spawns = new HashSet<>();

    private Double chestMidpoint;

    private Double chestRange;

    private List<ItemStack> loot = new ArrayList<ItemStack>();

    public Optional<String> getWorldName() {
        return worldName == null ? Optional.empty() : Optional.of(worldName);
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public Optional<String> getExitWorld() {
        return exitWorld == null ? Optional.empty() : Optional.of(exitWorld);
    }

    public void setExitWorld(String exitWorld) {
        this.exitWorld = exitWorld;
    }

    public Optional<Vector> getExit() {
        return exit == null ? Optional.empty() : Optional.of(exit);
    }

    public void setExit(Vector exit) {
        this.exit = exit;
    }

    public Optional<Vector> getCenter() {
        return center == null ? Optional.empty() : Optional.of(center);
    }

    public void setCenter(Vector center) {
        this.center = center;
    }

    public Optional<Integer> getPlayerLimit() {
        return playerLimit == null ? Optional.empty() : Optional.of(playerLimit);
    }

    public void setPlayerLimit(Integer playerLimit) {
        this.playerLimit = playerLimit;
    }

    public Optional<Integer> getCountdownTime() {
        return countdownTime == null ? Optional.empty() : Optional.of(countdownTime);
    }

    public void setCountdownTime(Integer countdownTime) {
        this.countdownTime = countdownTime;
    }

    public Set<Vector> getSpawns() {
        return spawns;
    }

    public Optional<Double> getChestMidpoint() {
        return chestMidpoint == null ? Optional.empty() : Optional.of(chestMidpoint);
    }

    public void setChestMidpoint(Double chestMidpoint) {
        this.chestMidpoint = chestMidpoint;
    }

    public Optional<Double> getChestRange() {
        return chestRange == null ? Optional.empty() : Optional.of(chestRange);
    }

    public void setChestRange(Double chestRange) {
        this.chestRange = chestRange;
    }

    public List<ItemStack> getLoot() {
        return loot;
    }

}
