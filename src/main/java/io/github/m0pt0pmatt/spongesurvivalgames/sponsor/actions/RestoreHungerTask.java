package io.github.m0pt0pmatt.spongesurvivalgames.sponsor.actions;

import org.bukkit.entity.Player;

import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.TaskException;

/**
 * Restores a player's health when executed.
 * @author Skyler
 *
 */
public class RestoreHungerTask implements MenuPlayerTask {
	
	private static final String healMessage = "You're hunger has been eased by a sponsor!";
	
	@Override
	public void execute(Player player) throws TaskException {
		if (player == null || !player.isOnline()) {
			throw new TaskException();
		}
		
		//Set player's food level to full, exhaustion down, saturation full (best condition)
		player.setFoodLevel(20);
		player.setExhaustion(0f);
		player.setSaturation(20f);
		
		player.sendMessage(healMessage);
	}

	
}
