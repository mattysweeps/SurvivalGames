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


import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
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
	@Override
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
		
		for (ItemStack item : (List<ItemStack>) config.getList(Fields.LOOT.getKey(), (List<?>) Fields.LOOT.getDefault()) {
			builder.addLoot(item);
		}
		
        return builder.build();
    }

    @Override
    public void serialize(TypeToken<?> type, SurvivalGameConfig obj, ConfigurationNode value) throws ObjectMappingException {
        if (obj.getWorldName().isPresent()) value.getNode("world").setValue(obj.getWorldName().get());

        ConfigurationNode exitNode = value.getNode("exit");
        if (obj.getExitWorld().isPresent()) exitNode.getNode("world").setValue(obj.getExitWorld().get());
        if (obj.getExit().isPresent()) {
            exitNode.getNode("X").setValue(obj.getExit().get().getX());
            exitNode.getNode("Y").setValue(obj.getExit().get().getY());
            exitNode.getNode("Z").setValue(obj.getExit().get().getZ());
        }

        ConfigurationNode centerNode = value.getNode("center");
        if (obj.getCenter().isPresent()) {
            centerNode.getNode("X").setValue(obj.getCenter().get().getX());
            centerNode.getNode("Y").setValue(obj.getCenter().get().getY());
            centerNode.getNode("Z").setValue(obj.getCenter().get().getZ());
        }

        if (obj.getPlayerLimit().isPresent()) value.getNode("playerLimit").setValue(obj.getPlayerLimit().get());
        if (obj.getCountdownTime().isPresent()) value.getNode("countdownTime").setValue(obj.getCountdownTime().get());

        ConfigurationNode spawnsNode = value.getNode("spawns");
        for (Vector spawn : obj.getSpawns()) {
            ConfigurationNode spawnNode = spawnsNode.getAppendedNode();
            spawnNode.getNode("X").setValue(spawn.getX());
            spawnNode.getNode("Y").setValue(spawn.getY());
            spawnNode.getNode("Z").setValue(spawn.getZ());
        }

        ConfigurationNode chestNode = value.getNode("chest");
        if (obj.getChestMidpoint().isPresent()) chestNode.getNode("midpoint").setValue(obj.getChestMidpoint().get());
        if (obj.getChestRange().isPresent()) chestNode.getNode("range").setValue(obj.getChestRange().get());
    }
}
