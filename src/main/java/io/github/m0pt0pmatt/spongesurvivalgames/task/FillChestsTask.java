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
package io.github.m0pt0pmatt.spongesurvivalgames.task;

import static io.github.m0pt0pmatt.spongesurvivalgames.Util.getOrThrow;

import com.flowpowered.math.vector.Vector3d;
import io.github.m0pt0pmatt.spongesurvivalgames.game.SurvivalGame;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.tileentity.carrier.Chest;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.TextMessageException;
import org.spongepowered.api.world.World;

import java.util.Random;

public class FillChestsTask implements Task {

    private static final Task INSTANCE = new FillChestsTask();

    @Override
    public void execute(SurvivalGame survivalGame) throws TextMessageException {

        Random random = new Random();

        Vector3d lowerBoundary = survivalGame.getConfig().getLesserBoundary().orElseThrow(() -> new TextMessageException(Text.of("lower")));
        Vector3d greaterBoundary = survivalGame.getConfig().getLesserBoundary().orElseThrow(() -> new TextMessageException(Text.of("better")));

        String worldName = survivalGame.getConfig().getWorldName().orElseThrow(() -> new TextMessageException(Text.of("worldName")));
        World world = Sponge.getServer().getWorld(worldName).orElseThrow(() -> new TextMessageException(Text.of("world")));

        Integer chestMidpoint = getOrThrow(survivalGame.getConfig().getChestMidpoint(), "chest midpoint");
        Integer chestRange = getOrThrow(survivalGame.getConfig().getChestRange(), "chest range");

        world.getTileEntities().forEach(tileEntity -> {

            if (tileEntity instanceof Chest) {
                Chest chest = (Chest) tileEntity;

                chest.getInventory().clear();

                if (!survivalGame.getConfig().getItems().isEmpty()) {

                    double itemCount = (
                            chestMidpoint +
                                    (
                                            (random.nextDouble() * chestRange)
                                                    * (random.nextDouble() > 0.5 ? 1 : -1)
                                    )
                    );
                    for (int i = 0; i < itemCount; i++) {
                        ItemStackSnapshot stackSnapshot = survivalGame.getConfig().getItems().get(random.nextInt(survivalGame.getConfig().getItems().size()));
                        chest.getInventory().offer(stackSnapshot.createStack());
                    }
                }
            }

        });
    }

    public static Task getInstance() {
        return INSTANCE;
    }
}
