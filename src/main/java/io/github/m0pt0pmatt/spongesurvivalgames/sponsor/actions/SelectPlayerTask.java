package io.github.m0pt0pmatt.spongesurvivalgames.sponsor.actions;

import java.util.UUID;

import org.bukkit.entity.Player;

import io.github.m0pt0pmatt.spongesurvivalgames.SurvivalGame;
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.TaskException;
import io.github.m0pt0pmatt.spongesurvivalgames.sponsor.Sponsor;

/**
 * Selects for a sponsor a player, and progresses the menu slides.<br />
 * @author Skyler
 *
 */
public class SelectPlayerTask implements MenuTask {

	private UUID selected;
	
	private Sponsor owner;
	
	public SelectPlayerTask(Sponsor sponsor, UUID selected) {
		this.selected = uuid;
		this.owner = sponsor;
	}

	@Override
	public void execute(SurvivalGame game, Player player) throws TaskException {
		player.getInventory().addItem(this.item);
	}

}
