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

import com.google.common.collect.ImmutableList;
import io.github.m0pt0pmatt.survivalgames.SurvivalGamesPlugin;
import io.github.m0pt0pmatt.survivalgames.event.IntervalEvent;
import io.github.m0pt0pmatt.survivalgames.event.PlayerEvent;
import io.github.m0pt0pmatt.survivalgames.event.SurvivalGameEvent;
import io.github.m0pt0pmatt.survivalgames.interval.ActiveIntervalRepository;
import org.apache.commons.lang3.ClassUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.tileentity.CommandBlock;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.world.BlockChangeFlags;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class SurvivalGameEventListener {

    private static final SurvivalGameEventListener INSTANCE = new SurvivalGameEventListener();
    private static final String COMMAND_BLOCK_STRING = "/ssg event ";
    private static final List<Class<?>> survivalGameClasses = ClassUtils.getAllSuperclasses(SurvivalGameEvent.class);

    private SurvivalGameEventListener() {

    }

    @Listener
    public void fireCommandBlocks(SurvivalGameEvent event) {
        event.getSurvivalGame().getCommandBlocks().forEach(c -> handleCommandBlock(c, event));
    }

    private void handleCommandBlock(CommandBlock commandBlock, SurvivalGameEvent event) {
        Value<String> storedCommand = commandBlock.storedCommand();
        if (!storedCommand.exists()) {
            return;
        }

        String[] parts = storedCommand.get().trim().split("\\s+");
        if (parts.length < 3) {
            return;
        }

        if (!parts[0].equalsIgnoreCase("/ssg") || !parts[1].equalsIgnoreCase("event")) {
            return;
        }

        String eventName = parts[2];

        List<String> classes = ImmutableList.<Class<?>>builder()
                .add(event.getClass())
                .addAll(ClassUtils.getAllSuperclasses(event.getClass()))
                .build()
                .stream()
                .filter(c -> !survivalGameClasses.contains(c))
                .map(Class::getSimpleName)
                .collect(Collectors.toList());

        if (!classes.contains(eventName)) {
            return;
        }

        // execute other command
        if (parts.length > 3) {

            String internalCommand =
                    storedCommand.get().substring(COMMAND_BLOCK_STRING.length());

            CommandSource source = null;
            if (event instanceof PlayerEvent) {
                source = ((PlayerEvent) event).getPlayer();
            } else if (event instanceof IntervalEvent){
                source = commandBlock;
            }

            if (source != null) {
                Sponge.getCommandManager().process(source, internalCommand);
            }
        }

        // Redstone trick
        BlockSnapshot snapshot = ActiveIntervalRepository.getSnapshot(commandBlock.getLocation());
        commandBlock.getLocation().setBlock(BlockState.builder().blockType(BlockTypes.REDSTONE_TORCH).build(), BlockChangeFlags.ALL);
        SurvivalGamesPlugin.SYNC_EXECUTOR.schedule(() -> snapshot.restore(true, BlockChangeFlags.ALL), 1, TimeUnit.SECONDS);
    }

    public static SurvivalGameEventListener getInstance() {
        return INSTANCE;
    }
}
