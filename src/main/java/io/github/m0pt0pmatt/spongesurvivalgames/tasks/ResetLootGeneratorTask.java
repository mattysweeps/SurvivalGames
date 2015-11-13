package io.github.m0pt0pmatt.spongesurvivalgames.tasks;

import io.github.m0pt0pmatt.spongesurvivalgames.SurvivalGame;

class ResetLootGeneratorTask implements SurvivalGameTask {
    @Override
    public boolean execute(SurvivalGame game) {
        game.getLootGenerator().clearLoot();
        game.getLoot().forEach(
                loot -> game.getLootGenerator().addLoot(loot)
        );
        return true;
    }
}
