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

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OpenInventorySponsor implements Sponsor {

    private final List<ItemStack> items;

    public OpenInventorySponsor(List<ItemStack> items) {
        this.items = new ArrayList<>();
        this.items.addAll(items);
    }

    @Override
    public void execute(Player player) {

        List<ItemStack> copyList = new ArrayList<>(items);
        int originalSize = copyList.size();

        for (Iterator<ItemStack> i = copyList.iterator(); i.hasNext(); i.remove()){
            if (player.getInventory().firstEmpty() != -1){
                player.getInventory().all(i.next());
            }
        }

        if (originalSize > copyList.size()){
            player.sendMessage("Items were put in your inventory!");
        }

        if (copyList.size() > 0){
            Inventory inventory = Bukkit.createInventory(player, InventoryType.CHEST);
            items.forEach(inventory::addItem);
            player.openInventory(inventory);
        }

        player.playSound(player.getLocation(), Sound.LEVEL_UP, (float) 1.0, (float) 1.75);
    }
}
