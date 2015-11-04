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
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.EmptyLootGeneratorException;
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.TaskException;
import io.github.m0pt0pmatt.spongesurvivalgames.loot.Loot;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

/**
 * Task for filling the chests with random loot
 */
public class FillChestsTask implements SurvivalGameTask {
    @Override
    public void execute(SurvivalGame game) throws TaskException {
        String worldName = game.getWorldName().get();
        World world = Bukkit.getServer().getWorld(worldName);

        int xmin = game.getConfig().getXMin().get();
        int xmax = game.getConfig().getXMax().get();
        int ymin = game.getConfig().getYMin().get();
        int ymax = game.getConfig().getYMax().get();
        int zmin = game.getConfig().getZMin().get();
        int zmax = game.getConfig().getZMax().get();

        Collection<Block> chests = new ArrayList<>();
        for (int x = xmin; x < xmax; x++) {
            for (int y = ymin; y < ymax; y++) {
                for (int z = zmin; z < zmax; z++) {
                    Block block = world.getBlockAt(x, y, z);
                    if (block.getState() instanceof Chest) {
                        chests.add(block);
                    }
                }
            }
        }

        chests.forEach(block -> {
                    final Random random = new Random();
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

                        try {
                            Loot item = game.getLootGenerator().generate();
                            if (item != null) inventory.addItem(item.getItem());
                        } catch (EmptyLootGeneratorException e) {
                        }

                    }

                    chest.update();
                }
        );
    }
}
