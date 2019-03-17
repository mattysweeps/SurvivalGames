package io.github.m0pt0pmatt.survivalgames.event;

import io.github.m0pt0pmatt.survivalgames.game.SurvivalGame;
import io.github.m0pt0pmatt.survivalgames.game.SurvivalGameState;

public class GameStoppedEvent extends GameStateChangedEvent {
    public GameStoppedEvent(SurvivalGame survivalGame, SurvivalGameState previousState) {
        super(survivalGame, previousState, SurvivalGameState.STOPPED);
    }
}
