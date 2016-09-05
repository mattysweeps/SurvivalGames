/*
 * This file is part of SpongeSurvivalGamesPlugin, licensed under the MIT License (MIT).
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
package io.github.m0pt0pmatt.spongesurvivalgames.command.executor;

import com.google.common.collect.ImmutableMap;

import org.spongepowered.api.command.CommandCallable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.github.m0pt0pmatt.spongesurvivalgames.command.executor.add.AddCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.executor.create.CreateGameCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.executor.delete.DeleteGameCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.executor.list.ListGameCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.executor.print.PrintCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.executor.remove.RemoveCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.executor.set.SetCommand;

import static io.github.m0pt0pmatt.spongesurvivalgames.command.executor.CommandUtil.toEntry;

public class RootCommand extends ParentCommand {

    private static final List<String> COMMAND_ALIASES = Arrays.asList(
            "spongesurvivalgames",
            "ssg");

    private static final String PERMISSION = "";

    private static final Map<List<String>, CommandCallable> TOP_LEVEL_CHILDREN =
            ImmutableMap.<List<String>, CommandCallable>builder()
                    .put(toEntry(AddCommand.getInstance()))
                    .put(toEntry(CreateGameCommand.getInstance()))
                    .put(toEntry(DeleteGameCommand.getInstance()))
                    .put(toEntry(ListGameCommand.getInstance()))
                    .put(toEntry(PrintCommand.getInstance()))
                    .put(toEntry(RemoveCommand.getInstance()))
                    .put(toEntry(SetCommand.getInstance()))
                    .build();

    private static final SurvivalGamesCommand INSTANCE = new RootCommand();

    private RootCommand() {
        super(COMMAND_ALIASES, PERMISSION, TOP_LEVEL_CHILDREN);
    }

    public static SurvivalGamesCommand getInstance() {
        return INSTANCE;
    }
}
