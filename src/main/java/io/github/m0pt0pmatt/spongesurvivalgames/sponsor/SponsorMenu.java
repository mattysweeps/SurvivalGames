package io.github.m0pt0pmatt.spongesurvivalgames.sponsor;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

/**
 * A menu that a sponsor uses to select exactly how they'll sponsor a player
 * @author Skyler
 *
 */
public class SponsorMenu extends SponsorInventory implements Listener {
	
	private static final String menuTitlePrefix = "SPMNU_";
	
	private UUID targetID;
	
	public SponsorMenu(Sponsor owner, UUID targetID) {
		super(owner);
		this.targetID = targetID;
	}
	
	
	@Override
	public Inventory getFormattedInventory() {
		
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
		
}
