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

import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.value.mutable.MutableBoundedValue;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameMode;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public final class PlayerRestorer {

    private final Value<Text> displayNameData;
    private final List<ItemStack> enderChestInventory;
    private final Value<GameMode> gameModeData;
    private final Scoreboard scoreboard;

    private final MutableBoundedValue<Integer> foodLevel;
    private final MutableBoundedValue<Double> exhaustion;
    private final MutableBoundedValue<Double> saturation;

    private final List<ItemStack> inventory;

    private final MutableBoundedValue<Double> health;
    private final MutableBoundedValue<Double> maxHealth;

    private final Vector3d headRotation;

    private final Entity vehicle;
    private final List<Entity> passengers;
    private final Vector3d rotation;
    private final Vector3d velocity;

    private final Location<World> location;

    public PlayerRestorer(Player player) {
        displayNameData = player.getDisplayNameData().displayName().copy();
        enderChestInventory = inventory(player.getEnderChestInventory());
        gameModeData = player.getGameModeData().type().copy();
        scoreboard = player.getScoreboard();
        foodLevel = player.getFoodData().foodLevel();
        exhaustion = player.getFoodData().exhaustion();
        saturation = player.getFoodData().saturation();
        inventory = inventory(player.getInventory());
        health = player.getHealthData().health().copy();
        maxHealth = player.getHealthData().maxHealth().copy();
        headRotation = player.getHeadRotation().clone();
        vehicle = player.getVehicle().orElse(null);
        passengers = player.getPassengers();
        rotation = player.getRotation().clone();
        velocity = player.getVelocity().clone();
        location = player.getLocation();
    }

    private ArrayList<ItemStack> inventory(Inventory inventory) {
        ArrayList<ItemStack> items = new ArrayList<>(inventory.size());
        inventory.slots().forEach(i -> items.add(i.peek().orElse(null)));
        inventory.clear();
        return items;
    }

    private void restoreInventory(Inventory inventory, List<ItemStack> items) {
        inventory.clear();
        Iterator<ItemStack> ii = items.iterator();
        Iterator<Inventory> si = inventory.slots().iterator();
        while (ii.hasNext() && si.hasNext()) {
            ItemStack item = ii.next();
            Inventory slot = si.next();
            if (item != null) {
                slot.set(item);
            }
        }
    }

    public void restore(Player player) {

        player.offer(Keys.DISPLAY_NAME, displayNameData.get());
        restoreInventory(player.getEnderChestInventory(), enderChestInventory);
        player.offer(Keys.GAME_MODE, gameModeData.get());

        player.setScoreboard(scoreboard);

        player.offer(Keys.FOOD_LEVEL, foodLevel.get());
        player.offer(Keys.EXHAUSTION, exhaustion.get());
        player.offer(Keys.SATURATION, saturation.get());

        restoreInventory(player.getInventory(), inventory);

        player.offer(Keys.HEALTH, health.get());
        player.offer(Keys.MAX_HEALTH, maxHealth.get());

        player.setHeadRotation(headRotation);
        Optional.ofNullable(vehicle).ifPresent(player::setVehicle);
        if (passengers.size() > 0) {
            player.clearPassengers();
            passengers.forEach(player::addPassenger);
        }
        player.setRotation(rotation);
        player.setVelocity(velocity);
        player.setLocation(location);
    }
}
