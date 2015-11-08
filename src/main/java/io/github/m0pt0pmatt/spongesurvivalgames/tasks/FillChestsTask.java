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

import io.github.m0pt0pmatt.spongesurvivalgames.BukkitSurvivalGamesPlugin;
import io.github.m0pt0pmatt.spongesurvivalgames.SurvivalGame;
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.SurvivalGameException;
import io.github.m0pt0pmatt.spongesurvivalgames.loot.Loot;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Random;

/**
 * Task for filling the chests with random loot
 */
public class FillChestsTask implements SurvivalGameTask {

    private static final int sleepTime = 500;
    private static final int xlength = 100;
    private static final int ylength = 256;
    private static final int zlength = 100;

    private final Collection<Block> chests = new ArrayList<>();

    @Override
    public void execute(SurvivalGame game) throws SurvivalGameException {
        Bukkit.getScheduler().runTaskAsynchronously(
                BukkitSurvivalGamesPlugin.plugin,
                () -> fillChests(game)
        );
    }

    private int checkBlocks(final int x, final int y, final int z, World world) {
        BukkitTask task = Bukkit.getScheduler().runTask(BukkitSurvivalGamesPlugin.plugin,
                () -> {
                    for (int cx = x; cx < xlength; cx++) {
                        for (int cy = y; cy < ylength; cy++) {
                            for (int cz = z; cz < zlength; cz++) {
                                Block block = world.getBlockAt(cx, cy, cz);
                                if (block.getState() instanceof Chest) {
                                    chests.add(block);
                                }
                            }
                        }
                    }
                }
        );
        return task.getTaskId();
    }

    private void fillChests(SurvivalGame game) {
        String worldName = game.getWorldName().get();
        World world = Bukkit.getServer().getWorld(worldName);

        int xmin = game.getConfig().getXMin().get();
        int xmax = game.getConfig().getXMax().get();
        int ymin = game.getConfig().getYMin().get();
        int ymax = game.getConfig().getYMax().get();
        int zmin = game.getConfig().getZMin().get();
        int zmax = game.getConfig().getZMax().get();

        int taskID = 0;

        for (int x = xmin; x < xmax; x += xlength) {
            for (int y = ymin; y < ymax; y += ylength) {
                for (int z = zmin; z < zmax; z += zlength) {
                    //Do work
                    taskID = checkBlocks(x, y, z, world);
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        while (Bukkit.getScheduler().isCurrentlyRunning(taskID)) {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Bukkit.getScheduler().runTask(BukkitSurvivalGamesPlugin.plugin,
                () -> {
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
                                    Optional<Loot> item = game.getLootGenerator().generate();
                                    if (item.isPresent()) inventory.addItem(item.get().getItem());
                                }
                                chest.update();
                            }
                    );
                    game.setChestsFilled();
                    Bukkit.getLogger().info("Chests have finished populating");
                }
        );

    }
}
