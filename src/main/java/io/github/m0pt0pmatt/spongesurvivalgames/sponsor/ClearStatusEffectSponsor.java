package io.github.m0pt0pmatt.spongesurvivalgames.sponsor;

import org.spongepowered.api.data.manipulator.mutable.PotionEffectData;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

class ClearStatusEffectSponsor implements Sponsor {

    @Override
    public void execute(Player player) {
        String playerMessage = "Sponsor has cleared all of your status effects!";
        player.sendMessage(Text.of(playerMessage));
        player.get(PotionEffectData.class).ifPresent(p -> p.removeAll(p.effects()));
    }
}
