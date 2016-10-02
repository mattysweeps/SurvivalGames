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

import static io.github.m0pt0pmatt.survivalgames.Util.getOrThrow;

import io.github.m0pt0pmatt.survivalgames.command.CommandKeys;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.Collections;
import java.util.Optional;

import javax.annotation.Nonnull;

class EventCommand extends BaseCommand {

    private static final EventCommand INSTANCE = new EventCommand();

    private EventCommand() {
        super(
                RootCommand.getInstance(),
                "event",
                GenericArguments.seq(
                        GenericArguments.string(CommandKeys.EVENT_NAME),
                        GenericArguments.firstParsing(
                                GenericArguments.seq(
                                        GenericArguments.string(CommandKeys.EVENT_EXECUTOR),
                                        GenericArguments.optional(GenericArguments.string(CommandKeys.EVENT_COMMAND))),
                                GenericArguments.optional(GenericArguments.string(CommandKeys.EVENT_COMMAND))
                        )),
                Collections.emptyMap());
    }

    @Nonnull
    @Override
    public CommandResult execute(@Nonnull CommandSource src, @Nonnull CommandContext args) throws CommandException {

        CommandSource executor = src;
        if (args.hasAny(CommandKeys.EVENT_EXECUTOR)) {
            String eventExecutorName = (String) getOrThrow(args, CommandKeys.EVENT_EXECUTOR);
            Optional<Player> player = Sponge.getServer().getPlayer(eventExecutorName);
            if (player.isPresent()) {
                executor = player.get();
            }
        }

        if (args.hasAny(CommandKeys.EVENT_COMMAND)) {
            String eventCommand = (String) getOrThrow(args, CommandKeys.EVENT_COMMAND);
            Sponge.getCommandManager().process(executor, eventCommand);
        }

        src.sendMessage(Text.of("This command is used with command blocks."));
        return CommandResult.success();
    }

    static EventCommand getInstance() {
        return INSTANCE;
    }
}
