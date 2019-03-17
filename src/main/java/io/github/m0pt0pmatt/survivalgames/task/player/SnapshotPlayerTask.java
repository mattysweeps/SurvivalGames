package io.github.m0pt0pmatt.survivalgames.task.player;

import io.github.m0pt0pmatt.survivalgames.game.PlayerRestorer;
import io.github.m0pt0pmatt.survivalgames.game.SurvivalGame;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.util.TextMessageException;

public class SnapshotPlayerTask extends PlayerTask {

    private static final SnapshotPlayerTask INSTANCE = new SnapshotPlayerTask();

    private SnapshotPlayerTask() {

    }

    @Override
    public void execute(SurvivalGame survivalGame, Player player) throws TextMessageException {
        survivalGame.getPlayerSnapshots().put(player.getUniqueId(), new PlayerRestorer(player));
    }

    public static SnapshotPlayerTask getInstance() {
        return INSTANCE;
    }
}
