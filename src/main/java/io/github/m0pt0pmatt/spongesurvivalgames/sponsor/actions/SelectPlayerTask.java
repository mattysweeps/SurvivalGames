package io.github.m0pt0pmatt.spongesurvivalgames.sponsor.actions;

import java.util.UUID;

import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.TaskException;
import io.github.m0pt0pmatt.spongesurvivalgames.sponsor.Sponsor;
import io.github.m0pt0pmatt.spongesurvivalgames.sponsor.SponsorMenu;

/**
 * Selects for a sponsor a player, and progresses the menu slides.<br />
 * @author Skyler
 *
 */
public class SelectPlayerTask implements MenuTask {

	private UUID selected;
	
	private Sponsor owner;
	
	public SelectPlayerTask(Sponsor sponsor, UUID selected) {
		this.selected = selected;
		this.owner = sponsor;
	}

	@Override
	public void execute() throws TaskException {
		SponsorMenu menu = new SponsorMenu(owner, selected);
		owner.setInventory(menu);
	}

}
