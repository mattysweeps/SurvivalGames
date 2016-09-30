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
package io.github.m0pt0pmatt.spongesurvivalgames.game;

import io.github.m0pt0pmatt.spongesurvivalgames.Util;
import io.github.m0pt0pmatt.spongesurvivalgames.task.player.CelebrateWinnerTask;
import io.github.m0pt0pmatt.spongesurvivalgames.task.player.DelayedPlayerTask;
import io.github.m0pt0pmatt.spongesurvivalgames.task.player.PlayerTask;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.util.TextMessageException;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/** Checks for win conditions for a Survival Game */
public class WinChecker {

    private static final long CELEBRATE_DELAY_SECONDS = 5;

    private static final List<PlayerTask> WINNER_TASKS = Arrays.asList(
            CelebrateWinnerTask.getInstance(),
            DelayedPlayerTask.of(new PlayerTask() {
                @Override
                public void execute(SurvivalGame survivalGame, Player player) throws TextMessageException {
                    SurvivalGameStateManager.stop(survivalGame);
                }
            }, CELEBRATE_DELAY_SECONDS, TimeUnit.SECONDS));

    private WinChecker() {

    }

    public static void checkWin(SurvivalGame survivalGame) throws TextMessageException {
        if (survivalGame.getPlayerUUIDs().size() == 1) {
            UUID winnerId = survivalGame.getPlayerUUIDs().iterator().next();
            Player winner = Util.getOrThrow(Sponge.getServer().getPlayer(winnerId), "winner");
            for (PlayerTask task : WINNER_TASKS) {
                task.execute(survivalGame, winner);
            }
        }
    }
}
