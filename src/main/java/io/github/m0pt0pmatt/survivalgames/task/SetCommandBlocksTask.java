package io.github.m0pt0pmatt.survivalgames.task;

import io.github.m0pt0pmatt.survivalgames.Util;
import io.github.m0pt0pmatt.survivalgames.game.SurvivalGame;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.tileentity.CommandBlock;
import org.spongepowered.api.util.TextMessageException;
import org.spongepowered.api.world.World;

public class SetCommandBlocksTask implements Task {

    private static final SetCommandBlocksTask INSTANCE = new SetCommandBlocksTask();

    private SetCommandBlocksTask() {

    }

    @Override
    public void execute(SurvivalGame survivalGame) throws TextMessageException {
        String worldName = Util.getOrThrow(survivalGame.getConfig().getWorldName(), "world-name");
        World world = Util.getOrThrow(Sponge.getServer().getWorld(worldName), "world");

        survivalGame.getCommandBlocks().clear();

        // Force chunks to be loaded
        int minX = world.getLocation(survivalGame.getConfig().getBlockArea().getLesserBoundary().get()).getChunkPosition().getX();
        int maxX = world.getLocation(survivalGame.getConfig().getBlockArea().getGreaterBoundary().get()).getChunkPosition().getX();
        int minZ = world.getLocation(survivalGame.getConfig().getBlockArea().getLesserBoundary().get()).getChunkPosition().getZ();
        int maxZ = world.getLocation(survivalGame.getConfig().getBlockArea().getGreaterBoundary().get()).getChunkPosition().getZ();
        for (; minX <= maxX; minX++) {
            for (; minZ <= maxZ; minZ++) {
                world.loadChunk(minX, 0, minZ, false);
            }
        }

        world.getTileEntities()
                .stream()
                .filter(tileEntity -> tileEntity instanceof CommandBlock)
                .forEach(tileEntity -> survivalGame.getCommandBlocks().add((CommandBlock) tileEntity));
    }

    public static SetCommandBlocksTask getInstance() {
        return INSTANCE;
    }
}
