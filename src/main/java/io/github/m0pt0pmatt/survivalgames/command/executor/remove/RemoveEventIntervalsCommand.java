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
package io.github.m0pt0pmatt.survivalgames.command.executor.remove;

import static io.github.m0pt0pmatt.survivalgames.Util.getOrThrow;

import io.github.m0pt0pmatt.survivalgames.command.CommandKeys;
import io.github.m0pt0pmatt.survivalgames.command.element.EventIntervalCommandElement;
import io.github.m0pt0pmatt.survivalgames.command.element.SurvivalGameCommandElement;
import io.github.m0pt0pmatt.survivalgames.command.executor.LeafCommand;
import io.github.m0pt0pmatt.survivalgames.command.executor.SurvivalGamesCommand;
import io.github.m0pt0pmatt.survivalgames.game.SurvivalGame;
import io.github.m0pt0pmatt.survivalgames.game.SurvivalGameState;
import javax.annotation.Nonnull;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.text.Text;

class RemoveEventIntervalsCommand extends LeafCommand {

    private static SurvivalGamesCommand INSTANCE = new RemoveEventIntervalsCommand();

    private RemoveEventIntervalsCommand() {
        super(
                RemoveCommand.getInstance(),
                "event-interval",
                GenericArguments.seq(
                        SurvivalGameCommandElement.getInstance(),
                        EventIntervalCommandElement.getInstance()));
    }

    @Nonnull
    @Override
    public CommandResult executeCommand(@Nonnull CommandSource src, @Nonnull CommandContext args)
            throws CommandException {

        SurvivalGame survivalGame = (SurvivalGame) getOrThrow(args, CommandKeys.SURVIVAL_GAME);
        String intervalName = (String) getOrThrow(args, CommandKeys.INTERVAL_NAME);

        if (survivalGame.getState() != SurvivalGameState.STOPPED) {
            throw new CommandException(Text.of("State must be " + SurvivalGameState.STOPPED));
        }

        survivalGame.getConfig().getEventIntervals().remove(intervalName);
        src.sendMessage(Text.of("Removed event interval", intervalName));
        return CommandResult.success();
    }

    static SurvivalGamesCommand getInstance() {
        return INSTANCE;
    }
}
