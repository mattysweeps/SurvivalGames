package io.github.m0pt0pmatt.spongesurvivalgames.backups;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.google.common.collect.Lists;

import io.github.m0pt0pmatt.spongesurvivalgames.BukkitSurvivalGamesPlugin;
import io.github.m0pt0pmatt.spongesurvivalgames.SurvivalGame;
import io.github.m0pt0pmatt.spongesurvivalgames.SurvivalGameState;
import io.github.m0pt0pmatt.spongesurvivalgames.config.SurvivalGameConfig;
import io.github.m0pt0pmatt.spongesurvivalgames.config.SurvivalGameConfigSerializer;
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.NoPlayerLimitException;
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.PlayerLimitReachedException;

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
	
    @SuppressWarnings("unchecked")
	public static Backup valueOf(Map<String, Object> configMap) {
        Backup backup = new Backup();
        
        SurvivalGameConfigSerializer serializer = new SurvivalGameConfigSerializer();
        YamlConfiguration section = new YamlConfiguration();
        section.createSection("config", (HashMap<String, Object>) configMap.get("config"));
        SurvivalGameConfig config = new SurvivalGameConfig();
        
        serializer.deserialize(config, section.getConfigurationSection("config"), true);
        backup.config = config;
        
        backup.gameState = SurvivalGameState.valueOf((String) configMap.get("state"));
        
        for (String key : configMap.keySet()) {
        	if (!key.startsWith("player-")) {
        		continue;
        	}
        	
        	backup.players.put(UUID.fromString(key.substring(7)), //start at 7 to get rid of player- 
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
    	
        @SuppressWarnings("unchecked")
		public static PlayerRecord valueOf(Map<String, Object> configMap) {
        	
        	UUID id = UUID.fromString((String) configMap.get("holderID"));
        	Player player = Bukkit.getPlayer(id);
        	
        	if (player == null) {
        		BukkitSurvivalGamesPlugin.plugin.getLogger().warning(
        				"Unable to fetch player for restored inventory: " + id);
        		return new PlayerRecord(null, null, 0, 0, null);
        	}
        	
        	int index = 0;
        	ItemStack[] inv = new ItemStack[36];
        	for (Object o : (List<Object>) configMap.get("inventory")) {
        		if (o == null) {
        			inv[index] = null;
        			index++;
        			continue;
        		}
        		
        		inv[index] = (ItemStack) o;
        		index++;
        	}
        	
        	ItemStack[] armor = new ItemStack[4];
        	index = 0;
        	for (Object o : (List<Object>) configMap.get("armor")) {
        		if (o == null) {
        			armor[index] = null;
        			index++;
        			continue;
        		}
        		
        		armor[index] = (ItemStack) o;
        		index++;
        	}
        	
        	double health = (double) configMap.get("health"),
        			maxHealth = (double) configMap.get("maxHealth");
        	
        	String worldName = (String) configMap.get("worldname");
        	World world = Bukkit.getWorld(worldName);
        	
        	if (world == null) {
        		BukkitSurvivalGamesPlugin.plugin.getLogger().warning(
        				"Unable to get world: " + worldName);
        		return new PlayerRecord(null, null, 0, 0, null);        		
        	}
        	
        	Vector offset = (Vector) configMap.get("pos");
        	
        	Location loc = new Location(world, offset.getX(), offset.getY(), offset.getZ());
        	
        	PlayerRecord record = new PlayerRecord(inv, armor, health, maxHealth, loc);
            return record;
        }

        @Override
        public Map<String, Object> serialize() {
            Map<String, Object> map = new HashMap<>();
            
            map.put("inventory", Lists.newArrayList(inventory));
            map.put("armor", Lists.newArrayList(armor));
            map.put("holderID", id.toString());
            map.put("health", playerHealth);
            map.put("maxHealth", playerMaxHealth);
            
            map.put("worldname", location.getWorld().getName());
            map.put("pos", new Vector(location.getX(), location.getY(), location.getZ()));

            return map;
        }
    	
        private UUID id;
        
    	private ItemStack[] inventory;
    	
    	private ItemStack[] armor;
    	
    	private double playerHealth;
    	
    	private double playerMaxHealth;
    	
    	private Location location;
    	
    	public PlayerRecord(Player player) {
    		id = player.getUniqueId();
    		inventory = player.getInventory().getContents();
    		armor = player.getInventory().getArmorContents();
    		playerHealth = player.getHealth();
    		playerMaxHealth = player.getMaxHealth();
    		location = player.getLocation();
    	}
    	
    	public PlayerRecord(ItemStack[] inventory, ItemStack[] armor, double health, double maxHealth, Location location) {
    		this.inventory = inventory;
    		this.armor = armor;
    		playerHealth = health;
    		playerMaxHealth = maxHealth;
    		this.location = location;
    	}

		public ItemStack[] getPlayerInventory() {
			return inventory;
		}
		
		public ItemStack[] getPlayerArmor() {
			return armor;
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
    	this.gameState = game.getState();
    	this.config = game.getConfig();
    	
    	for (Player player : BukkitSurvivalGamesPlugin.getPlayers(game.getPlayerUUIDs())) {
    		players.put(player.getUniqueId(), new PlayerRecord(player));
    	}
    }
    
    /**
     * Saves the output to the provided file.<br />
     * <b>Will erase the file if it already exists before writing</b>
     * @param outputFile
     * @throws IOException
     */
    public void save(File outputFile) throws IOException {
    	if (outputFile.exists()) {
    		outputFile.delete();
    	}
    	
    	BackupDumper.dumpBackup(this, outputFile);
    }
    
    public static Backup load(File inputFile) throws IOException, InvalidConfigurationException {
    	YamlConfiguration config = new YamlConfiguration();
    	
    	config.load(inputFile);
    	
    	Backup backup = (Backup) config.get("backup");
    	
    	return backup;
    }
    
    /**
     * Creates a game and restores it to the state kept by this backup.
     * @param id The id to give the new game
     * @return the game made from the backup
     */
    public SurvivalGame restore(String id) {
    	SurvivalGame game = new SurvivalGame(id);
    	BukkitSurvivalGamesPlugin.survivalGameMap.put(id, game);
    	
    	game.setConfig(config);
    	game.setState(gameState);
    	
    	for (UUID playerID : players.keySet()) {
    		//try and get the online player.
    		Player player = Bukkit.getPlayer(playerID);
    		if (player == null) {
    			BukkitSurvivalGamesPlugin.plugin.getLogger().warning("Couldn't find player for restore: " + playerID);
    			continue;
    		}
    		
    		//set their inventory, position, health from record
    		PlayerRecord record = players.get(playerID);
    		
    		player.setHealth(record.getPlayerHealth());
    		player.setMaxHealth(record.getPlayerMaxHealth());
    		player.getInventory().setContents(record.getPlayerInventory());
    		player.getInventory().setArmorContents(record.getPlayerArmor());
    		
    		player.teleport(record.getLocation());
    		
    		try {
				game.addPlayer(playerID);
			} catch (NoPlayerLimitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (PlayerLimitReachedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	
    	}
    	
    	return game;
    }
    
    protected YamlConfiguration asConfig() {
    	YamlConfiguration outcfg = new YamlConfiguration();
    	
    	outcfg.set("backup", this);
    	
    	return outcfg;
    }
	
}
