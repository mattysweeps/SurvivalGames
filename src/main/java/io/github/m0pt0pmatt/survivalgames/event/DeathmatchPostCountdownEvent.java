package io.github.m0pt0pmatt.survivalgames.event;

import io.github.m0pt0pmatt.survivalgames.game.SurvivalGame;

public class DeathmatchPostCountdownEvent extends PostCountdownEvent {
    public DeathmatchPostCountdownEvent(SurvivalGame survivalGame) {
        super(survivalGame);
    }
}
