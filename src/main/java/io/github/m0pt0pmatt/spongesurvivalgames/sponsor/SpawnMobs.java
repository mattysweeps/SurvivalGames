package io.github.m0pt0pmatt.spongesurvivalgames.sponsor;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class SpawnMobs implements Sponsor{
	//radius of spawns will be distanceFromPlayer + donutRadius
	private final int distanceFromPlayer = 5;//start the mobs at least 5 blocks away from the player
	private final int donutRadius = 5 ;//and at most 10 blocks away from the player
	
	private EntityType mobType;
	private int numMobs;
	
	SpawnMobs(EntityType mobType, int numMobs){
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
