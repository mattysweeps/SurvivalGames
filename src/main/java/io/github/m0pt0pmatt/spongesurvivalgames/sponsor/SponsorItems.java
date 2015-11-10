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

package io.github.m0pt0pmatt.spongesurvivalgames.sponsor;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

final class SponsorItems {

    public static final ItemStack LIGHTARMOR = new ItemStack(Material.LEATHER_CHESTPLATE);
    public static final ItemStack MEDIUMARMOR = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
    public static final ItemStack LIGHTSWORD = new ItemStack(Material.WOOD_SWORD);
    public static final ItemStack MEDIUMSWORD = new ItemStack(Material.IRON_SWORD);
    public static final ItemStack BOW = new ItemStack(Material.BOW);
    public static final ItemStack HEALTHPOTION = new ItemStack(Material.POTION, 1, (short) 8229);
    public static final ItemStack POISONPOTION = new ItemStack(Material.POTION, 1, (short) 16388);
    public static final ItemStack FOOD1 = new ItemStack(Material.CARROT_ITEM, 5);
    public static final ItemStack FOOD2 = new ItemStack(Material.BREAD, 5);
    public static final ItemStack FOOD3 = new ItemStack(Material.COOKED_CHICKEN, 5);
    public static final ItemStack RESTOREHUNGER = custom(new ItemStack(Material.APPLE), "Restore Hunger", Enchantment.ARROW_INFINITE, 1);
    public static final ItemStack RESTOREHEALTH = custom(new ItemStack(Material.GOLDEN_APPLE), "Restore Health", Enchantment.ARROW_INFINITE, 1);

    private static ItemStack custom(ItemStack item, String name, Enchantment enchantment, int level) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.addEnchant(enchantment, level, true);
        item.setItemMeta(meta);
        return item;
    }
}
