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

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class SpawnEntitySponsor implements Sponsor{
	//radius of spawns will be distanceFromPlayer + donutRadius
	private final int distanceFromPlayer = 5;//start the mobs at least 5 blocks away from the player
	private final int donutRadius = 5 ;//and at most 10 blocks away from the player
	
	private EntityType mobType;
	private int numMobs;
	
	SpawnEntitySponsor(EntityType mobType, int numMobs){
		this.mobType = mobType;
		this.numMobs = numMobs;
	}
	
	@Override
	public void execute(Player player) {
		Random rGen = new Random();
		for(int i=0; i<this.numMobs; i++){
			int x = rGen.nextInt(donutRadius)+ distanceFromPlayer;
			x = rGen.nextInt(2) == 1 ? x*-1 : x;
			
			int z = rGen.nextInt(donutRadius)+ distanceFromPlayer;
			z = rGen.nextInt(2) == 1 ? z*-1 : z;
			
			Location playerLocation = player.getLocation();
			Location spawnLocation = new Location(player.getWorld(), playerLocation.getX()+x, playerLocation.getY(), playerLocation.getZ()+z);
			
			//make sure the location we want to spawn is valid.
			while(canSpawn(spawnLocation)== false){
				spawnLocation.add(0, 1, 0);
			}
			
			spawnMob(spawnLocation);
		}
	}

	private void spawnMob(Location location) {
		location.getWorld().spawnEntity(location, this.mobType);
	}
	
	private boolean canSpawn(Location location){
		if(location.getBlock().getType() == Material.AIR)
			return true;
		return false;
	}
}
