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

import com.google.common.collect.Sets;
import io.github.m0pt0pmatt.spongesurvivalgames.BukkitSurvivalGamesPlugin;
import io.github.m0pt0pmatt.spongesurvivalgames.SurvivalGame;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.Vector;

import java.util.Set;

/**
 * Task for creating the player cage which surronds a player at the start of the match
 */
class CreateCageSnapshotsTask implements SurvivalGameTask {

    private static final Set<Vector> surroundingVectors = Sets.newHashSet(
            new Vector(1, 0, 0),
            new Vector(1, 1, 0),
            new Vector(-1, 0, 0),
            new Vector(-1, 1, 0),
            new Vector(0, 0, 1),
            new Vector(0, 1, 1),
            new Vector(0, 0, -1),
            new Vector(0, 1, -1),
            new Vector(0, 2, 0)
    );

    @Override
    public boolean execute(SurvivalGame game) {
        for (Vector spawn : game.getSpawnVectors()) {

            World world = Bukkit.getServer().getWorld(game.getWorldName().get());
            if (world == null) return false;

            Location location = new Location(world, spawn.getX(), spawn.getY(), spawn.getZ());

            surroundingVectors.forEach(vector -> location.clone().add(vector).getBlock().setType(Material.BARRIER));

            Bukkit.getScheduler().scheduleSyncDelayedTask(
                    BukkitSurvivalGamesPlugin.plugin,
                    () -> surroundingVectors.forEach(vector -> location.clone().add(vector).getBlock().setType(Material.AIR)),
                    20L * game.getCountdownTime().get()
            );
        }
        return true;
    }
}
