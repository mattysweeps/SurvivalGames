package io.github.m0pt0pmatt.spongesurvivalgames.backups;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.util.Vector;

import io.github.m0pt0pmatt.spongesurvivalgames.BukkitSurvivalGamesPlugin;
import io.github.m0pt0pmatt.spongesurvivalgames.SurvivalGame;
import io.github.m0pt0pmatt.spongesurvivalgames.SurvivalGameState;
import io.github.m0pt0pmatt.spongesurvivalgames.backups.serializers.InventoryHolder;
import io.github.m0pt0pmatt.spongesurvivalgames.config.SurvivalGameConfig;
import io.github.m0pt0pmatt.spongesurvivalgames.config.SurvivalGameConfigSerializer;

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
        PlayerRecord.registerAliases();
    }
	
    public static Backup valueOf(Map<String, Object> configMap) {
        Backup backup = new Backup();
        
        SurvivalGameConfigSerializer serializer = new SurvivalGameConfigSerializer();
        ConfigurationSection section = (ConfigurationSection) configMap.get("config");
        SurvivalGameConfig config = new SurvivalGameConfig();
        serializer.deserialize(config, section, true);
        backup.config = config;
        
        backup.gameState = SurvivalGameState.valueOf((String) configMap.get("state"));
        
        for (String key : configMap.keySet()) {
        	if (!key.startsWith("player-")) {
        		continue;
        	}
        	
        	backup.players.put(UUID.fromString(key), 
        			(PlayerRecord) configMap.get(key));
        }
        
        return backup;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        
        SurvivalGameConfigSerializer serializer = new SurvivalGameConfigSerializer();
        map.put("config", serializer.serialize(config));
        map.put("state", gameState.name());
        
        for (UUID id : players.keySet()) {
        	map.put("player-" + id.toString(), players.get(id));
        }

        return map;
    }
    
    /**
     * Internal helper class to help keep together player information
     * @author Skyler
     *
     */
    public static class PlayerRecord implements ConfigurationSerializable {
    	
    	public static void registerAliases() {
            //Register this serializable class with some aliases too
            ConfigurationSerialization.registerClass(PlayerRecord.class);
            ConfigurationSerialization.registerClass(PlayerRecord.class, "backup");
            ConfigurationSerialization.registerClass(PlayerRecord.class, "BACKUP");
        }
    	
        public static PlayerRecord valueOf(Map<String, Object> configMap) {
        	
        	UUID id = UUID.fromString((String) configMap.get("holderID"));
        	Player player = Bukkit.getPlayer(id);
        	
        	if (player == null) {
        		BukkitSurvivalGamesPlugin.plugin.getLogger().warning(
        				"Unable to fetch player for restored inventory: " + id);
        		return new PlayerRecord(null, 0, 0, null);
        	}
        	
        	InventoryHolder helper = (InventoryHolder) configMap.get("inventory");
        	Inventory inv = helper.getInventory(player);
        	
        	int health = (int) configMap.get("health"),
        			maxHealth = (int) configMap.get("maxHealth");
        	
        	String worldName = (String) configMap.get("worldname");
        	World world = Bukkit.getWorld(worldName);
        	
        	if (world == null) {
        		BukkitSurvivalGamesPlugin.plugin.getLogger().warning(
        				"Unable to get world: " + worldName);
        		return new PlayerRecord(null, 0, 0, null);        		
        	}
        	
        	Vector offset = (Vector) configMap.get("pos");
        	
        	Location loc = new Location(world, offset.getX(), offset.getY(), offset.getZ());
        	
        	PlayerRecord record = new PlayerRecord(inv, health, maxHealth, loc);
            return record;
        }

        @Override
        public Map<String, Object> serialize() {
            Map<String, Object> map = new HashMap<>();
            
            map.put("inventory", new InventoryHolder(playerInventory));
            map.put("holderID", id.toString());
            map.put("health", playerHealth);
            map.put("maxHealth", playerMaxHealth);
            
            map.put("worldname", location.getWorld().getName());
            map.put("pos", new Vector(location.getX(), location.getY(), location.getZ()));

            return map;
        }
    	
        private UUID id;
        
    	private Inventory playerInventory;
    	
    	private double playerHealth;
    	
    	private double playerMaxHealth;
    	
    	private Location location;
    	
    	public PlayerRecord(Player player) {
    		id = player.getUniqueId();
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
    
    private Backup() {
    	players = new HashMap<UUID, PlayerRecord>();
    }
    
    public Backup(SurvivalGame game) {
    	players = new HashMap<UUID, PlayerRecord>();
    }
	
}
