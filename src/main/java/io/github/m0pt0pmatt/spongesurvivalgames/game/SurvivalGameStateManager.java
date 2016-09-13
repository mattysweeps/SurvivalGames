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

import org.spongepowered.api.Sponge;
import org.spongepowered.api.util.TextMessageException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.github.m0pt0pmatt.spongesurvivalgames.config.SurvivalGameConfig;
import io.github.m0pt0pmatt.spongesurvivalgames.task.ClearPlayersTask;
import io.github.m0pt0pmatt.spongesurvivalgames.task.ClearScoreBoardTask;
import io.github.m0pt0pmatt.spongesurvivalgames.task.ClearWorldBorderTask;
import io.github.m0pt0pmatt.spongesurvivalgames.task.CreateCageSnapshotsTask;
import io.github.m0pt0pmatt.spongesurvivalgames.task.CreateCountdownTask;
import io.github.m0pt0pmatt.spongesurvivalgames.task.CreateDeathmatBorderTask;
import io.github.m0pt0pmatt.spongesurvivalgames.task.CreateScoreboardTask;
import io.github.m0pt0pmatt.spongesurvivalgames.task.CreateWorldBorderTask;
import io.github.m0pt0pmatt.spongesurvivalgames.task.DespawnPlayersTask;
import io.github.m0pt0pmatt.spongesurvivalgames.task.FillChestsTask;
import io.github.m0pt0pmatt.spongesurvivalgames.task.ReadyPlayerTask;
import io.github.m0pt0pmatt.spongesurvivalgames.task.ReadySpectatorsTask;
import io.github.m0pt0pmatt.spongesurvivalgames.task.RotatePlayersTask;
import io.github.m0pt0pmatt.spongesurvivalgames.task.SpawnPlayersTask;
import io.github.m0pt0pmatt.spongesurvivalgames.task.SpawnSpectatorsTask;
import io.github.m0pt0pmatt.spongesurvivalgames.task.Task;

public class SurvivalGameStateManager {

    private static final List<Task> READY_TASKS = Collections.singletonList(FillChestsTask.getInstance());

    private static final List<Task> START_TASKS = Arrays.asList(
            CreateCageSnapshotsTask.getInstance(),
            SpawnPlayersTask.getInstance(),
            RotatePlayersTask.getInstance(),
            ReadyPlayerTask.getInstance(),
            SpawnSpectatorsTask.getInstance(),
            ReadySpectatorsTask.getInstance(),
            CreateCountdownTask.getInstance(),
            CreateScoreboardTask.getInstance(),
            CreateWorldBorderTask.getInstance()
            );

    private static final List<Task> DEATH_MATCH_TASKS = Arrays.asList(
            CreateCageSnapshotsTask.getInstance(),
            SpawnPlayersTask.getInstance(),
            RotatePlayersTask.getInstance(),
            CreateCountdownTask.getInstance(),
            CreateDeathmatBorderTask.getInstance()
    );

    private static final List<Task> STOP_TASKS = Arrays.asList(
            DespawnPlayersTask.getInstance(),
            ClearScoreBoardTask.getInstance(),
            ClearWorldBorderTask.getInstance(),
            ClearPlayersTask.getInstance()
    );

    public static void ready(SurvivalGame survivalGame) {
        checkConfig(survivalGame.getConfig());
        try {
            executeTasks(READY_TASKS, survivalGame);
            survivalGame.state = SurvivalGameState.JOINABLE;
            survivalGame.runningState = SurvivalGameRunningState.STOPPED;
        } catch (TextMessageException e) {
            e.printStackTrace();
        }
    }

    public static void start(SurvivalGame survivalGame) {
        checkConfig(survivalGame.getConfig());
        try {
            executeTasks(START_TASKS, survivalGame);
            survivalGame.state = SurvivalGameState.RUNNING;
            survivalGame.runningState = SurvivalGameRunningState.IN_PROGRESS;
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
            executeTasks(STOP_TASKS, survivalGame);
            survivalGame.state = SurvivalGameState.STOPPED;
            survivalGame.runningState = SurvivalGameRunningState.STOPPED;
        } catch (TextMessageException e) {
            e.printStackTrace();
        }
    }

    private static void checkConfig(SurvivalGameConfig config) {

        String worldName = config.getWorldName()
                .orElseThrow(() -> new IllegalArgumentException("World name is not set."));

        if (!Sponge.getServer().getWorld(worldName).isPresent()) {
            throw new IllegalArgumentException("World " + worldName + " does not exist.");
        }

        if (!config.getLesserBoundary().isPresent() || !config.getGreaterBoundary().isPresent()) {
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
        for (Task task: tasks) {
            task.execute(survivalGame);
        }
    }

}
