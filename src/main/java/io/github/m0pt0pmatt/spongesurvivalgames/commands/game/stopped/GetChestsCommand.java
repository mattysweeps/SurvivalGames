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

import io.github.m0pt0pmatt.spongesurvivalgames.commands.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.InventoryHolder;

import java.util.*;

/**
 * Command to scrape the entire bounds and get all the chests
 */
public class GetChestsCommand extends StoppedCommand {

    @Override
    public boolean execute(CommandSender sender, Map<CommandArgs, String> arguments) {

        if (!super.execute(sender, arguments)) {
            return false;
        }

        Optional<String> worldName = game.getWorldName();
        if (!worldName.isPresent()) {
            sender.sendMessage("No worldname set!");
            return false;
        }
        World world = Bukkit.getServer().getWorld(worldName.get());

        Optional<Integer> xmin = game.getConfig().getXMin();
        Optional<Integer> zmin = game.getConfig().getZMin();
        Optional<Integer> xmax = game.getConfig().getXMax();
        Optional<Integer> zmax = game.getConfig().getZMax();
        if (!xmin.isPresent() || !zmin.isPresent() || !xmax.isPresent() || !zmax.isPresent()) {
            sender.sendMessage(ChatColor.RED + "Bounds not set! Set the bounds first");
            return false;
        }
        if (!game.getConfig().getWorldName().isPresent()) {
            sender.sendMessage(ChatColor.RED + "The world name is not set. Please set the world name first.");
            return false;
        }

        game.getConfig().getChestLocations().clear();

        sender.sendMessage("Beginning scrape for chests. This may take a while...");

        Set<Chunk> chunks = new HashSet<>();
        for (int x = xmin.get() - 16; x < xmax.get() + 16; x += 16) {
            for (int z = zmin.get() - 16; z < zmax.get() + 16; z += 16) {
                chunks.add(world.getChunkAt(Math.floorDiv(x, 16), Math.floorDiv(z, 16)));
            }
        }
        sender.sendMessage("Found " + chunks.size() + " chunks");

        List<Chest> chests = new LinkedList<>();
        int count = 0;
        for (Chunk chunk : chunks) {
            for (BlockState e : chunk.getTileEntities()) {
                if (e instanceof InventoryHolder) {
                    ((InventoryHolder) e).getInventory().clear();
                }
                if (e instanceof Chest) {
                    chests.add((Chest) e);
                }
            }
            count++;
            if (count % 1000 == 0) {
                sender.sendMessage("Finished chunks 1 - " + count);
            }
        }
        sender.sendMessage("Done!");
        sender.sendMessage("Found " + chests.size() + " chests!");

        for (Chest chest : chests) {
            game.getConfig().getChestLocations().add(
                    chest.getLocation().toVector()
            );
        }

        return true;
    }

}
