package io.github.m0pt0pmatt.spongesurvivalgames.sponsor.actions;

import org.bukkit.entity.Player;

import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.TaskException;

/**
 * Performs an task for a menu, which targets a player
 * @author Skyler
 *
 */
public interface MenuPlayerTask {

	public void execute(Player player) throws TaskException;
	
}
