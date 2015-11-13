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

import io.github.m0pt0pmatt.spongesurvivalgames.BukkitSurvivalGamesPlugin;
import io.github.m0pt0pmatt.spongesurvivalgames.SurvivalGame;
import io.github.m0pt0pmatt.spongesurvivalgames.util.Title;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Task for counting down the starting time
 */
class CreateCountdownTask implements SurvivalGameTask {

    @Override
    public boolean execute(SurvivalGame game) {

        Set<UUID> everyone = new HashSet<>();
        everyone.addAll(game.getPlayerUUIDs());
        everyone.addAll(game.getSpectatorUUIDs());

        BukkitSurvivalGamesPlugin.getPlayers(everyone).forEach(player -> {
            for (int i = game.getCountdownTime().get(); i > 0; i--) {
                final int j = i;

                Bukkit.getScheduler().scheduleSyncDelayedTask(
                        BukkitSurvivalGamesPlugin.plugin,
                        () -> {
                            Title.displayTitle(player, j + "", j < 4 ? ChatColor.DARK_RED : ChatColor.DARK_GREEN
                            );
                            player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
                        },
                        20L * (game.getCountdownTime().get() - i)
                );
            }

            Bukkit.getScheduler().scheduleSyncDelayedTask(
                    BukkitSurvivalGamesPlugin.plugin,
                    () -> {
                        Title.displayTitle(player, "Go!", ChatColor.DARK_RED);
                        player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, (float) 1.5);
                        new CheckWinTask().execute(game);
                    },
                    20L * game.getCountdownTime().get()
            );
        });
        return true;
    }
}
