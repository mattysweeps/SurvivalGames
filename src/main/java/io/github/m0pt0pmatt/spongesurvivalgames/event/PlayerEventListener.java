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

package io.github.m0pt0pmatt.spongesurvivalgames.event;

import io.github.m0pt0pmatt.spongesurvivalgames.SpongeSurvivalGamesPlugin;
import io.github.m0pt0pmatt.spongesurvivalgames.SurvivalGameState;
import io.github.m0pt0pmatt.spongesurvivalgames.game.SurvivalGameRepository;

import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.entity.living.humanoid.player.KickPlayerEvent;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.event.network.ClientConnectionEvent;

/**
 * Listener class for the plugin.
 */
public class PlayerEventListener {

    private static final PlayerEventListener INSTANCE = new PlayerEventListener();

    private PlayerEventListener() {
    }

    public static PlayerEventListener getInstance(){
        return INSTANCE;
    }

    @Listener
    public void onPlayerStupid(DamageEntityEvent event) {

        if (!(event.getTargetEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getTargetEntity();

        long count = SurvivalGameRepository.values().stream()
            .filter(g -> g.getState().equals(SurvivalGameState.RUNNING) || g.getState().equals(SurvivalGameState.DEATHMATCH))
            .filter(g -> g.getPlayerUUIDs().contains(player.getUniqueId()))
            .count();

        if (count < 1) {
            event.setCancelled(true);
        }
    }

    @Listener
    public void onPlayerDeath(DestructEntityEvent event, @Getter("getTargetEntity") Entity player) {
        if (player instanceof Player)
        reportDeath((Player) player);
    }

    @Listener
    public void onPlayerDisconnect(ClientConnectionEvent.Disconnect event) {
        reportDeath(event.getTargetEntity());
    }

    @Listener
    public void event(KickPlayerEvent event) {
        reportDeath(event.getTargetEntity());
    }

    private static void reportDeath(Player player) {
        SurvivalGameRepository.values().stream()
            .filter(g -> g.getPlayerUUIDs().contains(player.getUniqueId()))
            .forEach(g -> g.reportDeath(player.getUniqueId()));
    }
}
