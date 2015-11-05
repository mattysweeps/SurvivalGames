package io.github.m0pt0pmatt.spongesurvivalgames.sponsor;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.m0pt0pmatt.spongesurvivalgames.SurvivalGame;
import io.github.m0pt0pmatt.spongesurvivalgames.sponsor.actions.GiveItemTask;
import io.github.m0pt0pmatt.spongesurvivalgames.sponsor.actions.MenuTask;

/**
 * Wrapper  class for a player who has been appointed as a sponsor.<br />
 * Sponsors have the ability to aid or harm in-game players through a menu of options.
 * @author Skyler
 */
public class Sponsor {
	
	//TODO Make a command that appoints people as sponsors

	private static enum items {
		
		LIGHTARMOR(new ItemStack(Material.LEATHER_CHESTPLATE)),
		MEDIUMARMOR(new ItemStack(Material.CHAINMAIL_CHESTPLATE)),
		LIGHTSWORD(new ItemStack(Material.WOOD_SWORD)),
		MEDIUMSWORD(new ItemStack(Material.IRON_SWORD)),
		BOW(new ItemStack(Material.BOW)),
		HEALTHPOTION(new ItemStack(Material.POTION, 1, (short) 8229)),
		POISONPOTION(new ItemStack(Material.POTION, 1, (short) 16388)),
		FOOD1(new ItemStack(Material.CARROT_ITEM, 5)),
		FOOD2(new ItemStack(Material.BREAD, 5)),
		FOOD3(new ItemStack(Material.COOKED_CHICKEN, 5));
		
		private ItemStack item;
		
		private items(ItemStack item) {
			this.item = item;
		}
		
		public ItemStack getItem() {
			return item;
		}
	}
	
	private static enum MenuButtons {
		
		LIGHTARMOR(items.LIGHTARMOR.getItem(),
				new GiveItemTask(items.LIGHTARMOR.getItem())),
		MEDIUMARMOR(items.MEDIUMARMOR.getItem(),
				new GiveItemTask(items.MEDIUMARMOR.getItem())),
		LIGHTSWORD(items.LIGHTSWORD.getItem(),
				new GiveItemTask(items.LIGHTSWORD.getItem())),
		MEDIUMSWORD(items.MEDIUMSWORD.getItem(),
				new GiveItemTask(items.MEDIUMSWORD.getItem())),
		BOW(items.BOW.getItem(),
				new GiveItemTask(items.BOW.getItem())),
		HEALTHPOTION(items.HEALTHPOTION.getItem(),
				new GiveItemTask(items.HEALTHPOTION.getItem())),
		POISONPOTION(items.POISONPOTION.getItem(),
				new GiveItemTask(items.POISONPOTION.getItem())),
		FOOD1(items.FOOD1.getItem(),
				new GiveItemTask(items.FOOD1.getItem())),
		FOOD2(items.FOOD2.getItem(),
				new GiveItemTask(items.FOOD2.getItem())),
		FOOD3(items.FOOD3.getItem(),
				new GiveItemTask(items.FOOD3.getItem()));
		
		
		private ItemStack displayItem;
		
		private MenuTask task;
		
		private MenuButtons(ItemStack item, MenuTask task) {
			this.displayItem = item;
			this.task = task;
		}
		
		public ItemStack getItem() {
			return displayItem;
		}
		
		public MenuTask getTask() {
			return task;
		}
	}
	
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
	protected void setInventory(SponsorInventory newInv) {
		this.inv = newInv;
		player.openInventory(
				inv.getFormattedInventory());
	}
}
