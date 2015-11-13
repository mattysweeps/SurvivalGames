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

import io.github.m0pt0pmatt.spongesurvivalgames.SurvivalGame;
import io.github.m0pt0pmatt.spongesurvivalgames.loot.Loot;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;

import java.util.Optional;
import java.util.Random;

/**
 * Task for filling the chests with random loot
 */
class FillChestsTask implements SurvivalGameTask {

    @Override
    public boolean execute(SurvivalGame game) {

        String worldName = game.getWorldName().get();
        World world = Bukkit.getServer().getWorld(worldName);
        if (world == null) return false;

        game.getConfig().getChestLocations().forEach(chestVector ->

                {
                    final Random random = new Random();

                    Block block = world.getBlockAt(chestVector.getBlockX(), chestVector.getBlockY(), chestVector.getBlockZ());
                    if (block.getState() instanceof Chest) {
                        Chest chest = (Chest) block.getState();

                        Inventory inventory = chest.getBlockInventory();
                        inventory.clear();
                        double itemCount = (
                                game.getChestMidpoint().get() +
                                        (
                                                (random.nextDouble() * game.getChestRange().get())
                                                        * (random.nextDouble() > 0.5 ? 1 : -1)
                                        )
                        );
                        for (int i = 0; i < itemCount; i++) {
                            Optional<Loot> item = game.getLootGenerator().generate();
                            if (item.isPresent()) inventory.addItem(item.get().getItem());

                        }
                        chest.update();
                    } else
                        System.out.println("Unable to locate chest at " + chestVector.getBlockX() + ", " + chestVector.getBlockY() + ", " + chestVector.getBlockZ());

                }


        );

        game.setChestsFilled();
        Bukkit.getLogger().info("Chests have finished populating");
        return true;
    }


}
