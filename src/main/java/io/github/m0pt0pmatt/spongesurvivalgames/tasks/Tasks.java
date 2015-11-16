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

package io.github.m0pt0pmatt.spongesurvivalgames.tasks;

public class Tasks {
    public static final SurvivalGameTask CHECK_WIN = new CheckWinTask();
    public static final SurvivalGameTask CLEAR_PLAYERS = new ClearPlayersTask();
    public static final SurvivalGameTask CLEAR_WORLD_BORDER = new ClearWorldBorderTask();
    public static final SurvivalGameTask CLEAR_SCOREBOARD = new DeleteScoreboardTask();
    public static final SurvivalGameTask CREATE_WORLD_BORDER = new CreateWorldBorderTask();
    public static final SurvivalGameTask CREATE_CAGE = new CreateCageSnapshotsTask();
    public static final SurvivalGameTask CREATE_COUNTDOWN = new CreateCountdownTask();
    public static final SurvivalGameTask CREATE_DEATHMATCH_BORDER = new CreateDeathmatchBorderTask();
    public static final SurvivalGameTask CREATE_SCOREBOARD = new CreateScoreboardTask();
    public static final SurvivalGameTask DESPAWN_PLAYERS = new DespawnPlayersTask();
    public static final SurvivalGameTask FILL_CHESTS = new FillChestsTask();
    public static final SurvivalGameTask READY_PLAYERS = new ReadyPlayerTask();
    public static final SurvivalGameTask READY_SPECTATORS = new ReadySpectatorsTask();
    public static final SurvivalGameTask RESET_LOOT_GENERATOR = new ResetLootGeneratorTask();
    public static final SurvivalGameTask ROTATE_PLAYERS = new RotatePlayersTask();
    public static final SurvivalGameTask SPAWN_PLAYERS = new SpawnPlayersTask();
    public static final SurvivalGameTask SPAWN_SPECTATORS = new SpawnSpectatorsTask();
}
