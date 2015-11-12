package io.github.m0pt0pmatt.spongesurvivalgames.backups.serializers;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryHolder implements ConfigurationSerializable {

	public static void registerAliases() {
        //Register this serializable class with some aliases too
        ConfigurationSerialization.registerClass(InventoryHolder.class);
        ConfigurationSerialization.registerClass(InventoryHolder.class, "inventory");
        ConfigurationSerialization.registerClass(InventoryHolder.class, "Inventory");
    }
	
    public static InventoryHolder valueOf(Map<String, Object> configMap) {
    	InventoryHolder inv = new InventoryHolder();
    	
    	for (String key : configMap.keySet()) {
    		int index = 0;
    		try {
    			index = Integer.valueOf(key);
    		} catch (NumberFormatException e) {
    			continue;
    		}
    		if (!(configMap.get(key) instanceof ItemStack)){
    			continue;
    		}
    		
    		inv.items.put(index, (ItemStack) configMap.get(key));
    	}
    	inv.size = (int) configMap.get("size");

        return inv;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        
        map.put("size", size);
        for (int key : items.keySet()) {
        	map.put(key + "", items.get(key));
        }

        return map;
    }
    
    private Map<Integer, ItemStack> items;
    
    private int size;
    
    private InventoryHolder() {
    	this.items = new HashMap<Integer, ItemStack>();
    	this.size = 0;
    }
    
    public InventoryHolder(Inventory inventory) {
    	this.items = new HashMap<Integer, ItemStack>();
    	this.size = inventory.getSize();
    	for (int i = 0; i < size; i++) {
    		items.put(i, inventory.getItem(i));
    	}
    }
    
    public Inventory getInventory(org.bukkit.inventory.InventoryHolder holder) {
    	Inventory inv = Bukkit.createInventory(holder, size);
    	return inv;
    }
}
