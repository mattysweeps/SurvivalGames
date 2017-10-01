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

import static io.github.m0pt0pmatt.survivalgames.Util.toEntry;

import com.google.common.collect.ImmutableMap;
import io.github.m0pt0pmatt.survivalgames.command.executor.ParentCommand;
import io.github.m0pt0pmatt.survivalgames.command.executor.RootCommand;
import io.github.m0pt0pmatt.survivalgames.command.executor.SurvivalGamesCommand;
import java.util.List;
import org.spongepowered.api.command.CommandCallable;

public class SetCommand extends ParentCommand {

    private static final SurvivalGamesCommand INSTANCE = new SetCommand();

    private SetCommand() {
        super(
                RootCommand.getInstance(),
                "set",
                ImmutableMap.<List<String>, CommandCallable>builder()
                        .put(toEntry(SetBlocksCommand.getInstance()))
                        .put(toEntry(SetBoundaryCommand.getInstance()))
                        .put(toEntry(SetCenterVectorCommand.getInstance()))
                        .put(toEntry(SetChestMidpointCommand.getInstance()))
                        .put(toEntry(SetChestRangeCommand.getInstance()))
                        .put(toEntry(SetCountDownSecondsCommand.getInstance()))
                        .put(toEntry(SetExitVectorCommand.getInstance()))
                        .put(toEntry(SetExitWorldNameCommand.getInstance()))
                        .put(toEntry(SetPlayerLimitCommand.getInstance()))
                        .put(toEntry(SetWorldNameCommand.getInstance()))
                        .build());
    }

    public static SurvivalGamesCommand getInstance() {
        return INSTANCE;
    }
}
