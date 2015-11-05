package io.github.m0pt0pmatt.spongesurvivalgames.sponsor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

/**
 * A menu with all players available for selection by the sponsor.
 * @author Skyler
 *
 */
public class PlayerMenu extends SponsorInventory implements Listener {
	
	private static final String menuTitlePrefix = "PLMNU_";
	
	private Map<Integer, UUID> playerMap;
	
	public PlayerMenu(Sponsor owner) {
		super(owner);
		playerMap = new HashMap<Integer, UUID>();
	}
	
	
	@Override
	public Inventory getFormattedInventory() {
		
		Inventory inv = Bukkit.createInventory(owner.getPlayer(), 27, 
				(menuTitlePrefix + owner.getPlayer().getName()).substring(0, 31) ); //limit to 32 chars cause title limit
		List<Player> players = getValidPlayers();
		int index = 0;
		
		for (Player player : players) {
			inv.setItem(index, 
					getHeadItem(player));
			playerMap.put(index, player.getUniqueId());
			index++;
		}
		
		return inv;
	}
	
	@EventHandler
	public void onInventoryInteract(InventoryClickEvent e) {
		if (e.isCancelled()) {
			return;
		}
		
		if (!e.getInventory().getTitle().equals(menuTitlePrefix + owner.getPlayer().getName().substring(0, 31))) {
			return; //wrong inventory
		}
		
		if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) {
			return; //click'ed on nothing
		}
		
		
		
	}
	
	/**
	 * Gets a list of players in the game held by the sponsor
	 * @return
	 */
	private List<Player> getValidPlayers() {
		Set<UUID> ids = owner.getGame().getPlayerUUIDs();
		List<Player> players = new ArrayList<Player>(ids.size());
		Player player;
		
		for (UUID id : ids) {
			player = Bukkit.getPlayer(id);
			if (player == null || !player.isOnline()) {
				continue;
			}
			
			players.add(player);
		}
		
		return players;
	}
	
	/**
	 * Returns an itemstack of the provided player head.<br />
	 * The returned skill will have the same skin as the player and be given the display name (tooltip)
	 * of the player's name.
	 * @param player
	 * @return
	 */
	private ItemStack getHeadItem(Player player) {
		ItemStack skull = new ItemStack(Material.SKULL_ITEM);
		
		SkullMeta meta = (SkullMeta) skull.getItemMeta();
		
		meta.setOwner(player.getName());
		meta.setDisplayName(player.getName());
		
		skull.setItemMeta(meta);
		
		return skull;
	}
	
}
