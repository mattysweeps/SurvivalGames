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
package io.github.m0pt0pmatt.survivalgames.listener;

import io.github.m0pt0pmatt.survivalgames.SurvivalGamesPlugin;
import io.github.m0pt0pmatt.survivalgames.event.PlayerEvent;
import io.github.m0pt0pmatt.survivalgames.event.SurvivalGameEvent;
import java.util.concurrent.TimeUnit;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.tileentity.CommandBlock;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.world.BlockChangeFlags;

public class SurvivalGameEventListener {

    private static SurvivalGameEventListener INSTANCE = new SurvivalGameEventListener();
    private static final String COMMAND_BLOCK_STRING = "/ssg event ";

    private SurvivalGameEventListener() {}

    @Listener
    public void fireCommandBlocks(SurvivalGameEvent event) {
        String eventName = event.getClass().getSimpleName();

        for (CommandBlock commandBlock : event.getSurvivalGame().getCommandBlocks()) {

            Value<String> storedCommand = commandBlock.storedCommand();
            if (storedCommand.exists()) {
                if (storedCommand.get().startsWith(COMMAND_BLOCK_STRING)) {

                    String commandBlockCommandName =
                            storedCommand.get().substring(COMMAND_BLOCK_STRING.length());

                    if (eventName.equalsIgnoreCase(commandBlockCommandName)) {

                        if (event instanceof PlayerEvent) {
                            String originalCommand = storedCommand.get();

                            // Strip off "/ssg event [event]" to get the real command
                            String actual = storedCommand.get()
                                    .substring(COMMAND_BLOCK_STRING.length() + commandBlockCommandName.length() + 1);
                            storedCommand.set(actual);
                            commandBlock.execute();
                            storedCommand.set(originalCommand);


                        } else {
                            commandBlock.execute();
                        }

                        BlockSnapshot snapshot =
                                commandBlock.getBlock().snapshotFor(commandBlock.getLocation());
                        commandBlock
                                .getLocation()
                                .setBlock(
                                        BlockState.builder()
                                                .blockType(BlockTypes.REDSTONE_TORCH)
                                                .build(),
                                        BlockChangeFlags.ALL);

                        SurvivalGamesPlugin.SYNC_EXECUTOR.schedule(
                                () -> snapshot.restore(true, BlockChangeFlags.ALL),
                                5,
                                TimeUnit.SECONDS);
                    }
                }
            }
        }
    }

    public static SurvivalGameEventListener getInstance() {
        return INSTANCE;
    }
}
