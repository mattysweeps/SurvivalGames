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
package io.github.m0pt0pmatt.spongesurvivalgames.command.executor.state;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.text.Text;

import java.util.Collections;

import javax.annotation.Nonnull;

import io.github.m0pt0pmatt.spongesurvivalgames.command.CommandKeys;
import io.github.m0pt0pmatt.spongesurvivalgames.command.element.SurvivalGameCommandElement;
import io.github.m0pt0pmatt.spongesurvivalgames.command.executor.BaseCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.executor.SurvivalGamesCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.game.SurvivalGame;
import io.github.m0pt0pmatt.spongesurvivalgames.game.SurvivalGameState;
import io.github.m0pt0pmatt.spongesurvivalgames.game.SurvivalGameStateManager;

public class DeathMatchSurvivalGameCommand extends BaseCommand {

    private static final SurvivalGamesCommand INSTANCE = new DeathMatchSurvivalGameCommand();

    private DeathMatchSurvivalGameCommand() {
        super(Collections.singletonList("death-match"), "", SurvivalGameCommandElement.getInstance(), Collections.emptyMap());
    }

    @Nonnull
    @Override
    public CommandResult execute(@Nonnull CommandSource src, @Nonnull CommandContext args) throws CommandException {

        SurvivalGame survivalGame = (SurvivalGame) args.getOne(CommandKeys.SURVIVAL_GAME)
                .orElseThrow(() -> new CommandException(Text.of("No Survival Game")));

        if (survivalGame.getState() != SurvivalGameState.RUNNING) {
            throw new CommandException(Text.of("Must be in the " + SurvivalGameState.RUNNING + " states."));
        }

        SurvivalGameStateManager.deathMatch(survivalGame);

        src.sendMessage(Text.of("Death-match started"));
        return CommandResult.success();
    }

    public static SurvivalGamesCommand getInstance() {
        return INSTANCE;
    }
}
