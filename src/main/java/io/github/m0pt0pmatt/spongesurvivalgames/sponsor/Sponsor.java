package io.github.m0pt0pmatt.spongesurvivalgames.sponsor;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
 * Wrapper  class for a player who has been appointed as a sponsor.<br />
 * Sponsors have the ability to aid or harm in-game players through a menu of options.
 * @author Skyler
 * @TODO Make a command that appoints people as sponsors
 */
public class Sponsor {
	
	/**
	 * An inventory sponsors will see when they make selections about what to do.<br />
	 * This was made static as each sponsor should really see the same inventory.
	 */
	private static Inventory sponsorInventory = null;
	
	private static enum MenuButtons
	
	private static void generateInventory() {
		
	}
	
	private Player player;
	
	public Sponsor(Player player) {
		this.player = player;
		if (Sponsor.sponsorInventory == null) {
			Sponsor.generateInventory();
		}
	}
}
