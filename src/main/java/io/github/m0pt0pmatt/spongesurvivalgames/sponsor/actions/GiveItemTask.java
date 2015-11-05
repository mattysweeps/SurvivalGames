package io.github.m0pt0pmatt.spongesurvivalgames.sponsor.actions;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.m0pt0pmatt.spongesurvivalgames.SurvivalGame;
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.TaskException;

public class GiveItemTask implements MenuTask {

	private ItemStack item;
	
	public GiveItemTask(ItemStack item) {
		this.item = item;
	}

	@Override
	public void execute(SurvivalGame game, Player player) throws TaskException {
		player.getInventory().addItem(this.item);
	}

}
