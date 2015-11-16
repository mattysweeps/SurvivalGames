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
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.SurvivalGameException;
import io.github.m0pt0pmatt.spongesurvivalgames.util.Title;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

class CheckWinTask implements SurvivalGameTask {
    @Override
    public boolean execute(SurvivalGame game) {

        if (game.getPlayerUUIDs().size() > 1) {
            return true;
        }

        UUID winnerUUID = game.getPlayerUUIDs().stream().findFirst().get();
        Player winner = Bukkit.getServer().getPlayer(winnerUUID);
        if (winner != null) {
            Bukkit.getScheduler().runTaskLater(
                    BukkitSurvivalGamesPlugin.plugin,
                    () -> {
                        if (winner.isOnline()) winner.teleport(game.getExitLocation().get());
                    },
                    200);
            winner.sendMessage("Congratulations! You won!");
            Title.displayTitle(winner, "You Win!", "", ChatColor.DARK_GREEN, ChatColor.MAGIC, 30, 100);
        }

        Bukkit.getScheduler().runTaskLater(
                BukkitSurvivalGamesPlugin.plugin,
                () -> {
                    try {
                        game.stop();
                    } catch (SurvivalGameException e) {
                        Bukkit.getLogger().warning(e.getDescription());
                    }
                },
                200);
        return true;
    }
}
