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

package io.github.m0pt0pmatt.survivalgames.thread;

import com.flowpowered.math.vector.Vector3d;
import com.google.common.collect.Lists;
import io.github.m0pt0pmatt.survivalgames.game.SurvivalGame;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.world.World;

import java.util.ArrayList;

public class SetBlocksProgressable extends PercentageProgressable {

    private final SurvivalGame survivalGame;

    public SetBlocksProgressable(SurvivalGame survivalGame) {
        this.survivalGame = survivalGame;
    }

    @Override
    public void run() {
        setBlocks();
    }

    private void setBlocks() {
        Vector3d lesser = survivalGame.getConfig().getBlockArea().getLesserBoundary().orElseThrow(() -> new IllegalStateException("Missing boundaries"));
        Vector3d greater = survivalGame.getConfig().getBlockArea().getGreaterBoundary().orElseThrow(() -> new IllegalStateException("Missing boundaries"));
        World world = survivalGame.getConfig().getWorldName().flatMap(n -> Sponge.getServer().getWorld(n)).orElseThrow(() -> new IllegalStateException("No world yet"));

        int totalBlocks = (greater.getFloorX() - lesser.getFloorX())
                * (greater.getFloorY() - lesser.getFloorY())
                * (greater.getFloorZ() - lesser.getFloorZ());
        int count = 0;

        ArrayList<BlockSnapshot> blocks = Lists.newArrayList();
        for (int x = lesser.getFloorX(); x < greater.getFloorX(); x++) {
            for (int y = lesser.getFloorY(); y < greater.getFloorY(); y++) {
                for (int z = lesser.getFloorZ(); z < greater.getFloorZ(); z++) {
                    blocks.add(world.createSnapshot(x, y, z));
                    count++;
                    if (totalBlocks != 0) {
                        setPercentage((double) count / totalBlocks);
                    }
                }
            }
        }

        survivalGame.getConfig().setBlocks(blocks);
    }
}
