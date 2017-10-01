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
package io.github.m0pt0pmatt.survivalgames.listener;

import io.github.m0pt0pmatt.survivalgames.event.PlayerDeathEvent;
import io.github.m0pt0pmatt.survivalgames.game.SurvivalGame;
import io.github.m0pt0pmatt.survivalgames.game.SurvivalGameRepository;
import io.github.m0pt0pmatt.survivalgames.game.WinChecker;
import io.github.m0pt0pmatt.survivalgames.task.UpdateScoreBoardTask;
import io.github.m0pt0pmatt.survivalgames.task.player.DespawnPlayersTask;
import io.github.m0pt0pmatt.survivalgames.task.player.HealPlayersTask;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.entity.damage.source.EntityDamageSource;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.util.TextMessageException;
import org.spongepowered.api.world.World;

public class PlayerDeathListener {

    private static final PlayerDeathListener INSTANCE = new PlayerDeathListener();

    private PlayerDeathListener() {}

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

        for (SurvivalGame survivalGame : SurvivalGameRepository.values()) {
            if (survivalGame.getPlayerUUIDs().contains(player.getUniqueId())) {
                performDeadPlayer(survivalGame, player, event.getCause());
                event.setCancelled(true);
                break;
            }
        }
    }

    private static void performDeadPlayer(SurvivalGame survivalGame, Player player, Cause cause) {

        // Get the player who killed the dead player.
        Player killingPlayer =
                (Player)
                        cause.first(EntityDamageSource.class)
                                .map(EntityDamageSource::getSource)
                                .filter(e -> e instanceof Player)
                                .orElse(null);

        // Update the scoreboard
        try {
            UpdateScoreBoardTask.getInstance().update(survivalGame, player, killingPlayer);
        } catch (TextMessageException e) {
            e.printStackTrace();
        }

        // Drop the dead players inventory
        World world = player.getWorld();
        Inventory inventory = player.getInventory();
        ItemStack item;
        while ((item = inventory.poll().orElse(null)) != null) {
            Entity entity =
                    world.createEntity(EntityTypes.ITEM, player.getLocation().getPosition());
            entity.offer(Keys.REPRESENTED_ITEM, item.createSnapshot());
            world.spawnEntity(entity);
        }

        // Despawn the player
        try {
            DespawnPlayersTask.getInstance().execute(survivalGame, player);
        } catch (TextMessageException e) {
            e.printStackTrace();
        }

        // Heal the player
        try {
            HealPlayersTask.getInstance().execute(survivalGame, player);
        } catch (TextMessageException e) {
            e.printStackTrace();
        }

        // Remove the player from the game
        survivalGame.getPlayerUUIDs().remove(player.getUniqueId());

        // Post the death event
        Sponge.getEventManager().post(new PlayerDeathEvent(cause, survivalGame, player));

        // Check for a win condition.
        try {
            WinChecker.checkWin(survivalGame);
        } catch (TextMessageException e) {
            e.printStackTrace();
        }
    }

    public static PlayerDeathListener getInstance() {
        return INSTANCE;
    }
}
