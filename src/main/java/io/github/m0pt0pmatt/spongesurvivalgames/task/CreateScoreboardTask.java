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

package io.github.m0pt0pmatt.spongesurvivalgames.task;

import io.github.m0pt0pmatt.spongesurvivalgames.SpongeSurvivalGamesPlugin;
import io.github.m0pt0pmatt.spongesurvivalgames.SurvivalGame;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.objective.Objective;
import org.spongepowered.api.statistic.Statistics;

class CreateScoreboardTask implements SurvivalGameTask {
    @Override
    public boolean execute(SurvivalGame game) {
        ScoreboardManager manager = Sponge.getServer().getServerScoreboard().get()
        Scoreboard board = manager.getNewScoreboard();

        Objective objective = board.registerNewObjective("killCount", "playerKillCount");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName("Kills");

        SpongeSurvivalGamesPlugin.getPlayers(game.getPlayerUUIDs()).forEach(
            player -> {
                player.setScoreboard(board);
                player.offer(Keys.STATISTICS, Statistics.PLAYER_KILLS, 0)
                player.setStatistic(Statistic.PLAYER_KILLS, 0);
                objective.getScore(player.getName()).setScore(0);
            }
        );

        return true;
    }
}
