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

package io.github.m0pt0pmatt.survivalgames.task;

import io.github.m0pt0pmatt.survivalgames.SurvivalGamesPlugin;
import io.github.m0pt0pmatt.survivalgames.game.SurvivalGame;

import io.github.m0pt0pmatt.survivalgames.thread.ProgressBuilder;
import io.github.m0pt0pmatt.survivalgames.thread.ResetBlocksProgressable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.util.TextMessageException;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/** Sets the blocks in the map */
public class RestoreBlocksTask implements Task {

    private static final Task INSTANCE = new RestoreBlocksTask();

    private RestoreBlocksTask() {

    }

    @Override
    public void execute(SurvivalGame survivalGame) throws CommandException {
        ProgressBuilder.builder(MessageChannel.TO_CONSOLE, SurvivalGamesPlugin.SYNC_EXECUTOR, SurvivalGamesPlugin.ASYNC_EXECUTOR)
                .runAsync(new ResetBlocksProgressable(survivalGame), "Resetting Blocks", Duration.of(5, ChronoUnit.MINUTES))
                .start();
    }

    public static Task getInstance() {
        return INSTANCE;
    }
}
