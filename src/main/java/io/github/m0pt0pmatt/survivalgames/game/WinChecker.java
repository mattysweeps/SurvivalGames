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

import static io.github.m0pt0pmatt.survivalgames.Util.getOrThrow;

import io.github.m0pt0pmatt.survivalgames.task.DelayedTask;
import io.github.m0pt0pmatt.survivalgames.task.player.CelebrateWinnerTask;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.util.TextMessageException;

/** Checks for win conditions for a Survival Game */
public class WinChecker {

    private static final long CELEBRATE_DELAY_SECONDS = 5;

    private WinChecker() {}

    public static void checkWin(SurvivalGame survivalGame) throws TextMessageException {
        if (survivalGame.getPlayerUUIDs().size() <= 1) {

            // If there is a winner
            if (survivalGame.getPlayerUUIDs().size() == 1) {
                UUID winnerId = survivalGame.getPlayerUUIDs().iterator().next();
                Player winner = getOrThrow(Sponge.getServer().getPlayer(winnerId), "winner");
                CelebrateWinnerTask.getInstance().execute(survivalGame, winner);
            }

            // Stop the game
            DelayedTask.of(
                            SurvivalGameStateManager::stop,
                            CELEBRATE_DELAY_SECONDS,
                            TimeUnit.SECONDS)
                    .execute(survivalGame);
        }
    }
}
