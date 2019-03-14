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

        world.getTileEntities()
                .stream()
                .filter(tileEntity -> tileEntity instanceof CommandBlock)
                .forEach(tileEntity -> survivalGame.getCommandBlocks().add((CommandBlock) tileEntity));
    }

    public static SetCommandBlocksTask getInstance() {
        return INSTANCE;
    }
}
