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

package io.github.m0pt0pmatt.survivalgames.game;

import io.github.m0pt0pmatt.survivalgames.data.GameConfig;
import org.spongepowered.api.block.tileentity.CommandBlock;
import org.spongepowered.api.entity.EntitySnapshot;
import org.spongepowered.api.world.BlockChangeFlags;

import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/** Represents a Survival Game. */
public class SurvivalGame {

    SurvivalGameState state;
    SurvivalGameRunningState runningState;

    private final String name;
    private final GameConfig config;
    private final Set<UUID> playerUUIDs;
    private final Set<UUID> spectatorUUIDs;
    private final Set<CommandBlock> commandBlocks;
    private final Set<UUID> activeMobSpawners;
    private final Set<UUID> activeEventIntervals;
    private final Map<UUID, EntitySnapshot> playerSnapshots;

    public SurvivalGame(String name, GameConfig config) {
        this.name = checkNotNull(name);
        state = SurvivalGameState.STOPPED;
        runningState = SurvivalGameRunningState.STOPPED;
        this.config = checkNotNull(config);
        playerUUIDs = new HashSet<>();
        spectatorUUIDs = new HashSet<>();
        commandBlocks = new HashSet<>();
        activeMobSpawners = new HashSet<>();
        activeEventIntervals = new HashSet<>();
        playerSnapshots = new HashMap<>();
    }

    public SurvivalGame(String name) {
        this(name, new GameConfig());
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

    public GameConfig getConfig() {
        return config;
    }

    public Set<UUID> getPlayerUUIDs() {
        return playerUUIDs;
    }

    public Set<UUID> getSpectatorUUIDs() {
        return spectatorUUIDs;
    }

    public Set<CommandBlock> getCommandBlocks() {
        return commandBlocks;
    }

    public Set<UUID> getActiveMobSpawners() {
        return activeMobSpawners;
    }

    public Set<UUID> getActiveEventIntervals() {
        return activeEventIntervals;
    }

    public Map<UUID, EntitySnapshot> getPlayerSnapshots() {
        return playerSnapshots;
    }

    public void restoreBlocks() {
        getConfig().getBlocks().forEach(blockSnapshot -> blockSnapshot.restore(true, BlockChangeFlags.ALL));
    }
}
