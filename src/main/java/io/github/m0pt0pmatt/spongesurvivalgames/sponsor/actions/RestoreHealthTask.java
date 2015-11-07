package io.github.m0pt0pmatt.spongesurvivalgames.sponsor.actions;

import org.bukkit.entity.Player;

import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.TaskException;

/**
 * Restores a player's health when executed.
 * @author Skyler
 *
 */
public class RestoreHealthTask implements MenuPlayerTask {
	
	private static final String healMessage = "You're health has been restored by a sponsor!";
	
	@Override
	public void execute(Player player) throws TaskException {
		if (player == null || !player.isOnline()) {
			throw new TaskException();
		}
		
		player.setHealth(player.getMaxHealth());
		
		player.sendMessage(healMessage);
	}

	
}
