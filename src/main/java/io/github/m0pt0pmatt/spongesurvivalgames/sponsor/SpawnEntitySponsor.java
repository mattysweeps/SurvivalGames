/*
 * This file is part of SpongeSurvivalGamesPlugin, licensed under the MIT License (MIT).
 *
 * Copyright (c) Matthew Broomfield <m0pt0pmatt17@gmail.com>
 * Copyright (c) Stephanie Martinez <bassclairnetrules@gmail.com>
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

package io.github.m0pt0pmatt.spongesurvivalgames.sponsor;

import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Arrays;
import java.util.Random;

class SpawnEntitySponsor implements Sponsor {

    private final EntityType mobType;
    private final int numMobs;

    SpawnEntitySponsor(EntityType mobType, int numMobs) {
        this.mobType = mobType;
        this.numMobs = numMobs;
    }

    @Override
    public void execute(Player player) {
        Random rGen = new Random();
        for (int i = 0; i < this.numMobs; i++) {
            int distanceFromPlayer = 5;
            int donutRadius = 15;
            int x = rGen.nextInt(donutRadius) + distanceFromPlayer;
            x = rGen.nextInt(2) == 1 ? x * -1 : x;

            int z = rGen.nextInt(donutRadius) + distanceFromPlayer;
            z = rGen.nextInt(2) == 1 ? z * -1 : z;

            Location<World> playerLocation = player.getLocation();

            Location<World> spawnLocation = new Location<>(playerLocation.getExtent(), playerLocation.getX() + x, playerLocation.getY(), playerLocation.getZ() + z);

            //make sure the location we want to spawn is valid.
            int attempts = 0;
            while (!canSpawn(spawnLocation) && attempts < 20) {
                spawnLocation = spawnLocation.add(0, 1, 0);
                attempts++;
            }
            if (attempts >= 20) {
                i--;
                continue;
            }

            player.getWorld().createEntity(mobType, spawnLocation.getPosition());
        }
    }

    private boolean canSpawn(Location location) {

        final BlockType[] spawnHere = {BlockTypes.AIR, BlockTypes.TORCH, BlockTypes.STANDING_SIGN,
            BlockTypes.WATER, BlockTypes.TALLGRASS, BlockTypes.YELLOW_FLOWER,
            BlockTypes.RED_FLOWER, BlockTypes.DEADBUSH, BlockTypes.VINE};

        BlockState block = location.getBlock();
        int height = 2;
        for (int i = 0; i < height; i++) {
            if (!Arrays.asList(spawnHere).contains(block.getType()))
                return false;
        }
        return true;
    }
}
