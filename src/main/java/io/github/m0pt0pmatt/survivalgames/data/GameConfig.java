/*
 * This file is part of SurvivalGamesPlugin, licensed under the MIT License (MIT).
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

package io.github.m0pt0pmatt.survivalgames.data;

import com.flowpowered.math.vector.Vector3d;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import ninja.leaping.configurate.objectmapping.ObjectMapper;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Config for a Survival Game.
 * All configurable fields are stored in this class. Getters return Optionals.
 */
@ConfigSerializable
public class GameConfig {

    private static final int DEFAULT_PLAYER_LIMIT = 4;
    private static final int DEFAULT_COUNTDOWN_SECONDS = 10;

    public static ObjectMapper<GameConfig> OBJECT_MAPPER;
    static {
        try {
            OBJECT_MAPPER = ObjectMapper.forClass(GameConfig.class);
        } catch (ObjectMappingException ignored) {

        }
    }

    @Setting(value = "spawn-points", comment = "Where players spawn when the game starts.")
    private List<Vector3d> spawnPoints;

    @Setting(value = "world-name", comment = "The name of the world where the survival game will take place.")
    private String worldName;

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

    @Setting(value = "chest-midpoint")
    private Integer chestMidpoint;

    @Setting(value = "chest-range")
    private Integer chestRange;

    @Setting(value = "itemConfig")
    private ItemConfig itemConfig;

    @Setting(value = "blockArea")
    private Area blockArea;

    @Setting(value = "mobSpawnAreas")
    private List<MobSpawnArea> mobSpawnAreas = Lists.newArrayList();

    @Setting(value = "event-intervals")
    private Map<String, Integer> eventIntervals = Maps.newHashMap();

    public GameConfig() {
        setPlayerLimit(DEFAULT_PLAYER_LIMIT);
        setCountdownSeconds(DEFAULT_COUNTDOWN_SECONDS);
        spawnPoints = new ArrayList<>();
    }

    public Optional<String> getWorldName() {
        return Optional.ofNullable(worldName);
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
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

    public void setItemConfig(ItemConfig itemConfig) {
        this.itemConfig = itemConfig;
    }

    public ItemConfig getItemConfig() {
        if (itemConfig == null) {
            itemConfig = new ItemConfig();
        }
        return itemConfig;
    }

    public Area getBlockArea() {
        if (blockArea == null) {
            blockArea = new Area();
        }
        return blockArea;
    }

    public List<MobSpawnArea> getMobSpawnAreas() {
        return mobSpawnAreas;
    }

    public Map<String, Integer> getEventIntervals() {
        return eventIntervals;
    }
}
