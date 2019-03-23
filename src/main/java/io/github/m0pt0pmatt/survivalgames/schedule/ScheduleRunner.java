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

package io.github.m0pt0pmatt.survivalgames.schedule;

import com.google.common.collect.ImmutableMap;
import io.github.m0pt0pmatt.survivalgames.SurvivalGamesPlugin;
import io.github.m0pt0pmatt.survivalgames.data.Schedule;
import io.github.m0pt0pmatt.survivalgames.data.WorldBorderStage;
import io.github.m0pt0pmatt.survivalgames.game.SurvivalGame;
import io.github.m0pt0pmatt.survivalgames.game.SurvivalGameRunningState;
import io.github.m0pt0pmatt.survivalgames.game.SurvivalGameState;
import io.github.m0pt0pmatt.survivalgames.game.SurvivalGameStateManager;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.WorldBorder;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

class ScheduleRunner implements Runnable {

    private enum ScheduleState {
        STOPPED,
        LOBBY,
        LOBBY_COUNTDOWN,
        RUNNING,
    }

    private final Map<ScheduleState, Runnable> stateFunctions = ImmutableMap.<ScheduleState, Runnable>builder()
            .put(ScheduleState.STOPPED, this::stopped)
            .put(ScheduleState.LOBBY, this::lobby)
            .put(ScheduleState.LOBBY_COUNTDOWN, this::lobbyCountdown)
            .put(ScheduleState.RUNNING, this::running)
            .build();

    private final SurvivalGame survivalGame;
    private final Schedule schedule;

    // Survival Game states which the runner thinks the game is in
    private SurvivalGameState savedState;
    private SurvivalGameRunningState savedRunningState;

    // For checking how much time has passed.
    private Instant startTime;

    // Top level boolean for cancelling
    private boolean shouldRun = true;

    // Scheduler state
    private ScheduleState state;

    // For moving the border
    private int borderIndex = 0;

    private boolean isDeathmatch = false;

    ScheduleRunner(SurvivalGame survivalGame, Schedule schedule) {
        this.survivalGame = survivalGame;
        this.schedule = schedule;

        setState(survivalGame.getState());
        setRunningState(survivalGame.getRunningState());
    }

    private void setState(SurvivalGameState currentState) {
        ScheduleState newState = null;
        if (currentState.equals(SurvivalGameState.RUNNING)) {
            newState = ScheduleState.RUNNING;
        } else if (currentState.equals(SurvivalGameState.STOPPED)) {
            newState = ScheduleState.STOPPED;
        } else if (currentState.equals(SurvivalGameState.READY)) {
            newState = ScheduleState.LOBBY;
        }
        toState(newState, null, null);
        savedState = currentState;
    }

    private void setRunningState(SurvivalGameRunningState currentState) {
        SurvivalGameRunningState newState = null;
        if (currentState.equals(SurvivalGameRunningState.STOPPED)) {
            newState = SurvivalGameRunningState.STOPPED;
            isDeathmatch = false;
        } else if (currentState.equals(SurvivalGameRunningState.IN_PROGRESS)) {
            newState = SurvivalGameRunningState.IN_PROGRESS;
            isDeathmatch = false;
        } else if (currentState.equals(SurvivalGameRunningState.DEATH_MATCH)) {
            newState = SurvivalGameRunningState.DEATH_MATCH;
            isDeathmatch = true;
        }
        borderIndex = 0;
        savedRunningState = newState;
    }

