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

import com.flowpowered.math.vector.Vector3i;
import com.google.common.collect.Sets;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import io.github.m0pt0pmatt.spongesurvivalgames.SpongeSurvivalGamesPlugin;
import io.github.m0pt0pmatt.spongesurvivalgames.game.SurvivalGame;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Task for creating the player cage which surronds a player at the start of the match
 */
class CreateCageSnapshotsTask implements SurvivalGameTask {

    private static final Set<Vector3i> surroundingVectors = Sets.newHashSet(
        new Vector3i(1, 0, 0),
        new Vector3i(1, 1, 0),
        new Vector3i(-1, 0, 0),
        new Vector3i(-1, 1, 0),
        new Vector3i(0, 0, 1),
        new Vector3i(0, 1, 1),
        new Vector3i(0, 0, -1),
        new Vector3i(0, 1, -1),
        new Vector3i(0, 2, 0)
    );

    @Override
    public boolean execute(SurvivalGame game) {

        for (Vector3i spawn : game.getSpawnVectors()) {

            World world = Sponge.getGame().getServer().getWorld(game.getWorldName().get()).get();

            Location<World> location = new Location<>(world, spawn.getX(), spawn.getY(), spawn.getZ());

            surroundingVectors.forEach(vector -> location.add(vector).setBlock(BlockTypes.BARRIER.getDefaultState(), Cause.of(NamedCause.owner(SpongeSurvivalGamesPlugin.plugin))));

            SpongeSurvivalGamesPlugin.executorService.schedule(
                    () -> surroundingVectors.forEach(vector -> location.add(vector).setBlock(BlockTypes.AIR.getDefaultState(), Cause.of(NamedCause.owner(SpongeSurvivalGamesPlugin.plugin)))),
                    game.getCountdownTime().get(),
                    TimeUnit.SECONDS
            );
        }
        return true;
    }
}
