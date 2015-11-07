package io.github.m0pt0pmatt.spongesurvivalgames.sponsor;

import org.bukkit.entity.Player;

import io.github.m0pt0pmatt.spongesurvivalgames.SurvivalGame;

/**
 * Wrapper  class for a player who has been appointed as a sponsor.<br />
 * Sponsors have the ability to aid or harm in-game players through a menu of options.
 * @author Skyler
 */
public class Sponsor {
	
	//TODO Make a command that appoints people as sponsors
	
	private Player player;
	
	private SurvivalGame game;
	
	private SponsorInventory inv;
	
	public Sponsor(SurvivalGame game, Player player) {
		this.player = player;
		this.game = game;
	}
	
	public SurvivalGame getGame() {
		return game;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	/**
	 * Sets the inventory stored with this sponsor and displays it to the player.
	 * @param newInv
	 */
	public void setInventory(SponsorInventory newInv) {
		this.inv = newInv;
		player.openInventory(
				inv.getFormattedInventory());
	}
}
