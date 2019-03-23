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

package io.github.m0pt0pmatt.survivalgames.command.executor.unset;

import static com.google.common.base.Preconditions.checkNotNull;
import static io.github.m0pt0pmatt.survivalgames.Util.getOrThrow;
import static io.github.m0pt0pmatt.survivalgames.Util.sendSuccess;

import io.github.m0pt0pmatt.survivalgames.command.CommandKeys;
import io.github.m0pt0pmatt.survivalgames.command.element.SurvivalGameCommandElement;
import io.github.m0pt0pmatt.survivalgames.command.executor.LeafCommand;
import io.github.m0pt0pmatt.survivalgames.game.SurvivalGame;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.text.Text;

class AbstractUnsetCommand extends LeafCommand {

    private final Consumer<SurvivalGame> consumer;
    private final Text message;

    AbstractUnsetCommand(String name, Consumer<SurvivalGame> consumer, Text message) {
        super(UnsetCommand.getInstance(), name, SurvivalGameCommandElement.getInstance());
        this.consumer = checkNotNull(consumer, "consumer");
        this.message = checkNotNull(message, "message");
    }

    @Nonnull
    @Override
    public CommandResult executeCommand(@Nonnull CommandSource src, @Nonnull CommandContext args)
            throws CommandException {

        SurvivalGame survivalGame = (SurvivalGame) getOrThrow(args, CommandKeys.SURVIVAL_GAME);

        try {
            consumer.accept(survivalGame);
        } catch (RuntimeException e) {
            throw new CommandException(Text.of("Error while unsetting: " + e.getMessage()), e);
        }

        sendSuccess(src, message);
        return CommandResult.success();
    }
}
