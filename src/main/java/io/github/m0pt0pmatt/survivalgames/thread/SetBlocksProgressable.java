package io.github.m0pt0pmatt.survivalgames.thread;

import com.flowpowered.math.vector.Vector3d;
import com.google.common.collect.Lists;
import io.github.m0pt0pmatt.survivalgames.game.SurvivalGame;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.tileentity.CommandBlock;
import org.spongepowered.api.world.World;

import java.util.ArrayList;

public class SetBlocksProgressable extends PercentageProgressable {

    private final SurvivalGame survivalGame;

    public SetBlocksProgressable(SurvivalGame survivalGame) {
        this.survivalGame = survivalGame;
    }

    @Override
    public void run() {
        setBlocks();
    }

    public void setBlocks() {
        Vector3d lesser = survivalGame.getConfig().getBlockArea().getLesserBoundary().orElseThrow(() -> new IllegalStateException("Missing boundaries"));
        Vector3d greater = survivalGame.getConfig().getBlockArea().getGreaterBoundary().orElseThrow(() -> new IllegalStateException("Missing boundaries"));

        ArrayList<BlockSnapshot> blocks = Lists.newArrayList();
        World world = survivalGame.getConfig().getWorldName().flatMap(n -> Sponge.getServer().getWorld(n)).orElseThrow(() -> new IllegalStateException("No world yet"));

        long totalBlocks = (greater.getFloorX() - lesser.getFloorX())
                * (greater.getFloorY() - lesser.getFloorY())
                * (greater.getFloorZ() - lesser.getFloorZ());
        long count = 0;

        for (int x = lesser.getFloorX(); x < greater.getFloorX(); x++) {
            for (int y = lesser.getFloorY(); y < greater.getFloorY(); y++) {
                for (int z = lesser.getFloorZ(); z < greater.getFloorZ(); z++) {
                    blocks.add(world.createSnapshot(x, y, z));
                    count++;
                    if (totalBlocks != 0) {
                        setPercentage((double) count / totalBlocks);
                    }
                }
            }
        }

        survivalGame.getConfig().setBlocks(blocks);

        world.getTileEntities()
                .stream()
                .filter(tileEntity -> tileEntity instanceof CommandBlock)
                .forEach(tileEntity -> survivalGame.getCommandBlocks().add((CommandBlock) tileEntity));
    }
}
