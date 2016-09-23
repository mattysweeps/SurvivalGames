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

import static java.lang.Double.max;
import static java.lang.Double.min;

import com.flowpowered.math.vector.Vector3d;
import ninja.leaping.configurate.objectmapping.ObjectMapper;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Config for a Survival Game.
 * <p>All configurable fields are stored in this class. Getters return Optionals.</p>
 */
@ConfigSerializable
public class SurvivalGameConfig {

    private static final int DEFAULT_PLAYER_LIMIT = 4;
    private static final int DEFAULT_COUNTDOWN_SECONDS = 10;

    public static ObjectMapper<SurvivalGameConfig> OBJECT_MAPPER;

    static {
        try {
            OBJECT_MAPPER = ObjectMapper.forClass(SurvivalGameConfig.class);
        } catch (ObjectMappingException ignored) {

        }
    }

    @Setting(value = "spawn-points", comment = "Where players spawn when the game starts.")
    private List<Vector3d> spawnPoints;

    @Setting(value = "world-name", comment = "The name of the world where the survival game will take place.")
    private String worldName;

    @Setting(value = "lesser-boundary", comment = "Map boundary")
    private Vector3d lesserBoundary;

    @Setting(value = "greater-boundary", comment = "Map boundary")
    private Vector3d greaterBoundary;

    @Setting(value = "exit-world-name", comment = "The name of the world where players teleport to once they leave the game.")
    private String exitWorldName;

    @Setting(value = "exit-vector", comment = "The location where players teleport to once they leave the game.")
    private Vector3d exitVector;

    @Setting(value = "center-vector", comment = "The center of the survival game map.")
    private Vector3d centerVector;

    @Setting(value = "player-limit", comment = "The max number of players which can join the game.")
    private Integer playerLimit;

    @Setting(value = "countdown-seconds", comment = "The number of seconds to countdown once the survival game starts.")
    private Integer countdownSeconds;

    @Setting(value = "items")
    private List<ItemStackSnapshot> items;

    @Setting(value = "blocks")
    private List<BlockSnapshot> blocks;

    @Setting(value = "blocksValid")
    private boolean blocksValid = false;

    @Setting(value = "chest-midpoint")
    private Integer chestMidpoint;

    @Setting(value = "chest-range")
    private Integer chestRange;

    public SurvivalGameConfig() {
        setPlayerLimit(DEFAULT_PLAYER_LIMIT);
        setCountdownSeconds(DEFAULT_COUNTDOWN_SECONDS);
        spawnPoints = new ArrayList<>();
        items = new ArrayList<>();
        blocks = new ArrayList<>();
    }

    public Optional<String> getWorldName() {
        return Optional.ofNullable(worldName);
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public Optional<Vector3d> getLesserBoundary() {
        return Optional.ofNullable(lesserBoundary);
    }

    public Optional<Vector3d> getGreaterBoundary() {
        if (greaterBoundary != null) {
            return Optional.of(greaterBoundary);
        }

        return getLesserBoundary();
    }

    public void addBoundaryVector(Vector3d vector3i) {

        if (vector3i == null) {
            return;
        }

        if (lesserBoundary == null) {
            lesserBoundary = vector3i;
        } else {
            if (greaterBoundary != null) {

                double x1 = lesserBoundary.getX();
                double x2 = greaterBoundary.getX();
                double x3 = vector3i.getX();

                double y1 = lesserBoundary.getY();
                double y2 = greaterBoundary.getY();
                double y3 = vector3i.getY();

                double z1 = lesserBoundary.getZ();
                double z2 = greaterBoundary.getZ();
                double z3 = vector3i.getZ();

                lesserBoundary = new Vector3d(
                        min(min(x1, x2), x3),
                        min(min(y1, y2), y3),
                        min(min(z1, z2), z3));

                greaterBoundary = new Vector3d(
                        max(max(x1, x2), x3),
                        max(max(y1, y2), y3),
                        max(max(z1, z2), z3));
            } else {
                double x1 = lesserBoundary.getX();
                double x2 = vector3i.getX();

                double y1 = lesserBoundary.getY();
                double y2 = vector3i.getY();

                double z1 = lesserBoundary.getZ();
                double z2 = vector3i.getZ();

                lesserBoundary = new Vector3d(
                        min(x1, x2),
                        min(y1, y2),
                        min(z1, z2));

                greaterBoundary = new Vector3d(
                        max(x1, x2),
                        max(y1, y2),
                        max(z1, z2));
            }
        }
    }

    public void clearBoundaryVectors() {
        lesserBoundary = null;
        greaterBoundary = null;
    }

    public Optional<String> getExitWorldName() {
        return Optional.ofNullable(exitWorldName);
    }

    public void setExitWorldName(String exitWorldName) {
        this.exitWorldName = exitWorldName;
    }

    public Optional<Vector3d> getExitVector() {
        return Optional.ofNullable(exitVector);
    }

    public void setExitVector(Vector3d exitVector) {
        this.exitVector = exitVector;
    }

    public Optional<Vector3d> getCenterVector() {
        return Optional.ofNullable(centerVector);
    }

    public void setCenterVector(Vector3d centerVector) {
        this.centerVector = centerVector;
    }

    public Optional<Integer> getPlayerLimit() {
        return Optional.ofNullable(playerLimit);
    }

    public void setPlayerLimit(Integer playerLimit) {
        this.playerLimit = playerLimit;
    }

    public Optional<Integer> getCountdownSeconds() {
        return Optional.ofNullable(countdownSeconds);
    }

    public void setCountdownSeconds(Integer countdownSeconds) {
        this.countdownSeconds = countdownSeconds;
    }

    public List<Vector3d> getSpawnPoints() {
        return spawnPoints;
    }

    public List<ItemStackSnapshot> getItems() {
        return items;
    }

    public List<BlockSnapshot> getBlocks() {
        return blocks;
    }

    public boolean areBlocksValid() {
        return blocksValid;
    }

    public void setBlocksValid(boolean blocksValid) {
        this.blocksValid = blocksValid;
    }

    public Optional<Integer> getChestMidpoint() {
        return Optional.ofNullable(chestMidpoint);
    }

    public void setChestMidpoint(Integer chestMidpoint) {
        this.chestMidpoint = chestMidpoint;
    }

    public Optional<Integer> getChestRange() {
        return Optional.ofNullable(chestRange);
    }

    public void setChestRange(Integer chestRange) {
        this.chestRange = chestRange;
    }
}
