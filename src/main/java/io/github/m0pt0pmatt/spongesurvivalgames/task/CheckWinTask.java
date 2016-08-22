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

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.Color;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.github.m0pt0pmatt.spongesurvivalgames.SpongeSurvivalGamesPlugin;
import io.github.m0pt0pmatt.spongesurvivalgames.exception.SurvivalGameException;
import io.github.m0pt0pmatt.spongesurvivalgames.game.SurvivalGame;

import static io.github.m0pt0pmatt.spongesurvivalgames.SpongeSurvivalGamesPlugin.LOGGER;

class CheckWinTask implements SurvivalGameTask {
    @Override
    public boolean execute(SurvivalGame game) {

        if (game.getPlayerUUIDs().size() > 1) {
            return true;
        }

        UUID winnerUUID = game.getPlayerUUIDs().stream().findFirst().get();
        Optional<Player> winner = Sponge.getServer().getPlayer(winnerUUID);
        if (winner.isPresent()) {

            SpongeSurvivalGamesPlugin.executorService.schedule(
                () -> {
                    if (winner.get().isOnline()) winner.get().setLocation(game.getExitLocation().get());
                },
                200,
                TimeUnit.MILLISECONDS
            );

            winner.get().sendMessage(Text.of("Congratulations! You won!"));
            //InternalTitle.displayTitle(winner.get(), "You Win!", "", Color.DARK_GREEN, Color.RED, 30, 100);
        }

        SpongeSurvivalGamesPlugin.executorService.schedule(
            () -> {
                try {
                    game.stop();
                } catch (SurvivalGameException e) {
                    LOGGER.error("unable to stop");
                }
            },
            200,
            TimeUnit.MILLISECONDS);

        return true;
    }
}
