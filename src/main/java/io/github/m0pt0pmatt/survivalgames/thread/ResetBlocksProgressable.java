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

import io.github.m0pt0pmatt.survivalgames.SurvivalGamesPlugin;
import io.github.m0pt0pmatt.survivalgames.game.SurvivalGame;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.world.BlockChangeFlags;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ResetBlocksProgressable extends PercentageProgressable {

    private final SurvivalGame survivalGame;

    public ResetBlocksProgressable(SurvivalGame survivalGame) {
        this.survivalGame = survivalGame;
    }

    @Override
    public void run() {

        int chunkCount = 50000;
        int total = survivalGame.getConfig().getBlocks().size();

        Iterator<BlockSnapshot> iterator = survivalGame.getConfig().getBlocks().iterator();
        for (int i = 0; i < total; i += chunkCount) {

            int start = i;
            Future<?> future = SurvivalGamesPlugin.SYNC_EXECUTOR.submit(() -> {
                for (int j = start; j < start + chunkCount; j++) {
                    BlockSnapshot blockSnapshot = iterator.next();
                    blockSnapshot.restore(true, BlockChangeFlags.ALL);
                }
            });

            try {
                future.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            } catch (ExecutionException e) {
                if (!(e.getCause() instanceof NoSuchElementException)) {
                    e.printStackTrace();
                    return;
                }
            }

            setPercentage((double) i / total);

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}
