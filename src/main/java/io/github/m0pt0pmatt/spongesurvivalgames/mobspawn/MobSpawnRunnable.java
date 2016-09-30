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
package io.github.m0pt0pmatt.spongesurvivalgames.mobspawn;

import static com.google.common.base.Preconditions.checkNotNull;

import com.flowpowered.math.vector.Vector3d;
import io.github.m0pt0pmatt.spongesurvivalgames.data.MobSpawnArea;
import io.github.m0pt0pmatt.spongesurvivalgames.event.MobSpawnedEvent;
import io.github.m0pt0pmatt.spongesurvivalgames.game.SurvivalGame;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Optional;
import java.util.Random;

class MobSpawnRunnable implements Runnable {

    private final SurvivalGame survivalGame;
    private final MobSpawnArea mobSpawnArea;
    private final World world;
    private static final Random random = new Random();

    MobSpawnRunnable(SurvivalGame survivalGame, MobSpawnArea mobSpawnArea, World world) {
        this.survivalGame = checkNotNull(survivalGame, "survivalGame");
        this.mobSpawnArea = checkNotNull(mobSpawnArea, "mobSpawnArea");
        this.world = checkNotNull(world, "world");
    }

    @Override
    public void run() {

        Optional<Vector3d> lesserBoundary = mobSpawnArea.getLesserBoundary();
        Optional<Vector3d> greaterBoundary = mobSpawnArea.getGreaterBoundary();
        Optional<EntityType> entityType = mobSpawnArea.getEntityType();

        if (lesserBoundary.isPresent() && greaterBoundary.isPresent() && entityType.isPresent()) {
            double xDiff = greaterBoundary.get().getX() - lesserBoundary.get().getX();
            double yDiff = greaterBoundary.get().getY() - lesserBoundary.get().getY();
            double zDiff = greaterBoundary.get().getZ() - lesserBoundary.get().getZ();

            Location<World> location = world.getLocation(
                    lesserBoundary.get().getX() + random.nextInt((int) xDiff),
                    lesserBoundary.get().getY() + random.nextInt((int) yDiff),
                    lesserBoundary.get().getZ() + random.nextInt((int) zDiff)
            );

            Entity entity = world.createEntity(entityType.get(), location.getPosition());

            MobSpawnedEvent event = new MobSpawnedEvent(survivalGame, entity);
            boolean spawned = world.spawnEntity(entity, event.getCause());

            if (spawned) {
                Sponge.getEventManager().post(event);
            }
        }
    }
}
