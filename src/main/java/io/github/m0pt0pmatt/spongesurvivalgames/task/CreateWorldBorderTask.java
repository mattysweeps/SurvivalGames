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
import static java.lang.Math.abs;

import com.flowpowered.math.vector.Vector3d;
import io.github.m0pt0pmatt.spongesurvivalgames.game.SurvivalGame;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.util.TextMessageException;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.WorldBorder;

public class CreateWorldBorderTask implements Task {

    private static final Task INSTANCE = new CreateWorldBorderTask();

    @Override
    public void execute(SurvivalGame survivalGame) throws TextMessageException {

        String worldName = getOrThrow(survivalGame.getConfig().getWorldName(), "world name");
        World world = getOrThrow(Sponge.getServer().getWorld(worldName), "world");
        WorldBorder worldBorder = world.getWorldBorder();
        Vector3d center = getOrThrow(survivalGame.getConfig().getCenterVector(), "center vector");

        worldBorder.setCenter(center.getX(), center.getZ());

        Vector3d lesserBoundaryVector = getOrThrow(survivalGame.getConfig().getLesserBoundary(), "boundary");
        Vector3d greaterBoundaryVector = getOrThrow(survivalGame.getConfig().getGreaterBoundary(), "boundary");

        double diameter = Double.max(
                abs(greaterBoundaryVector.getX() - lesserBoundaryVector.getX()),
                abs(greaterBoundaryVector.getZ() - lesserBoundaryVector.getZ()));

        worldBorder.setDiameter(diameter);
        worldBorder.setWarningDistance(0);
    }

    public static Task getInstance() {
        return INSTANCE;
    }
}
