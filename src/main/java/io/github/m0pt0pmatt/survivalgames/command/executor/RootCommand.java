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

package io.github.m0pt0pmatt.survivalgames.command.executor;

import com.google.common.collect.ImmutableList;
import io.github.m0pt0pmatt.survivalgames.command.executor.add.AddCommand;
import io.github.m0pt0pmatt.survivalgames.command.executor.print.ListGamesCommand;
import io.github.m0pt0pmatt.survivalgames.command.executor.print.PrintCommand;
import io.github.m0pt0pmatt.survivalgames.command.executor.remove.RemoveCommand;
import io.github.m0pt0pmatt.survivalgames.command.executor.set.SetCommand;
import io.github.m0pt0pmatt.survivalgames.command.executor.state.DeathMatchSurvivalGameCommand;
import io.github.m0pt0pmatt.survivalgames.command.executor.state.ReadySurvivalGameCommand;
import io.github.m0pt0pmatt.survivalgames.command.executor.state.StartSurvivalGameCommand;
import io.github.m0pt0pmatt.survivalgames.command.executor.state.StopSurvivalGameCommand;
import io.github.m0pt0pmatt.survivalgames.command.executor.unset.UnsetCommand;

public class RootCommand extends ParentCommand {

    private static final RootCommand INSTANCE = new RootCommand();
    static {
        INSTANCE.setChildren(
                ImmutableList.<SurvivalGamesCommand>builder()
                        .add(AddCommand.getInstance())
                        .add(CreateGameCommand.getInstance())
                        .add(DeleteGameCommand.getInstance())
                        .add(ListGamesCommand.getInstance())
                        .add(PrintCommand.getInstance())
                        .add(RemoveCommand.getInstance())
                        .add(SetCommand.getInstance())
                        .add(UnsetCommand.getInstance())
                        .add(ReadySurvivalGameCommand.getInstance())
                        .add(StartSurvivalGameCommand.getInstance())
                        .add(StopSurvivalGameCommand.getInstance())
                        .add(DeathMatchSurvivalGameCommand.getInstance())
                        .add(LoadConfigCommand.getInstance())
                        .add(SaveConfigCommand.getInstance())
                        .add(EventCommand.getInstance())
                        .add(DemoCommand.getInstance())
                        .add(TeleportCommand.getInstance())
                        .add(JoinCommand.getInstance())
                        .add(LeaveCommand.getInstance())
                        .add(SpectateCommand.getInstance())
                        .add(ScheduleCommand.getInstance())
                        .add(UnscheduleCommand.getInstance())
                        .build()
        );
    }

    private RootCommand() {
        super(null,"ssg");
    }

    @Override
    public String getPermission() {
        return "ssg";
    }

    public static RootCommand getInstance() {
        return INSTANCE;
    }
}
