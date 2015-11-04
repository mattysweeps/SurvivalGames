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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import io.github.m0pt0pmatt.spongesurvivalgames.BukkitSurvivalGamesPlugin;

public class SurvivalGameConfig {
	
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

    private String worldName;

    private String exitWorld;

    private Vector exit;

    private Vector center;

    private Integer playerLimit;

    private Integer countdownTime;

    private Set<Vector> spawns = new HashSet<>();

    private Double chestMidpoint;

    private Double chestRange;

    private List<ItemStack> loot = new ArrayList<ItemStack>();
    
    /**
     * Returns a game configuration from the passed configuration section.<br />
     * @param config
     * @return
     */
    @SuppressWarnings("unchecked")
	public static SurvivalGameConfig fromConfig(ConfigurationSection config) {
    	SurvivalGameConfig scfg = new SurvivalGameConfig();
    	
    	scfg.worldName = config.getString(Fields.WORLD.getKey(), (String) Fields.WORLD.getDefault());
    	
    	scfg.exitWorld = config.getString(Fields.EXITWORLD.getKey(), (String) Fields.EXITWORLD.getDefault());
    	
    	scfg.exit = config.getVector(Fields.EXIT.getKey(), (Vector) Fields.EXIT.getDefault());
    	
    	scfg.center = config.getVector(Fields.CENTER.getKey(), (Vector) Fields.CENTER.getDefault());
    	
    	scfg.playerLimit = config.getInt(Fields.PLAYERLIMIT.getKey(), (Integer) Fields.PLAYERLIMIT.getDefault());
    	
    	scfg.countdownTime = config.getInt(Fields.COUNTDOWNTIME.getKey(), (Integer) Fields.COUNTDOWNTIME.getDefault());
    	
		List<Map<String, Object>> vectorList = (List<Map<String, Object>>) config.getList(Fields.SPAWNS.getKey(), (List<?>) Fields.SPAWNS.getDefault());
    	Set<Vector> spawns = new HashSet<Vector>();
    	
    	if (!vectorList.isEmpty()) {
    		for (Map<String, Object> map : vectorList) {
    			
    			if (!map.containsKey("X") || !map.containsKey("Y") || !map.containsKey("Z")) {
    				BukkitSurvivalGamesPlugin.plugin.getLogger().warning("Unable to find correct keys when parsing "
    						+ "spawn list!");
    				continue;
    			}
    			
    			spawns.add(new Vector(
    					(Double) map.get("X"),
    					(Double) map.get("Y"),
    					(Double) map.get("Z")));
    		}
    	}

		scfg.spawns = spawns;
		
		scfg.chestMidpoint = config.getDouble(Fields.CHEST_MIDPOINT.getKey(), (Double) Fields.CHEST_MIDPOINT.getDefault());
		
		scfg.chestRange = config.getDouble(Fields.CHEST_RANGE.getKey(), (Double) Fields.CHEST_RANGE.getDefault());
		
		scfg.loot = (List<ItemStack>) config.getList(Fields.LOOT.getKey(), (List<?>) Fields.LOOT.getDefault());
		
    	return scfg;
    	
    }

    /**
     * Returns the name of the world.
     * @return The string name, or null if none is set
     */
    public String getWorldName() {
        return worldName;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    /**
     * Returns the exit world name, or null
     * @return
     */
    public String getExitWorld() {
        return exitWorld;
    }

    public void setExitWorld(String exitWorld) {
        this.exitWorld = exitWorld;
    }

    public Vector getExit() {
        return exit;
    }

    public void setExit(Vector exit) {
        this.exit = exit;
    }

    public Vector getCenter() {
        return center;
    }

    public void setCenter(Vector center) {
        this.center = center;
    }

    public Integer getPlayerLimit() {
        return playerLimit;
    }

    public void setPlayerLimit(Integer playerLimit) {
        this.playerLimit = playerLimit;
    }

    public Integer getCountdownTime() {
        return countdownTime;
    }

    public void setCountdownTime(Integer countdownTime) {
        this.countdownTime = countdownTime;
    }

    public Set<Vector> getSpawns() {
        return spawns;
    }

    public Double getChestMidpoint() {
        return chestMidpoint;
    }

    public void setChestMidpoint(Double chestMidpoint) {
        this.chestMidpoint = chestMidpoint;
    }

    public Double getChestRange() {
        return chestRange;
    }

    public void setChestRange(Double chestRange) {
        this.chestRange = chestRange;
    }

    public List<ItemStack> getLoot() {
        return loot;
    }

}
