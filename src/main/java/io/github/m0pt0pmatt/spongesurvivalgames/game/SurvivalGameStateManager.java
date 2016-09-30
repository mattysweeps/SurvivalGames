/*
 *  QuestManager: An RPG plugin for the Bukkit API.
 *  Copyright (C) 2015-2016 Github Contributors
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.github.m0pt0pmatt.spongesurvivalgames.game;

import io.github.m0pt0pmatt.spongesurvivalgames.data.GameConfig;
import io.github.m0pt0pmatt.spongesurvivalgames.event.GameStateChangedEvent;
import io.github.m0pt0pmatt.spongesurvivalgames.task.ClearPlayersTask;
import io.github.m0pt0pmatt.spongesurvivalgames.task.StartMobSpawnersTask;
import io.github.m0pt0pmatt.spongesurvivalgames.task.StopEventIntervalsTask;
import io.github.m0pt0pmatt.spongesurvivalgames.task.player.ClearScoreBoardTask;
import io.github.m0pt0pmatt.spongesurvivalgames.task.ClearWorldBorderTask;
import io.github.m0pt0pmatt.spongesurvivalgames.task.CreateCageSnapshotsTask;
import io.github.m0pt0pmatt.spongesurvivalgames.task.player.CreateCountdownTask;
import io.github.m0pt0pmatt.spongesurvivalgames.task.CreateDeathmatchBorderTask;
import io.github.m0pt0pmatt.spongesurvivalgames.task.player.CreateScoreboardTask;
import io.github.m0pt0pmatt.spongesurvivalgames.task.CreateWorldBorderTask;
import io.github.m0pt0pmatt.spongesurvivalgames.task.player.DespawnPlayersTask;
import io.github.m0pt0pmatt.spongesurvivalgames.task.FillChestsTask;
import io.github.m0pt0pmatt.spongesurvivalgames.task.player.HealPlayersTask;
import io.github.m0pt0pmatt.spongesurvivalgames.task.SetBlocksTask;
import io.github.m0pt0pmatt.spongesurvivalgames.task.player.SpawnPlayersTask;
import io.github.m0pt0pmatt.spongesurvivalgames.task.player.SpawnSpectatorsTask;
import io.github.m0pt0pmatt.spongesurvivalgames.task.StartEventIntervalsTask;
import io.github.m0pt0pmatt.spongesurvivalgames.task.StopMobSpawnersTask;
import io.github.m0pt0pmatt.spongesurvivalgames.task.Task;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.util.TextMessageException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SurvivalGameStateManager {

    private static final List<Task> READY_TASKS = Collections.emptyList();

    private static final List<Task> START_TASKS = Arrays.asList(
            SetBlocksTask.getInstance(),
            FillChestsTask.getInstance(),
            CreateCageSnapshotsTask.getInstance(),
            SpawnPlayersTask.getInstance(),
            HealPlayersTask.getInstance(),
            SpawnSpectatorsTask.getInstance(),
            CreateCountdownTask.getInstance(),
            CreateScoreboardTask.getInstance(),
            CreateWorldBorderTask.getInstance(),
            StartEventIntervalsTask.getInstance(),
            StartMobSpawnersTask.getInstance()
    );

    private static final List<Task> DEATH_MATCH_TASKS = Arrays.asList(
            CreateCageSnapshotsTask.getInstance(),
            SpawnPlayersTask.getInstance(),
            CreateCountdownTask.getInstance(),
            CreateDeathmatchBorderTask.getInstance()
    );

    private static final List<Task> STOP_TASKS = Arrays.asList(
            DespawnPlayersTask.getInstance(),
            HealPlayersTask.getInstance(),
            ClearScoreBoardTask.getInstance(),
            ClearWorldBorderTask.getInstance(),
            ClearPlayersTask.getInstance(),
            StopMobSpawnersTask.getInstance(),
            StopEventIntervalsTask.getInstance()
    );

    public static void ready(SurvivalGame survivalGame) {
        checkConfig(survivalGame.getConfig());
        try {

            SurvivalGameState oldState = survivalGame.getState();
            executeTasks(READY_TASKS, survivalGame);
            survivalGame.state = SurvivalGameState.JOINABLE;
            survivalGame.runningState = SurvivalGameRunningState.STOPPED;
            Sponge.getEventManager().post(new GameStateChangedEvent(survivalGame, oldState, SurvivalGameState.JOINABLE));
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
            Sponge.getEventManager().post(new GameStateChangedEvent(survivalGame, oldState, SurvivalGameState.RUNNING));
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
            Sponge.getEventManager().post(new GameStateChangedEvent(survivalGame, oldState, SurvivalGameState.STOPPED));
        } catch (TextMessageException e) {
            e.printStackTrace();
        }
    }

    private static void checkConfig(GameConfig config) {

        String worldName = config.getWorldName()
                .orElseThrow(() -> new IllegalArgumentException("World name is not set."));

        if (!Sponge.getServer().getWorld(worldName).isPresent()) {
            throw new IllegalArgumentException("World " + worldName + " does not exist.");
        }

        if (!config.getBlockArea().getLesserBoundary().isPresent() || !config.getBlockArea().getGreaterBoundary().isPresent()) {
            throw new IllegalArgumentException("Boundaries are not set.");
        }

        String exitWorldName = config.getExitWorldName()
                .orElseThrow(() -> new IllegalArgumentException("Exit world name is not set."));

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

        if (config.getSpawnPoints().isEmpty()) {
            throw new IllegalArgumentException("No spawn points set.");
        }
    }

    private static void executeTasks(List<Task> tasks, SurvivalGame survivalGame) throws TextMessageException {
        for (Task task : tasks) {
            task.execute(survivalGame);
        }
    }

}
