package io.github.m0pt0pmatt.survivalgames.task.player;

import io.github.m0pt0pmatt.survivalgames.game.SurvivalGame;
import io.github.m0pt0pmatt.survivalgames.task.Task;
import org.spongepowered.api.entity.EntitySnapshot;
import org.spongepowered.api.util.TextMessageException;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class ReturnPlayerTask implements Task {

    private static final Task TASK = new ReturnPlayerTask();

    private ReturnPlayerTask() {
    }

    public static Task getInstance() {
        return TASK;
    }

    @Override
    public void execute(SurvivalGame survivalGame) throws TextMessageException {
        for(Iterator<Map.Entry<UUID, EntitySnapshot>> i = survivalGame.getPlayerSnapshots().entrySet().iterator(); i.hasNext();) {
            Map.Entry<UUID, EntitySnapshot> entry = i.next();
            i.remove();

            entry.getValue().restore();
        }
    }
}
