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

package io.github.m0pt0pmatt.spongesurvivalgames.commands.game.stopped;

import io.github.m0pt0pmatt.spongesurvivalgames.BukkitSurvivalGamesPlugin;
import io.github.m0pt0pmatt.spongesurvivalgames.SurvivalGame;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.command.CommandSender;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Command to scrape the entire bounds and get all the chests
 */
public class GetChestsCommand extends StoppedCommand {

    @Override
    public boolean execute(CommandSender sender, Map<String, String> arguments) {

        if (!super.execute(sender, arguments)) {
            return false;
        }

        SurvivalGame game = BukkitSurvivalGamesPlugin.survivalGameMap.get(id);

        game.getConfig().getChestLocations().clear();

        int x, z;
        x = game.getConfig().getXMax().get() - game.getConfig().getXMin().get();
        z = game.getConfig().getZMax().get() - game.getConfig().getZMin().get();

        x = x / 16;
        z = z / 16;

        sender.sendMessage("Bounds include about " + (x * z) + " chunks.");

        sender.sendMessage("Beginning scrape for chests. This may take a while...");
        List<Chest> chests = getChests(game, sender);
        sender.sendMessage("Done!");
        sender.sendMessage("Found " + chests.size() + " chests!");

        for (Chest chest : chests) {
            game.getConfig().getChestLocations().add(
                    chest.getLocation().toVector()
            );
        }

        return true;
    }

    private List<Chest> getChests(SurvivalGame game, CommandSender sender) {
        String worldName = game.getWorldName().get();
        World world = Bukkit.getServer().getWorld(worldName);

        int xmin = game.getConfig().getXMin().get();
        int xmax = game.getConfig().getXMax().get();
        int zmin = game.getConfig().getZMin().get();
        int zmax = game.getConfig().getZMax().get();

        List<Chest> chests = new LinkedList<Chest>();

        int count = 0;

        Chunk lastChunk = null;

        for (int x = xmin; x < xmax; x += 16) {
            for (int z = zmin; z < zmax; z += 16) {

                Chunk chunk = world.getChunkAt(Math.floorDiv(x, 16), Math.floorDiv(z, 16));

                if (lastChunk != null && lastChunk.equals(chunk)) {
                    System.out.println("Chunks are equal!");
                }
                for (BlockState e : chunk.getTileEntities()) {
                    if (e instanceof Chest) {
                        chests.add((Chest) e);
                    }
                }


                count++;
                if (count % 1000 == 0) {
                    sender.sendMessage("Finished chunks 1 - " + count);
                }
                lastChunk = chunk;
            }
        }

        return chests;
    }
}
