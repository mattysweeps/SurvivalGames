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

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Random;

public class SpawnEntitySponsor implements Sponsor {

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

            Location playerLocation = player.getLocation();
            Location spawnLocation = new Location(player.getWorld(), playerLocation.getX() + x, playerLocation.getY(), playerLocation.getZ() + z);

            //make sure the location we want to spawn is valid.
            int attempts = 0;
            while (!canSpawn(spawnLocation) && attempts < 20) {
                spawnLocation.add(0, 1, 0);
                attempts++;
            }
            if (attempts >= 20) {
                i--;
                continue;
            }

            player.getWorld().spawnEntity(spawnLocation, this.mobType);
        }
    }

    private boolean canSpawn(Location location) {
        final Material[] spawnHere = {Material.AIR, Material.TORCH, Material.SIGN,
                Material.STATIONARY_WATER, Material.LONG_GRASS, Material.YELLOW_FLOWER,
                Material.RED_ROSE, Material.DEAD_BUSH, Material.VINE};
        Block block = location.getBlock();
        int height = 2;
        for (int i = 0; i < height; i++) {
            if (!Arrays.asList(spawnHere).contains(block.getType()))
                return false;
            block = block.getRelative(BlockFace.UP);
        }
        return true;
    }
}
