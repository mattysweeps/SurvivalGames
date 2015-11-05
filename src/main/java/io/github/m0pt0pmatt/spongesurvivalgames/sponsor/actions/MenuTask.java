package io.github.m0pt0pmatt.spongesurvivalgames.sponsor.actions;

import org.bukkit.entity.Player;

import io.github.m0pt0pmatt.spongesurvivalgames.SurvivalGame;
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.TaskException;

/**
 * Performs an task for a menu.
 * @author Skyler
 *
 */
public interface MenuTask {

	public void execute(SurvivalGame game, Player player) throws TaskException;
	
}
