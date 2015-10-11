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

import io.github.m0pt0pmatt.spongesurvivalgames.SpongeSurvivalGamesPlugin;
import io.github.m0pt0pmatt.spongesurvivalgames.SurvivalGame;
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.TaskException;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityChest;
import org.spongepowered.api.block.tileentity.TileEntity;
import org.spongepowered.api.world.World;

import java.util.Collection;
import java.util.Random;

public class FillChestsTask implements SurvivalGameTask {
    @Override
    public void execute(SurvivalGame game) throws TaskException {
        String worldName = game.getWorldName().get();
        World world = SpongeSurvivalGamesPlugin.game.getServer().getWorld(worldName).get();
        Collection<TileEntity> tileEntities = world.getTileEntities();
        SpongeSurvivalGamesPlugin.logger.info("There are " + tileEntities.size() + " tile entities");
        tileEntities.forEach(entity -> {
                    final Random random = new Random();

                    SpongeSurvivalGamesPlugin.logger.info("We found a " + entity.getType() + "! " + entity.getLocation());

                    //TODO: this code doesn't use the api. Eventually it will need to be changed
                    if (entity.getType().getTileEntityType().equals(TileEntityChest.class)) {
                        SpongeSurvivalGamesPlugin.logger.info("CHHEESSST");

                        TileEntityChest chest = (TileEntityChest) entity;
                        chest.clear();

                        double itemCount = (
                                game.getChestMidpoint().get() +
                                (
                                        (random.nextDouble() * (game.getChestRange().get() * 2)) *
                                                random.nextDouble() > 0.5 ? 1 : -1
                                )
                        );
                        for (int i = 0; i < itemCount; i++){
                            chest.setInventorySlotContents(i, new net.minecraft.item.ItemStack(Item.getItemById(1)));
                        }

                    }
                }
        );
    }
}
