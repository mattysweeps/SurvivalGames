package io.github.m0pt0pmatt.survivalgames.event;

import io.github.m0pt0pmatt.survivalgames.SurvivalGamesPlugin;
import io.github.m0pt0pmatt.survivalgames.game.SurvivalGame;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;

public class GameDeathmatchEvent extends SurvivalGameEvent {
    public GameDeathmatchEvent(SurvivalGame survivalGame) {
        super(Cause.of(EventContext.empty(), SurvivalGamesPlugin.PLUGIN), survivalGame);
    }
}
