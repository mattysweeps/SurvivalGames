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

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.text.Text;

import javax.annotation.Nonnull;

public abstract class BaseCommand implements SurvivalGamesCommand {

    private final List<String> aliases;
    private final CommandElement arguments;
    private final SurvivalGamesCommand parentCommand;

    protected BaseCommand(
            SurvivalGamesCommand parentCommand,
            String name,
            CommandElement arguments) {
        this.parentCommand = parentCommand;
        this.aliases = Collections.singletonList(checkNotNull(name, "name"));
        this.arguments = checkNotNull(arguments, "arguments");
    }

    @Nonnull
    public abstract CommandResult executeCommand(@Nonnull CommandSource src, @Nonnull CommandContext args) throws CommandException;

    @Override
    public boolean testPermission(CommandSource source) {
        return getPermission() == null || source.hasPermission(getPermission())
                || Optional.ofNullable(parentCommand).map(p -> p.testPermission(source)).orElse(false);
    }

    @Override
    @Nonnull
    public final CommandResult execute(@Nonnull CommandSource src, @Nonnull CommandContext args) throws CommandException {
        if (!testPermission(src)) {
            throw new CommandException(Text.of("You do not have permission to run this command"));
        }
        return executeCommand(src, args);
    }

    @Override
    public final List<String> getAliases() {
        return aliases;
    }

    @Override
    public final CommandElement getArguments() {
        return arguments;
    }

    @Override
    public String getPermission() {
        if (parentCommand == null) {
            return aliases.get(0);
        }

        return parentCommand.getPermission() + "." + aliases.get(0);
    }
}
