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
import io.github.m0pt0pmatt.spongesurvivalgames.util.InternalTitle;
import io.github.m0pt0pmatt.spongesurvivalgames.util.Util;

import org.spongepowered.api.effect.sound.SoundCategories;
import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.util.Color;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Task for counting down the starting time
 */
class CreateCountdownTask implements SurvivalGameTask {

    @Override
    public boolean execute(SurvivalGame game) {

        Set<UUID> everyone = new HashSet<>();
        everyone.addAll(game.getPlayerUUIDs());
        everyone.addAll(game.getSpectatorUUIDs());

        Util.getPlayers(everyone).forEach(player -> {
            for (int i = game.getCountdownTime().get(); i > 0; i--) {
                final int j = i;

                SpongeSurvivalGamesPlugin.executorService.schedule(
                    () -> {
                        InternalTitle.displayTitle(player, j + "", j < 4 ? Color.RED : Color.DARK_GREEN);
                        player.playSound(SoundTypes.BLOCK_NOTE_PLING, SoundCategories.BLOCK, player.getLocation().getPosition(), 1);
                    },
                    (game.getCountdownTime().get() - i),
                    TimeUnit.SECONDS
                );
            }

            SpongeSurvivalGamesPlugin.executorService.schedule(
                () -> {
                    InternalTitle.displayTitle(player, "Go!", Color.RED);
                    player.playSound(SoundTypes.BLOCK_NOTE_PLING, SoundCategories.BLOCK, player.getLocation().getPosition(), 1);
                    new CheckWinTask().execute(game);
                },
                game.getCountdownTime().get(),
                TimeUnit.SECONDS
            );
        });
        return true;
    }
}
