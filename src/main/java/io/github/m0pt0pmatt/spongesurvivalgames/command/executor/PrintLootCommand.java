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

package io.github.m0pt0pmatt.spongesurvivalgames.command.game.print;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

import java.util.Arrays;
import java.util.Set;

import io.github.m0pt0pmatt.spongesurvivalgames.SurvivalGame;
import io.github.m0pt0pmatt.spongesurvivalgames.command.ChainedCommandExecutor;
import io.github.m0pt0pmatt.spongesurvivalgames.command.CommandArgs;
import io.github.m0pt0pmatt.spongesurvivalgames.command.game.GameCommand;
import io.github.m0pt0pmatt.spongesurvivalgames.loot.Loot;

/**
 * Command to print the loot of a game
 */
public class PrintLootCommand implements CommandExecutor {

    private static final CommandExecutor COMMAND_EXECUTOR = new ChainedCommandExecutor(Arrays.asList(
            GameCommand.get(),
            new PrintLootCommand()
    ));

    private PrintLootCommand() {

    }

    public static CommandExecutor get() {
        return COMMAND_EXECUTOR;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        SurvivalGame survivalGame = (SurvivalGame) args.getOne(CommandArgs.SURVIVAL_GAME.toString()).orElseThrow(IllegalArgumentException::new);

        Set<Loot> loot = survivalGame.getLoot();
        src.sendMessage(Text.of("Printing loot for game \"" + survivalGame.getID() + "\":"));
        for (Loot l : loot) {
            src.sendMessage(Text.of("Item: " + l.getItem().toString() + ", Weight: " + l.getWeight()));
        }

        return CommandResult.success();
    }
}