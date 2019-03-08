package io.github.m0pt0pmatt.survivalgames.task;

import io.github.m0pt0pmatt.survivalgames.game.SurvivalGame;
import org.spongepowered.api.util.TextMessageException;

public class CleanStateTask implements Task {

    private static final Task TASK = new CleanStateTask();

    private CleanStateTask() {

    }

    @Override
    public void execute(SurvivalGame survivalGame) throws TextMessageException {
        survivalGame.getPlayerSnapshots().clear();
        survivalGame.getPlayerUUIDs().clear();
        survivalGame.getSpectatorUUIDs().clear();
        survivalGame.getCommandBlocks().clear();
        survivalGame.getActiveMobSpawners().clear();
        survivalGame.getActiveEventIntervals().clear();
    }

    public static Task getInstance() {
        return TASK;
    }
}
