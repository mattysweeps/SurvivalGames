/*
 * This file is part of SurvivalGames, licensed under the MIT License (MIT).
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

import ninja.leaping.configurate.objectmapping.ObjectMapper;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ConfigSerializable
public class Schedule {

    public static ObjectMapper<Schedule> OBJECT_MAPPER;

    static {
        try {
            OBJECT_MAPPER = ObjectMapper.forClass(Schedule.class);
        } catch (ObjectMappingException e) {
            throw new RuntimeException(e);
        }
    }

    @Setting(value = "minimumPlayerCount")
    private Integer minimumPlayerCount;

    @Setting(value = "lobbyTimeLimit")
    private Long lobbyTimeLimit;

    @Setting(value = "lobbyCountdown")
    private Long lobbyCountdown;

    @Setting(value = "worldBorderStages")
    private List<WorldBorderStage> worldBorderStages = new ArrayList<>();

    @Setting(value = "deathMatchStages")
    private List<WorldBorderStage> deathMatchStages = new ArrayList<>();

    public Optional<Integer> getMinimumPlayerCount() {
        return Optional.ofNullable(minimumPlayerCount);
    }

    public void setMinimumPlayerCount(Integer minimumPlayerCount) {
        this.minimumPlayerCount = minimumPlayerCount;
    }

    public Optional<Duration> getLobbyTimeLimit() {
        return Optional.ofNullable(Duration.of(lobbyTimeLimit, ChronoUnit.SECONDS));
    }

    public void setLobbyTimeLimit(Duration lobbyTimeLimit) {
        this.lobbyTimeLimit = lobbyTimeLimit.getSeconds();
    }

    public void setLobbyTimeLimit(Long lobbyTimeLimit) {
        this.lobbyTimeLimit = lobbyTimeLimit;
    }

    public Optional<Duration> getLobbyCountdown() {
        return Optional.ofNullable(Duration.of(lobbyCountdown, ChronoUnit.SECONDS));
    }

    public void setLobbyCountdown(Duration lobbyCountdown) {
        this.lobbyCountdown = lobbyCountdown.getSeconds();
    }

    public void setLobbyCountdown(Long lobbyCountdown) {
        this.lobbyCountdown = lobbyCountdown;
    }

    public List<WorldBorderStage> getWorldBorderStages() {
        return worldBorderStages;
    }

    public void setWorldBorderStages(List<WorldBorderStage> worldBorderStages) {
        this.worldBorderStages = worldBorderStages;
    }

    public List<WorldBorderStage> getDeathMatchStages() {
        return deathMatchStages;
    }

    public void setDeathMatchStages(List<WorldBorderStage> deathMatchStages) {
        this.deathMatchStages = deathMatchStages;
    }
}
