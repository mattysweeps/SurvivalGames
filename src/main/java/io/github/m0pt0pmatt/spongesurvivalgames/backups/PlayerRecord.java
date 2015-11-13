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

package io.github.m0pt0pmatt.spongesurvivalgames.backups;

import com.google.common.collect.Lists;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Internal helper class to help keep together player information
 *
 * @author Skyler
 */
public class PlayerRecord implements ConfigurationSerializable {

    public static void registerAliases() {
        //Register this serializable class with some aliases too
        ConfigurationSerialization.registerClass(PlayerRecord.class);
        ConfigurationSerialization.registerClass(PlayerRecord.class, "backup");
        ConfigurationSerialization.registerClass(PlayerRecord.class, "BACKUP");
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();

        map.put("inventory", Lists.newArrayList(inventory));
        map.put("armor", Lists.newArrayList(armor));
        map.put("holderID", id.toString());
        map.put("health", playerHealth);
        map.put("maxHealth", playerMaxHealth);

        map.put("worldname", location.getWorld().getName());
        map.put("pos", new Vector(location.getX(), location.getY(), location.getZ()));

        return map;
    }

    private UUID id;

    private final ItemStack[] inventory;

    private final ItemStack[] armor;

    private final double playerHealth;

    private final double playerMaxHealth;

    private final Location location;

    public PlayerRecord(Player player) {
        id = player.getUniqueId();
        inventory = player.getInventory().getContents();
        armor = player.getInventory().getArmorContents();
        playerHealth = player.getHealth();
        playerMaxHealth = player.getMaxHealth();
        location = player.getLocation();
    }

    private PlayerRecord(ItemStack[] inventory, ItemStack[] armor, double health, double maxHealth, Location location) {
        this.inventory = inventory;
        this.armor = armor;
        playerHealth = health;
        playerMaxHealth = maxHealth;
        this.location = location;
    }

    public ItemStack[] getPlayerInventory() {
        return inventory;
    }

    public ItemStack[] getPlayerArmor() {
        return armor;
    }

    public double getPlayerHealth() {
        return playerHealth;
    }

    public double getPlayerMaxHealth() {
        return playerMaxHealth;
    }

    public Location getLocation() {
        return location;
    }


}