package io.github.m0pt0pmatt.spongesurvivalgames.loot;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.ItemStack;

/**
 * Wrapper class for Loot which houses information about it's weight and the item it's storing
 *
 * @author Skyler
 */
public class Loot implements ConfigurationSerializable {
	
	/**
	 * Serializes the wrapped loot information to a format that's able to be saved to a configuration file.
	 */
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("weight", weight);
		map.put("item", item);
		
		return map;
	}
	
	/**
	 * Uses the passed configuration map to instantiate a new piece of loot.
	 * @param configMap
	 * @return
	 */
	public static Loot valueOf(Map<String, Object> configMap) {
		Loot loot = new Loot();
		loot.weight = (double) configMap.get("weight");
		loot.item = (ItemStack) configMap.get("item");
		
		return loot;
	}
	
	{
		//Register this serializable class with some aliases too
		ConfigurationSerialization.registerClass(Loot.class);
		ConfigurationSerialization.registerClass(Loot.class, "loot");
		ConfigurationSerialization.registerClass(Loot.class, "LOOT");
	}
	
    private ItemStack item;

    private double weight;

    private Loot() {
    	this.weight = 0;
    	this.item = null;
    }
    
    public Loot(ItemStack item, double weight) {
        this.item = item;
        this.weight = weight;
    }

    public ItemStack getItem() {
        return item;
    }

    public double getWeight() {
        return weight;
    }


}
