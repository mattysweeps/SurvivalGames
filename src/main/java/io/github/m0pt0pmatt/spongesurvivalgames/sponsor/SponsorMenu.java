package io.github.m0pt0pmatt.spongesurvivalgames.sponsor;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.m0pt0pmatt.spongesurvivalgames.sponsor.actions.GiveItemTask;
import io.github.m0pt0pmatt.spongesurvivalgames.sponsor.actions.MenuPlayerTask;
import io.github.m0pt0pmatt.spongesurvivalgames.sponsor.actions.RestoreHealthTask;
import io.github.m0pt0pmatt.spongesurvivalgames.sponsor.actions.RestoreHungerTask;

/**
 * A menu that a sponsor uses to select exactly how they'll sponsor a player
 * @author Skyler
 *
 */
public class SponsorMenu extends SponsorInventory implements Listener {
	
	private static final String menuTitlePrefix = "SPMNU_";
	
	/**
	 * How many slots will be in the created, rendered inventory
	 */
	private static final int inventorySlotCount = 27;
	
	/**
	 * Collection of display items (and given items) for the buttons in the menu.<br />
	 * For all MenuButton's that give an item, these are what is expected to be given as well
	 * as what's used as a display.<br />
	 * For buttons that do something other than giving an item (like restoring hunger), these
	 * server only as the display icon.
	 * @author Skyler
	 *
	 */
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
		FOOD3(new ItemStack(Material.COOKED_CHICKEN, 5)),
		RESTOREHUNGER(new ItemStack(Material.APPLE), "Restore Hunger", Enchantment.ARROW_INFINITE, 1),
		RESTOREHEALTH(new ItemStack(Material.GOLDEN_APPLE), "Restore Health", Enchantment.ARROW_INFINITE, 1);
		
		private ItemStack item;
		
		private items(ItemStack item) {
			this.item = item;
		}
		
		private items(ItemStack item, String name) {
			this.item = item;
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(name);
			this.item.setItemMeta(meta);
		}
		
		private items(ItemStack item, String name, Enchantment enchantment, int level) {
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(name);
			meta.addEnchant(enchantment, level, true);
			this.item = item;
			this.item.setItemMeta(meta);
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
				new GiveItemTask(items.FOOD3.getItem())),
		RESTOREHUNGER(items.RESTOREHUNGER.getItem(),
				new RestoreHungerTask()),
		RESTOREHEALTH(items.RESTOREHEALTH.getItem(),
				new RestoreHealthTask());	
		
		
		private ItemStack displayItem;
		
		private MenuPlayerTask task;
		
		private MenuButtons(ItemStack item, MenuPlayerTask task) {
			this.displayItem = item;
			this.task = task;
		}
		
		public ItemStack getItem() {
			return displayItem;
		}
		
		public MenuPlayerTask getTask() {
			return task;
		}
	}
	
	private UUID targetID;
	
	public SponsorMenu(Sponsor owner, UUID targetID) {
		super(owner);
		this.targetID = targetID;
	}
	
	
	@Override
	public Inventory getFormattedInventory() {
		Inventory inv = Bukkit.createInventory(owner.getPlayer(), inventorySlotCount, 
				(menuTitlePrefix + owner.getPlayer().getName()).substring(0, 31));
		
		
		
		
		
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
		
}
