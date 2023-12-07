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

package io.github.m0pt0pmatt.survivalgames.command.executor.print;

import com.google.common.collect.ImmutableList;
import io.github.m0pt0pmatt.survivalgames.command.executor.ParentCommand;
import io.github.m0pt0pmatt.survivalgames.command.executor.RootCommand;
import io.github.m0pt0pmatt.survivalgames.command.executor.SurvivalGamesCommand;

public class PrintCommand extends ParentCommand {

    private static final SurvivalGamesCommand INSTANCE;
    static {
        INSTANCE = new PrintCommand();
        ((PrintCommand) INSTANCE).setChildren(
                ImmutableList.<SurvivalGamesCommand>builder()
                        .add(PrintBoundariesCommand.getInstance())
                        .add(PrintCenterLocationCommand.getInstance())
                        .add(PrintCountDownSecondsCommand.getInstance())
                        .add(PrintExitVectorCommand.getInstance())
                        .add(PrintExitWorldNameCommand.getInstance())
                        .add(PrintMobSpawnAreasCommand.getInstance())
                        .add(PrintPlayerLimitCommand.getInstance())
                        .add(PrintPlayersCommand.getInstance())
                        .add(PrintSpawnsCommand.getInstance())
                        .add(PrintSurvivalGameStateCommand.getInstance())
                        .add(PrintWorldNameCommand.getInstance())
                        .add(PrintEventIntervalsCommand.getInstance())
                        .add(PrintGameModeCommand.getInstance())
                        .build()
        );
    }

    private PrintCommand() {
        super(RootCommand.getInstance(), "print");
    }

    public static SurvivalGamesCommand getInstance() {
        return INSTANCE;
    }
}
