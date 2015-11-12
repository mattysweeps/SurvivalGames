package io.github.m0pt0pmatt.spongesurvivalgames.backups;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import io.github.m0pt0pmatt.spongesurvivalgames.SurvivalGameState;
import io.github.m0pt0pmatt.spongesurvivalgames.config.SurvivalGameConfig;

/**
 * A backup of a game.
 * @author Skyler
 *
 */
public class Backup implements ConfigurationSerializable {

	public static void registerAliases() {
        //Register this serializable class with some aliases too
        ConfigurationSerialization.registerClass(Backup.class);
        ConfigurationSerialization.registerClass(Backup.class, "backup");
        ConfigurationSerialization.registerClass(Backup.class, "BACKUP");
    }
	
    public static Backup valueOf(Map<String, Object> configMap) {
        Backup backup = new Backup();

        return backup;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();


        return map;
    }
    
    /**
     * Internal helper class to help keep together player information
     * @author Skyler
     *
     */
    private static class PlayerRecord implements ConfigurationSerializable {
    	
    	public static void registerAliases() {
            //Register this serializable class with some aliases too
            ConfigurationSerialization.registerClass(PlayerRecord.class);
            ConfigurationSerialization.registerClass(PlayerRecord.class, "backup");
            ConfigurationSerialization.registerClass(PlayerRecord.class, "BACKUP");
        }
    	
        public static PlayerRecord valueOf(Map<String, Object> configMap) {
        	PlayerRecord record = new PlayerRecord();

            return record;
        }

        @Override
        public Map<String, Object> serialize() {
            Map<String, Object> map = new HashMap<>();


            return map;
        }
    	
    	private Inventory playerInventory;
    	
    	private double playerHealth;
    	
    	private double playerMaxHealth;
    	
    	private Location location;
    	
    	public PlayerRecord(Player player) {
    		playerInventory = player.getInventory();
    		playerHealth = player.getHealth();
    		playerMaxHealth = player.getMaxHealth();
    		location = player.getLocation();
    	}
    	
    	public PlayerRecord(Inventory inventory, double health, double maxHealth, Location location) {
    		playerInventory = inventory;
    		playerHealth = health;
    		playerMaxHealth = maxHealth;
    		this.location = location;
    	}

		public Inventory getPlayerInventory() {
			return playerInventory;
		}

		public double getPlayerHealth() {
			return playerHealth;
		}

		public double getPlayerMaxHealth() {
			return playerMaxHealth;
		}

		public Location getLocation() {
			return location;
		}
    	
    	
    }
    
    private SurvivalGameConfig config;
    
    private SurvivalGameState gameState;
    
    private Map<UUID, PlayerRecord> players;
    
    public Backup() {
    	
    }
	
}
