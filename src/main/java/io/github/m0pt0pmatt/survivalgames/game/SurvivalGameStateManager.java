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
import io.github.m0pt0pmatt.survivalgames.event.GameStateChangedEvent;
import io.github.m0pt0pmatt.survivalgames.task.*;
import io.github.m0pt0pmatt.survivalgames.task.player.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.util.TextMessageException;

/**
 * Manages the transitions between states for Survival Games. Contains the knowledge of what tasks
 * to run for what state transitions.
 */
public class SurvivalGameStateManager {

    private static final List<Task> READY_TASKS =
            Collections.singletonList(RestoreBlocksTask.getInstance());

    private static final List<Task> START_TASKS =
            Arrays.asList(
                    SetCommandBlocksTask.getInstance(),
                    FillChestsTask.getInstance(),
                    CreateCageSnapshotsTask.getInstance(),
                    SpawnPlayersTask.getInstance(),
                    HealPlayersTask.getInstance(),
                    SpawnSpectatorsTask.getInstance(),
                    CreateCountdownTask.getInstance(),
                    CreateScoreboardTask.getInstance(),
                    CreateWorldBorderTask.getInstance(),
                    StartEventIntervalsTask.getInstance(),
                    StartMobSpawnersTask.getInstance());

    private static final List<Task> DEATH_MATCH_TASKS =
            Arrays.asList(
                    CreateCageSnapshotsTask.getInstance(),
                    SpawnPlayersTask.getInstance(),
                    CreateCountdownTask.getInstance(),
                    CreateDeathmatchBorderTask.getInstance());

    private static final List<Task> STOP_TASKS =
            Arrays.asList(
                    ReturnPlayerTask.getInstance(),
                    HealPlayersTask.getInstance(),
                    ClearScoreBoardTask.getInstance(),
                    ClearWorldBorderTask.getInstance(),
                    ClearPlayersTask.getInstance(),
                    StopMobSpawnersTask.getInstance(),
                    StopEventIntervalsTask.getInstance(),
                    CleanStateTask.getInstance());

    public static void ready(SurvivalGame survivalGame) {
        checkConfig(survivalGame.getConfig());
        try {
            SurvivalGameState oldState = survivalGame.getState();
            executeTasks(READY_TASKS, survivalGame);
            survivalGame.state = SurvivalGameState.READY;
            survivalGame.runningState = SurvivalGameRunningState.STOPPED;
            Sponge.getEventManager()
                    .post(
                            new GameStateChangedEvent(
                                    survivalGame, oldState, SurvivalGameState.READY));
        } catch (TextMessageException e) {
            e.printStackTrace();
        }
    }

    public static void start(SurvivalGame survivalGame) {
        checkConfig(survivalGame.getConfig());
        try {
            SurvivalGameState oldState = survivalGame.getState();
            executeTasks(START_TASKS, survivalGame);
            survivalGame.state = SurvivalGameState.RUNNING;
            survivalGame.runningState = SurvivalGameRunningState.IN_PROGRESS;
            Sponge.getEventManager()
                    .post(
                            new GameStateChangedEvent(
                                    survivalGame, oldState, SurvivalGameState.RUNNING));
        } catch (TextMessageException e) {
            e.printStackTrace();
        }
    }

    public static void deathMatch(SurvivalGame survivalGame) {
        try {
            executeTasks(DEATH_MATCH_TASKS, survivalGame);
            survivalGame.state = SurvivalGameState.RUNNING;
            survivalGame.runningState = SurvivalGameRunningState.DEATH_MATCH;
        } catch (TextMessageException e) {
            e.printStackTrace();
        }
    }

    public static void stop(SurvivalGame survivalGame) {
        try {
            SurvivalGameState oldState = survivalGame.getState();
            executeTasks(STOP_TASKS, survivalGame);
            survivalGame.state = SurvivalGameState.STOPPED;
            survivalGame.runningState = SurvivalGameRunningState.STOPPED;
            Sponge.getEventManager()
                    .post(
                            new GameStateChangedEvent(
                                    survivalGame, oldState, SurvivalGameState.STOPPED));
        } catch (TextMessageException e) {
            e.printStackTrace();
        }
    }

    private static void checkConfig(GameConfig config) {

        if (config.getSpawnPoints().isEmpty()) {
            throw new IllegalArgumentException("No spawn points set.");
        }

        String worldName =
                config.getWorldName()
                        .orElseThrow(() -> new IllegalArgumentException("World name is not set."));

        if (!Sponge.getServer().getWorld(worldName).isPresent()) {
            throw new IllegalArgumentException("World " + worldName + " does not exist.");
        }

        if (!config.getBlockArea().getLesserBoundary().isPresent()
                || !config.getBlockArea().getGreaterBoundary().isPresent()) {
            throw new IllegalArgumentException("Boundaries are not set.");
        }

        String exitWorldName =
                config.getExitWorldName()
                        .orElseThrow(
                                () -> new IllegalArgumentException("Exit world name is not set."));

        if (!Sponge.getServer().getWorld(exitWorldName).isPresent()) {
            throw new IllegalArgumentException("Exit world " + worldName + " does not exist.");
        }

        if (!config.getExitVector().isPresent()) {
            throw new IllegalArgumentException("Exit vector is not set.");
        }

        if (!config.getCenterVector().isPresent()) {
            throw new IllegalArgumentException("Center vector is not set.");
        }

        if (!config.getCountdownSeconds().isPresent()) {
            throw new IllegalArgumentException("Countdown seconds are not set.");
        }

        if (!config.getPlayerLimit().isPresent()) {
            throw new IllegalArgumentException("Player limit is not set.");
        }

        if (!config.getChestMidpoint().isPresent()) {
            throw new IllegalArgumentException("Chest midpoint is not set.");
        }

        if (!config.getChestRange().isPresent()) {
            throw new IllegalArgumentException("Chest range is not set.");
        }
    }

    private static void executeTasks(List<Task> tasks, SurvivalGame survivalGame)
            throws TextMessageException {
        for (Task task : tasks) {
            task.execute(survivalGame);
        }
    }
}
