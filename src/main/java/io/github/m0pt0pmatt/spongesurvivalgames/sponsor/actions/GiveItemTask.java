package io.github.m0pt0pmatt.spongesurvivalgames.sponsor.actions;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.TaskException;

public class GiveItemTask implements MenuTask {

	private ItemStack item;
	
	private Player player;
	
	public GiveItemTask(Player player, ItemStack item) {
		this.item = item;
		this.player = player;
	}

	@Override
	public void execute() throws TaskException {
		if (player == null || !player.isOnline()) {
			throw new TaskException();
		}
		
		player.getInventory().addItem(this.item);
	}

}
