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

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.text.Text;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class ParentCommand extends BaseCommand {

    private Text chooseChildMessage;
    private Map<List<String>, CommandCallable> children = new HashMap<>();

    ParentCommand(String name) {
        this(null, name);
    }

    protected ParentCommand(
            SurvivalGamesCommand parentCommand,
            String name) {
        super(parentCommand, name, GenericArguments.none());
    }

    protected void setChildren(Map<List<String>, CommandCallable> children){
        chooseChildMessage =
                Text.of("Select a child command: ")
                        .concat(
                                Text.joinWith(
                                        Text.of(':'),
                                        children.keySet()
                                                .stream()
                                                .flatMap(Collection::stream)
                                                .map(Text::of)
                                                .collect(Collectors.toList())));
        children.forEach(
                (aliasList, element) -> {
                    checkNotNull(aliasList, "aliasList");
                    checkNotNull(element, "childCommand");
                    aliasList.forEach(alias -> checkNotNull(alias, "alias"));
                });
        this.children = children;
    }

    @Override
    public final Map<List<String>, CommandCallable> getChildren() {
        return children;
    }

    @Nonnull
    @Override
    public final CommandResult execute(@Nonnull CommandSource src, @Nonnull CommandContext args)
            throws CommandException {
        throw new CommandException(chooseChildMessage);
    }
}
