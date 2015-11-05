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

package io.github.m0pt0pmatt.spongesurvivalgames.loot;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Wrapper class for Loot which houses information about it's weight and the item it's storing
 *
 * @author Skyler
 */
public class Loot implements ConfigurationSerializable {

    private ItemStack item;
    private double weight;

    public static void registerAliases() {
        //Register this serializable class with some aliases too
        ConfigurationSerialization.registerClass(Loot.class);
        ConfigurationSerialization.registerClass(Loot.class, "loot");
        ConfigurationSerialization.registerClass(Loot.class, "LOOT");
    }

    private Loot() {
        this.weight = 0;
        this.item = null;
    }

    public Loot(ItemStack item, double weight) {
        this.item = item;
        this.weight = weight;
    }

    /**
     * Uses the passed configuration map to instantiate a new piece of loot.
     *
     * @param configMap
     * @return
     */
    public static Loot valueOf(Map<String, Object> configMap) {
        Loot loot = new Loot();
        loot.weight = (double) configMap.get("weight");
        loot.item = (ItemStack) configMap.get("item");

        return loot;
    }

    /**
     * Serializes the wrapped loot information to a format that's able to be saved to a configuration file.
     */
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("weight", weight);
        map.put("item", item);

        return map;
    }

    public ItemStack getItem() {
        return item;
    }

    public double getWeight() {
        return weight;
    }

}
