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

package io.github.m0pt0pmatt.spongesurvivalgames.config;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import io.github.m0pt0pmatt.spongesurvivalgames.BukkitSurvivalGamesPlugin;

public class SurvivalGameConfigSerializer {
	
	
	private enum Fields {
		
		WORLD("world", ""),
		PLAYERLIMIT("playerLimit", 16),
		COUNTDOWNTIME("countdownTime", 30),
		EXITWORLD("exit.world", ""),
		EXIT("exit", new Vector(0,0,0)),
		CENTER("center", new Vector(0,0,0)),
		SPAWNS("spawns", new LinkedList<Map<String, Double>>()),
		CHEST_MIDPOINT("chest.midpoint", 0.0),
		CHEST_RANGE("chest.range", 0.0),
		LOOT("loot", new LinkedList<ItemStack>());
		
		private String key;
		
		private Object def;
		
		private Fields(String key, Object def) {
			this.key = key;
			this.def = def;
		}
		
		public String getKey() {
			return key;
		}
		
		public Object getDefault() {
			return def;
		}
	}
	
    @SuppressWarnings("unchecked")
	public SurvivalGameConfig deserialize(ConfigurationSection config) {
        SurvivalGameConfigBuilder builder = new SurvivalGameConfigBuilder();
        
        builder.worldName(config.getString(Fields.WORLD.getKey(), (String) Fields.WORLD.getDefault()));
    	
    	builder.exitWorld(config.getString(Fields.EXITWORLD.getKey(), (String) Fields.EXITWORLD.getDefault()));
    	
    	builder.exitLocation(config.getVector(Fields.EXIT.getKey(), (Vector) Fields.EXIT.getDefault()));
    	
    	builder.centerLocation(config.getVector(Fields.CENTER.getKey(), (Vector) Fields.CENTER.getDefault()));
    	
    	builder.playerLimit(config.getInt(Fields.PLAYERLIMIT.getKey(), (Integer) Fields.PLAYERLIMIT.getDefault()));
    	
    	builder.countdownTime(config.getInt(Fields.COUNTDOWNTIME.getKey(), (Integer) Fields.COUNTDOWNTIME.getDefault()));
    	
		List<Map<String, Object>> vectorList = (List<Map<String, Object>>) config.getList(Fields.SPAWNS.getKey(), (List<?>) Fields.SPAWNS.getDefault());
    	
    	if (!vectorList.isEmpty()) {
    		for (Map<String, Object> map : vectorList) {
    			
    			if (!map.containsKey("X") || !map.containsKey("Y") || !map.containsKey("Z")) {
    				BukkitSurvivalGamesPlugin.plugin.getLogger().warning("Unable to find correct keys when parsing "
    						+ "spawn list! Skipping...");
    				continue;
    			}
    			
    			try {
    			builder.addSpawn(new Vector(
    					(Double) map.get("X"),
    					(Double) map.get("Y"),
    					(Double) map.get("Z")));
    			} catch (ClassCastException e) {
    				BukkitSurvivalGamesPlugin.plugin.getLogger().warning("Error encountered when reading double "
    						+ "value in spawn location! Skipping...");
    				continue;
    			}
    		}
    	}
		
		builder.chestMidpoint(config.getDouble(Fields.CHEST_MIDPOINT.getKey(), (Double) Fields.CHEST_MIDPOINT.getDefault()));
		
		builder.chestRange(config.getDouble(Fields.CHEST_RANGE.getKey(), (Double) Fields.CHEST_RANGE.getDefault()));
		
		for (Object item : config.getList(Fields.LOOT.getKey(), (List<?>) Fields.LOOT.getDefault())) {
			if (!(item instanceof ItemStack)) {
				BukkitSurvivalGamesPlugin.plugin.getLogger().warning("Error encountered when parsing loot!"
						+ " List item not an ITEMSTACK! Skipping...");
				continue;				
			}
			builder.addLoot((ItemStack) item);
		}
		
        return builder.build();
    }

    public YamlConfiguration serialize(SurvivalGameConfig obj) {
    	
    	YamlConfiguration config = new YamlConfiguration();
    	
    	config.set(Fields.WORLD.getKey(), obj.getWorldName());
    	config.set(Fields.EXITWORLD.getKey(), obj.getExitWorld());
    	config.set(Fields.EXIT.getKey(), obj.getExit());
    	config.set(Fields.CENTER.getKey(), obj.getCenter());
    	config.set(Fields.PLAYERLIMIT.getKey(), obj.getPlayerLimit());
    	config.set(Fields.COUNTDOWNTIME.getKey(), obj.getCountdownTime());
    	
        config.set(Fields.SPAWNS.getKey(), new ArrayList<Vector>(obj.getSpawns()));
        
        config.set(Fields.CHEST_MIDPOINT.getKey(), obj.getChestMidpoint());
        config.set(Fields.CHEST_RANGE.getKey(), obj.getChestRange());
        
        config.set(Fields.LOOT.getKey(), obj.getLoot());
        
        return config;
    }
}