    @Override
    public void run() {
        while(shouldRun) {

            try {
                // check that state hasn't changed externally
                SurvivalGameState current = survivalGame.getState();
                if (current != savedState) {
                    setState(current);
                }

                SurvivalGameRunningState currentRunning = survivalGame.getRunningState();
                if (currentRunning != savedRunningState) {
                    setRunningState(currentRunning);
                }

                // Handle state
                Optional.ofNullable(stateFunctions.get(this.state)).ifPresent(Runnable::run);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    void cancel() {
        shouldRun = false;
    }

    private void stopped() {

        // Ready the game as soon as possible
        toState(ScheduleState.LOBBY, () -> SurvivalGameStateManager.ready(survivalGame), Text.of("Readying game"));
    }

    private void lobby() {

        // No reason to start a game if there'a no players
        if (survivalGame.getPlayerCount() == 0) {
            startTime = null;
            return;
        }

        if (startTime == null) {
            startTime = Instant.now();
        }

        if (schedule.getLobbyTimeLimit().isPresent()) {

            // Check if enough time has passed
            Instant now = Instant.now();
            Duration lobbyTimeLimit = schedule.getLobbyTimeLimit().get();
            Instant change = startTime.plus(lobbyTimeLimit);
            if (now.isAfter(change)) {
                toState(ScheduleState.LOBBY_COUNTDOWN, null, null);
            }

        } else if (schedule.getMinimumPlayerCount().isPresent()) {

            // Check if enough players have joined
            Integer minPlayerCount = schedule.getMinimumPlayerCount().get();
            if (survivalGame.getPlayerCount() > minPlayerCount) {
                toState(ScheduleState.LOBBY_COUNTDOWN, null, null);
            }

        } else {
            // No lobby time limit so start the lobby countdown
            toState(ScheduleState.LOBBY_COUNTDOWN, null, null);
        }
    }

    private void lobbyCountdown() {

        Runnable start = () -> {
            SurvivalGameStateManager.start(survivalGame);
            borderIndex = 0;
        };

        if (startTime == null) {
            startTime = Instant.now();
        }

        if (schedule.getLobbyCountdown().isPresent()) {
            Instant now = Instant.now();
            Duration lobbyCountDown = schedule.getLobbyCountdown().get();
            Instant change = startTime.plus(lobbyCountDown);
            if (now.isAfter(change)) {
                toState(ScheduleState.RUNNING, start, Text.of("Starting game"));
            } else {
                survivalGame.sendMessage(Text.of("Game starts in ", lobbyCountDown.minus(Duration.between(startTime, now)).getSeconds(), " seconds"));
            }
        } else {
            // Start immediately
            toState(ScheduleState.RUNNING, start, Text.of("Starting game"));
        }
    }

    private void running() {

        if (survivalGame.getState().equals(SurvivalGameState.STOPPED)) {
            toState(ScheduleState.STOPPED, null, null);
            return;
        }

        if (startTime == null) {
            startTime = Instant.now();
        }

        List<WorldBorderStage> stages;
        if (isDeathmatch) {
            stages = schedule.getDeathMatchStages();
        } else {
            stages = schedule.getWorldBorderStages();
        }

        if (stages.size() <= borderIndex) {
            return;
        }

        WorldBorderStage stage = stages.get(borderIndex);
        Instant now = Instant.now();
        Instant change = startTime.plus(stage.getWaitTime());
        if (now.isAfter(change)) {

            String worldName = survivalGame.getConfig().getWorldName().orElseThrow(() -> new RuntimeException("world-name"));
            World world = Sponge.getServer().getWorld(worldName).orElseThrow(() -> new RuntimeException("world"));
            WorldBorder wb = world.getWorldBorder();

            double newDiameter = Double.max(wb.getDiameter() - stage.getDiameterDelta(), 0.0);
            wb.setDiameter(newDiameter, stage.getMovementTime().toMillis());
            borderIndex++;
            startTime = null;
            survivalGame.sendMessage(Text.of("The border is starting to shrink..."));
        }
    }

    private void toState(ScheduleState targetState, Runnable setState, Text message) {
        SurvivalGamesPlugin.SYNC_EXECUTOR.submit(() -> {
            if (message != null) {
                survivalGame.sendMessage(message);
            }
            if (setState != null) {
                setState.run();
            }
            startTime = null;
            state = targetState;
        });
    }
}
