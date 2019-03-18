package io.github.m0pt0pmatt.survivalgames.event;

import io.github.m0pt0pmatt.survivalgames.game.SurvivalGame;

public class GameStartedPreCountdownEvent extends PreCountdownEvent {
    public GameStartedPreCountdownEvent(SurvivalGame survivalGame) {
        super(survivalGame);
    }
}
