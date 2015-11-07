package io.github.m0pt0pmatt.spongesurvivalgames.sponsor;

import org.bukkit.inventory.Inventory;

public abstract class SponsorInventory {
	
	protected Sponsor owner;
	
	protected SponsorInventory(Sponsor owner) {
		this.owner = owner;
	}
	
	/**
	 * Returns an inventory that's formatted and ready to be displayed.
	 * @return
	 */
	public abstract Inventory getFormattedInventory();
	
	public Sponsor getSponser() {
		return owner;
	}
	
}
