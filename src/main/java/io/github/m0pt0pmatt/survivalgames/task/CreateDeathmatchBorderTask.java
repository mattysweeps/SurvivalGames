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
package io.github.m0pt0pmatt.survivalgames.task;

import static io.github.m0pt0pmatt.survivalgames.Util.getOrThrow;
import static java.lang.Math.abs;

import com.flowpowered.math.vector.Vector3d;
import io.github.m0pt0pmatt.survivalgames.command.CommandKeys;
import io.github.m0pt0pmatt.survivalgames.game.SurvivalGame;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.util.TextMessageException;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.WorldBorder;

/** Creates the smaller world border for the deathmatch. */
public class CreateDeathmatchBorderTask implements Task {

    private static final Task INSTANCE = new CreateDeathmatchBorderTask();

    @Override
    public void execute(SurvivalGame survivalGame) throws TextMessageException {
        Integer xMin = null;
        Integer xMax = null;
        Integer zMin = null;
        Integer zMax = null;
        for (Vector3d spawn : survivalGame.getConfig().getSpawnPoints()) {
            if (xMin == null) {
                xMin = spawn.getFloorX();
            } else {
                xMin = Math.min(xMin, spawn.getFloorX());
            }
            if (xMax == null) {
                xMax = spawn.getFloorX();
            } else {
                xMax = Math.max(xMax, spawn.getFloorX());
            }
            if (zMin == null) {
                zMin = spawn.getFloorZ();
            } else {
                zMin = Math.min(zMin, spawn.getFloorZ());
            }
            if (zMax == null) {
                zMax = spawn.getFloorZ();
            } else {
                zMax = Math.max(zMax, spawn.getFloorZ());
            }
        }

        if (xMin != null) {
            String worldName =
                    getOrThrow(survivalGame.getConfig().getWorldName(), CommandKeys.WORLD_NAME);
            World world = getOrThrow(Sponge.getServer().getWorld(worldName), CommandKeys.WORLD);
            WorldBorder worldBorder = world.getWorldBorder();
            double diameter = Double.max(abs(xMax - xMin), abs(zMax - zMin));
            worldBorder.setDiameter(diameter);
        }
    }

    public static Task getInstance() {
        return INSTANCE;
    }
}
