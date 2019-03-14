package io.github.m0pt0pmatt.survivalgames.task;

import io.github.m0pt0pmatt.survivalgames.command.CommandKeys;
import io.github.m0pt0pmatt.survivalgames.game.SurvivalGame;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.util.TextMessageException;
import org.spongepowered.api.world.World;

import static io.github.m0pt0pmatt.survivalgames.Util.getOrThrow;

public class LoadChunksTask implements Task {

    private static LoadChunksTask INSTANCE = new LoadChunksTask();

    private LoadChunksTask() {

    }

    @Override
    public void execute(SurvivalGame survivalGame) throws TextMessageException {

        String worldName =
                getOrThrow(survivalGame.getConfig().getWorldName(), CommandKeys.WORLD_NAME);
        World world = getOrThrow(Sponge.getServer().getWorld(worldName), CommandKeys.WORLD_NAME);

        // Force chunks to be loaded
        int minX = world.getLocation(survivalGame.getConfig().getBlockArea().getLesserBoundary().get()).getChunkPosition().getX();
        int maxX = world.getLocation(survivalGame.getConfig().getBlockArea().getGreaterBoundary().get()).getChunkPosition().getX();
        int minZ = world.getLocation(survivalGame.getConfig().getBlockArea().getLesserBoundary().get()).getChunkPosition().getZ();
        int maxZ = world.getLocation(survivalGame.getConfig().getBlockArea().getGreaterBoundary().get()).getChunkPosition().getZ();
        for (; minX <= maxX; minX++) {
            for (int z = minZ; z <= maxZ; z++) {
                world.loadChunk(minX, 0, z, false);
            }
        }
    }

    public static LoadChunksTask getInstance() {
        return INSTANCE;
    }
}
