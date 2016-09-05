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
package io.github.m0pt0pmatt.spongesurvivalgames.command.executor.create;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.text.Text;

import java.util.Collections;

import javax.annotation.Nonnull;

import io.github.m0pt0pmatt.spongesurvivalgames.command.CommandKeys;
import io.github.m0pt0pmatt.spongesurvivalgames.command.executor.BaseCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.command.executor.SurvivalGamesCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.game.SurvivalGame;
import io.github.m0pt0pmatt.spongesurvivalgames.game.SurvivalGameRepository;

public class CreateGameCommand extends BaseCommand {

    private static final SurvivalGamesCommand INSTANCE = new CreateGameCommand();

    private CreateGameCommand() {
        super(
                Collections.singletonList("create"),
                "",
                GenericArguments.string(CommandKeys.SURVIVAL_GAME_NAME),
                Collections.emptyMap()
        );
    }

    @Override
    @Nonnull
    public CommandResult execute(@Nonnull CommandSource src, @Nonnull CommandContext args) throws CommandException {
        String survivalGameName = (String) args.getOne(CommandKeys.SURVIVAL_GAME_NAME)
                .orElseThrow(() -> new CommandException(Text.of("No Such Survival Game")));

        if (SurvivalGameRepository.contains(survivalGameName)) {
            src.sendMessage(Text.of("Game of the same name already exists"));
            return CommandResult.empty();
        }

        SurvivalGameRepository.put(survivalGameName, new SurvivalGame(survivalGameName));
        src.sendMessage(Text.of("Game created"));
        return CommandResult.success();
    }

    public static SurvivalGamesCommand getInstance() {
        return INSTANCE;
    }
}
