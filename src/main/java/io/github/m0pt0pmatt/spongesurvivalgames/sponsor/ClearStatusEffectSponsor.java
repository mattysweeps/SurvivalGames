package io.github.m0pt0pmatt.spongesurvivalgames.sponsor;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class ClearStatusEffectSponsor implements Sponsor{
	private final String playerMessage = "Sponsor has cleared all of your status effects!";
	@Override
	public void execute(Player player) {
		player.sendMessage(playerMessage);
		
		for(PotionEffect p : player.getActivePotionEffects()){
			player.removePotionEffect(p.getType());
		}
	}
	
}
