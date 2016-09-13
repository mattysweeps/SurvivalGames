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
package io.github.m0pt0pmatt.spongesurvivalgames.listener;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.concurrent.TimeUnit;

import io.github.m0pt0pmatt.spongesurvivalgames.SpongeSurvivalGamesPlugin;
import io.github.m0pt0pmatt.spongesurvivalgames.game.SurvivalGame;
import io.github.m0pt0pmatt.spongesurvivalgames.game.SurvivalGameRepository;
import io.github.m0pt0pmatt.spongesurvivalgames.game.WinChecker;

public class PlayerDeathListener {

    private static final PlayerDeathListener INSTANCE = new PlayerDeathListener();

    @Listener
    public void onPlayerDamage(DamageEntityEvent event) {

        Entity entity = event.getTargetEntity();
        if (!(entity instanceof Player)) {
            return;
        }

        Player player = (Player) entity;

        if (!event.willCauseDeath()) {
            return;
        }

        for (SurvivalGame survivalGame: SurvivalGameRepository.values()) {
            if (survivalGame.getPlayerUUIDs().contains(player.getUniqueId())) {

                SpongeSurvivalGamesPlugin.EXECUTOR.schedule(() -> {
                    performDeadPlayer(survivalGame, player);
                }, 10, TimeUnit.MILLISECONDS);

                event.setCancelled(true);
            }
        }
    }

    private static void performDeadPlayer(SurvivalGame survivalGame, Player player){

        player.setLocation(new Location<World>(Sponge.getServer().getWorld(survivalGame.getConfig().getWorldName().get()).get(),
                survivalGame.getConfig().getExitVector().get()));
        survivalGame.getPlayerUUIDs().remove(player.getUniqueId());
        WinChecker.checkWin(survivalGame);
    }

    public static PlayerDeathListener getInstance() {
        return INSTANCE;
    }
}
