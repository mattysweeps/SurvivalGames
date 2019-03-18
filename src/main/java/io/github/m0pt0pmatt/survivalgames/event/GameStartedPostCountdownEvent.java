package io.github.m0pt0pmatt.survivalgames.event;

import io.github.m0pt0pmatt.survivalgames.game.SurvivalGame;

public class GameStartedPostCountdownEvent extends PostCountdownEvent {
    public GameStartedPostCountdownEvent(SurvivalGame survivalGame) {
        super(survivalGame);
    }
}
