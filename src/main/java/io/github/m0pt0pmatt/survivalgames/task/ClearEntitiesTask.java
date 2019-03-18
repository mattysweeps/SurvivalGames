package io.github.m0pt0pmatt.survivalgames.task;

import io.github.m0pt0pmatt.survivalgames.command.CommandKeys;
import io.github.m0pt0pmatt.survivalgames.game.SurvivalGame;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.util.TextMessageException;
import org.spongepowered.api.world.World;

import static io.github.m0pt0pmatt.survivalgames.Util.getOrThrow;

public class ClearEntitiesTask implements Task {

    private static ClearEntitiesTask INSTANCE = new ClearEntitiesTask();

    private ClearEntitiesTask() {

    }

    @Override
    public void execute(SurvivalGame survivalGame) throws TextMessageException {
        String worldName =
                getOrThrow(survivalGame.getConfig().getWorldName(), CommandKeys.WORLD_NAME);
        World world = getOrThrow(Sponge.getServer().getWorld(worldName), CommandKeys.WORLD_NAME);

        world.getEntities().stream()
                .filter(e -> !(e instanceof Player))
                .forEach(Entity::remove);
    }

    public static ClearEntitiesTask getInstance() {
        return INSTANCE;
    }
}
