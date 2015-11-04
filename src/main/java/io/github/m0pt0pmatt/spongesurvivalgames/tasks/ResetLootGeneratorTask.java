package io.github.m0pt0pmatt.spongesurvivalgames.tasks;

import io.github.m0pt0pmatt.spongesurvivalgames.SurvivalGame;
import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.TaskException;

public class ResetLootGeneratorTask implements SurvivalGameTask {
    @Override
    public void execute(SurvivalGame game) throws TaskException {
        game.getLootGenerator().clearLoot();
        game.getLoot().forEach(
                loot -> game.getLootGenerator().addLoot(loot)
        );
    }
}
