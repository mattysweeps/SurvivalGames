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

import static io.github.m0pt0pmatt.survivalgames.Util.toEntry;

import com.google.common.collect.ImmutableMap;
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
import org.spongepowered.api.command.CommandCallable;

import java.util.List;
import java.util.Map;

public class RootCommand extends ParentCommand {

    private static final Map<List<String>, CommandCallable> TOP_LEVEL_CHILDREN =
            ImmutableMap.<List<String>, CommandCallable>builder()
                    .put(toEntry(AddCommand.getInstance()))
                    .put(toEntry(CreateGameCommand.getInstance()))
                    .put(toEntry(DeleteGameCommand.getInstance()))
                    .put(toEntry(ListGamesCommand.getInstance()))
                    .put(toEntry(PrintCommand.getInstance()))
                    .put(toEntry(RemoveCommand.getInstance()))
                    .put(toEntry(SetCommand.getInstance()))
                    .put(toEntry(UnsetCommand.getInstance()))
                    .put(toEntry(ReadySurvivalGameCommand.getInstance()))
                    .put(toEntry(StartSurvivalGameCommand.getInstance()))
                    .put(toEntry(StopSurvivalGameCommand.getInstance()))
                    .put(toEntry(DeathMatchSurvivalGameCommand.getInstance()))
                    .put(toEntry(LoadConfigCommand.getInstance()))
                    .put(toEntry(SaveConfigCommand.getInstance()))
                    .put(toEntry(EventCommand.getInstance()))
                    .put(toEntry(DemoCommand.getInstance()))
                    .put(toEntry(TeleportCommand.getInstance()))
                    .put(toEntry(JoinCommand.getInstance()))
                    .put(toEntry(SpectateCommand.getInstance()))
                    .build();

    private static final SurvivalGamesCommand INSTANCE = new RootCommand();

    private RootCommand() {
        super("ssg", TOP_LEVEL_CHILDREN);
    }

    @Override
    public String getPermission() {
        return "ssg";
    }

    public static SurvivalGamesCommand getInstance() {
        return INSTANCE;
    }
}
