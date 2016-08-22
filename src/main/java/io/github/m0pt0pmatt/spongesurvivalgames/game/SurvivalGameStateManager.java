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

    public SurvivalGameStateManager() {
        state = SurvivalGameState.STOPPED;
    }

    public void ready() {
        if (state == SurvivalGameState.STOPPED) {
            state = SurvivalGameState.READY;
        }
    }

    public void stop() {
        state = SurvivalGameState.STOPPED;
    }

    public void run() {
        if (state == SurvivalGameState.READY) {
            state = SurvivalGameState.RUNNING;
        }
    }

    public void deathMatch() {
        if (state == SurvivalGameState.RUNNING) {
            state = SurvivalGameState.DEATHMATCH;
        }
    }

    /**
     * Enumeration of the three states of a game.
     * A game is always in one of these states.
     */
    public enum SurvivalGameState {
        STOPPED,
        READY,
        RUNNING,
        DEATHMATCH
    }
}
