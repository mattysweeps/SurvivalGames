package io.github.m0pt0pmatt.survivalgames.event;

import io.github.m0pt0pmatt.survivalgames.game.SurvivalGame;

public class DeathmatchPreCountdownEvent extends PreCountdownEvent {
    public DeathmatchPreCountdownEvent(SurvivalGame survivalGame) {
        super(survivalGame);
    }
}
