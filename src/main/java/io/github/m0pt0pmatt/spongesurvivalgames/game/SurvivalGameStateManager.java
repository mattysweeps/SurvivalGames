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
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.text.Text;

import java.util.Optional;

import io.github.m0pt0pmatt.spongesurvivalgames.config.SurvivalGameConfig;

public class SurvivalGameStateManager {

    public static void ready(SurvivalGame survivalGame) {
        checkConfig(survivalGame.getConfig());
        survivalGame.state = SurvivalGameState.JOINABLE;
        survivalGame.runningState = SurvivalGameRunningState.STOPPED;
    }

    public static void stop(SurvivalGame survivalGame) {
        survivalGame.state = SurvivalGameState.STOPPED;
        survivalGame.runningState = SurvivalGameRunningState.STOPPED;
    }

    public static void start(SurvivalGame survivalGame) {
        checkConfig(survivalGame.getConfig());
        survivalGame.state = SurvivalGameState.RUNNING;
        survivalGame.runningState = SurvivalGameRunningState.IN_PROGRESS;
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

        if (config.getSpawns().isEmpty()) {
            throw new IllegalArgumentException("No spawn points set.");
        }
    }

    public static void deathMatch(SurvivalGame survivalGame) {
        survivalGame.state = SurvivalGameState.RUNNING;
        survivalGame.runningState = SurvivalGameRunningState.DEATH_MATCH;
    }
}
