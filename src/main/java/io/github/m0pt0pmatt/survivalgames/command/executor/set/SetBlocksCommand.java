/*
 * This file is part of SurvivalGames, licensed under the MIT License (MIT).
 *
 * Copyright (c) Matthew Broomfield <m0pt0pmatt17@gmail.com>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package io.github.m0pt0pmatt.survivalgames.command.executor.set;

import static io.github.m0pt0pmatt.survivalgames.Util.getOrThrow;
import static io.github.m0pt0pmatt.survivalgames.Util.sendSuccess;

import com.flowpowered.math.vector.Vector3d;
import io.github.m0pt0pmatt.survivalgames.command.CommandKeys;
import io.github.m0pt0pmatt.survivalgames.command.element.SurvivalGameCommandElement;
import io.github.m0pt0pmatt.survivalgames.command.executor.BaseCommand;
import io.github.m0pt0pmatt.survivalgames.command.executor.LeafCommand;
import io.github.m0pt0pmatt.survivalgames.command.executor.SurvivalGamesCommand;
import io.github.m0pt0pmatt.survivalgames.data.GameConfig;
import io.github.m0pt0pmatt.survivalgames.game.SurvivalGame;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.tileentity.CommandBlock;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.world.World;

import java.util.Collections;

import javax.annotation.Nonnull;

class SetBlocksCommand extends LeafCommand {

    private static final SurvivalGamesCommand INSTANCE = new SetBlocksCommand();

    private SetBlocksCommand() {
        super(
                SetCommand.getInstance(),
                "blocks",
                SurvivalGameCommandElement.getInstance()
        );
    }

    @Nonnull
    @Override
    public CommandResult execute(@Nonnull CommandSource src, @Nonnull CommandContext args) throws CommandException {

        SurvivalGame survivalGame = (SurvivalGame) getOrThrow(args, CommandKeys.SURVIVAL_GAME);
        GameConfig config = survivalGame.getConfig();
        Vector3d lesserBoundary = getOrThrow(config.getBlockArea().getLesserBoundary(), CommandKeys.LESSER_BOUNDARY);
        Vector3d greaterBoundary = getOrThrow(config.getBlockArea().getGreaterBoundary(), CommandKeys.GREATER_BOUNDARY);
        String worldName = getOrThrow(config.getWorldName(), CommandKeys.WORLD_NAME);
        World world = getOrThrow(Sponge.getServer().getWorld(worldName), CommandKeys.WORLD);

        survivalGame.getBlocks().clear();
        survivalGame.getCommandBlocks().clear();

        for (int x = lesserBoundary.getFloorX(); x < greaterBoundary.getFloorX() + 1; x++) {
            for (int y = lesserBoundary.getFloorY(); y < greaterBoundary.getFloorY() + 1; y++) {
                for (int z = lesserBoundary.getFloorZ(); z < greaterBoundary.getFloorZ() + 1; z++) {
                    survivalGame.getBlocks().add(world.getLocation(x, y, z).createSnapshot());
                }
            }
        }

        world.getTileEntities().stream()
                .filter(tileEntity -> tileEntity instanceof CommandBlock)
                .forEach(tileEntity -> survivalGame.getCommandBlocks().add((CommandBlock) tileEntity));

        survivalGame.setBlocksValid(true);

        sendSuccess(src, "Set map blocks and command blocks");
        return CommandResult.success();
    }

    static SurvivalGamesCommand getInstance() {
        return INSTANCE;
    }
}
