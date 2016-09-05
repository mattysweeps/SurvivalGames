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

public class SurvivalGameStateManager {

    private SurvivalGameState state;
    private SurvivalGameRunningState runningState;

    public SurvivalGameStateManager() {
        state = SurvivalGameState.EDITING;
        runningState = SurvivalGameRunningState.STOPPED;
    }

    public SurvivalGameState getState() {
        return state;
    }

    public SurvivalGameRunningState getRunningState() {
        return runningState;
    }

    public void ready() {
        if (state == SurvivalGameState.EDITING) {
            state = SurvivalGameState.JOINABLE;
        }
    }

    public void stop() {
        state = SurvivalGameState.EDITING;
        runningState = SurvivalGameRunningState.STOPPED;
    }

    public void run() {
        if (state == SurvivalGameState.JOINABLE) {
            state = SurvivalGameState.RUNNING;
            runningState = SurvivalGameRunningState.IN_PROGRESS;
        }
    }

    public void deathMatch() {
        if (state == SurvivalGameState.RUNNING) {
            runningState = SurvivalGameRunningState.DEATH_MATCH;
        }
    }
}
