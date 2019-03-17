package io.github.m0pt0pmatt.survivalgames.event;

import io.github.m0pt0pmatt.survivalgames.game.SurvivalGame;
import io.github.m0pt0pmatt.survivalgames.game.SurvivalGameState;

public class GameStartedEvent extends GameStateChangedEvent {
    public GameStartedEvent(SurvivalGame survivalGame, SurvivalGameState previousState) {
        super(survivalGame, previousState, SurvivalGameState.RUNNING);
    }
}
