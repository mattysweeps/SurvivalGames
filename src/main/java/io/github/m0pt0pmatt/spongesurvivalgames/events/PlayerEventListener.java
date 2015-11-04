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

package io.github.m0pt0pmatt.spongesurvivalgames.events;

import io.github.m0pt0pmatt.spongesurvivalgames.BukkitSurvivalGamesPlugin;
import io.github.m0pt0pmatt.spongesurvivalgames.SurvivalGame;
import io.github.m0pt0pmatt.spongesurvivalgames.SurvivalGameState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;

/**
 * Listener class for the plugin.
 */
public class PlayerEventListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {

        Player player = event.getEntity();

        for (Map.Entry<String, SurvivalGame> game : BukkitSurvivalGamesPlugin.survivalGameMap.entrySet()) {
            if (game.getValue().getState().equals(SurvivalGameState.RUNNING)) {
                if (game.getValue().getWorldName().get().equals(player.getLocation().getWorld().getName())) {
                    //Death has occurred inside the game
                    game.getValue().reportDeath(player.getUniqueId());
                    return;
                }
            }
        }

    }

    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent event) {

        Player player = event.getPlayer();

        for (Map.Entry<String, SurvivalGame> game : BukkitSurvivalGamesPlugin.survivalGameMap.entrySet()) {
            if (game.getValue().getState().equals(SurvivalGameState.RUNNING)) {
                if (game.getValue().getWorldName().get().equals(player.getLocation().getWorld().getName())) {
                    //Player has quit in the middle of a match
                    game.getValue().reportDeath(player.getUniqueId());
                    return;
                }
            }
        }
    }
}
