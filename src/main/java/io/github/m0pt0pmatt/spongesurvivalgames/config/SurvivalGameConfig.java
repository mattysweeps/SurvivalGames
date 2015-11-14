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

import java.util.*;

/**
 * Config for a Survival Game
 * <p>All configurable fields are stored in this class. Getter which return Nullable values return Optionals.</p>
 */
public class SurvivalGameConfig {

    private String worldName;
    private Integer xMin;
    private Integer xMax;
    private Integer zMin;
    private Integer zMax;
    private String exitWorld;
    private Vector exitVector;
    private Vector centerVector;
    private Integer playerLimit;
    private Integer countdownTime;
    private Double chestMidpoint;
    private Double chestRange;
    private Integer deathmatchRadius;
    private Integer deathmatchTime;
    private final Set<Vector> spawns = new HashSet<>();
    private final Set<Loot> loot = new HashSet<>();
    private final Set<Vector> chests = new HashSet<>();

    public SurvivalGameConfig() {

    }

    public SurvivalGameConfig(SurvivalGameConfig config) {
        worldName = config.worldName;
        xMin = config.xMin;
        xMax = config.xMax;
        zMin = config.zMin;
        zMax = config.zMax;
        exitWorld = config.exitWorld;
        exitVector = config.exitVector;
        centerVector = config.centerVector;
        playerLimit = config.playerLimit;
        countdownTime = config.countdownTime;
        chestMidpoint = config.chestMidpoint;
        chestRange = config.chestRange;
        deathmatchRadius = config.deathmatchRadius;
        deathmatchTime = config.deathmatchTime;
        spawns.addAll(config.spawns);
        loot.addAll(config.loot);
        chests.addAll(config.chests);
    }

    public Optional<String> getWorldName() {
        return worldName == null ? Optional.empty() : Optional.of(worldName);
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public Optional<Integer> getXMin() {
        return xMin == null ? Optional.empty() : Optional.of(xMin);
    }

    public void setXMin(Integer xMin) {
        this.xMin = xMin;
    }

    public Optional<Integer> getXMax() {
        return xMax == null ? Optional.empty() : Optional.of(xMax);
    }

    public void setXMax(Integer xMax) {
        this.xMax = xMax;
    }

    public Optional<Integer> getZMin() {
        return zMin == null ? Optional.empty() : Optional.of(zMin);
    }

    public void setZMin(Integer zMin) {
        this.zMin = zMin;
    }

    public Optional<Integer> getZMax() {
        return zMax == null ? Optional.empty() : Optional.of(zMax);
    }

    public void setZMax(Integer zMax) {
        this.zMax = zMax;
    }

    public Optional<String> getExitWorld() {
        return exitWorld == null ? Optional.empty() : Optional.of(exitWorld);
    }

    public void setExitWorld(String exitWorld) {
        this.exitWorld = exitWorld;
    }

    public Optional<Vector> getExitVector() {
        return exitVector == null ? Optional.empty() : Optional.of(exitVector);
    }

    public void setExitVector(Vector exitVector) {
        this.exitVector = exitVector;
    }

    public Optional<Vector> getCenterVector() {
        return centerVector == null ? Optional.empty() : Optional.of(centerVector);
    }

    public void setCenterVector(Vector centerVector) {
        this.centerVector = centerVector;
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

    public Optional<Integer> getDeathmatchRadius() {
        return deathmatchRadius == null ? Optional.empty() : Optional.of(deathmatchRadius);
    }

    public void setDeathmatchRadius(Integer deathmatchRadius) {
        this.deathmatchRadius = deathmatchRadius;
    }

    public Optional<Integer> getDeathmatchTime() {
        return deathmatchTime == null ? Optional.empty() : Optional.of(deathmatchTime);
    }

    public void setDeathmatchTime(Integer deathmatchTime) {
        this.deathmatchTime = deathmatchTime;
    }

    public Set<Vector> getSpawns() {
        return spawns;
    }

    public Set<Loot> getLoot() {
        return loot;
    }

    public Set<Vector> getChestLocations() {
        return chests;
    }
}
