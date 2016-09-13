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

import com.flowpowered.math.vector.Vector3i;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.Text;

import java.util.Optional;
import java.util.UUID;

public class WinChecker {

    private static final Text WINNER_MESSAGE = Text.of("Congratulations! You're the winner!");

    private WinChecker() {

    }

    public static void checkWin(SurvivalGame survivalGame) {
        if (survivalGame.getPlayerUUIDs().size() > 1) {
            return;
        } else if (survivalGame.getPlayerUUIDs().size() == 1) {

            UUID winnerId = survivalGame.getPlayerUUIDs().iterator().next();
            Sponge.getServer().getPlayer(winnerId).ifPresent(winner -> {
                winner.sendMessage(WINNER_MESSAGE);
                Optional<String> exitWorldName = survivalGame.getConfig().getExitWorldName();
                Optional<Vector3i> exitVector = survivalGame.getConfig().getExitVector();
                if (exitWorldName.isPresent() && exitVector.isPresent()) {
                    Sponge.getServer().getWorld(exitWorldName.get())
                            .ifPresent(world -> winner.setLocation(world.getLocation(exitVector.get())));
                }
            });
        }

        SurvivalGameStateManager.stop(survivalGame);
    }
}
