package io.github.m0pt0pmatt.survivalgames.task.player;

import io.github.m0pt0pmatt.survivalgames.game.SurvivalGame;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.util.TextMessageException;

public abstract class SpectatorTask extends AbstractPlayerTask {

    @Override
    protected void run(SurvivalGame survivalGame) throws TextMessageException {
        survivalGame.forEachSpectator(id -> Sponge.getServer().getPlayer(id).ifPresent(player -> {
            try {
                execute(survivalGame, player);
            } catch (TextMessageException e) {
                e.printStackTrace();
            }
        }));
    }
}
